package snail007;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import jodd.io.FileUtil;
import jodd.io.StreamUtil;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import jodd.util.SystemUtil;
import snail007.bean.ConfigBean;
import snail007.bean.ServiceBean;
import snail007.sdk.ProxyJNI.PLibrary;

public class Utils {

	public static ConfigBean config;

	public static void init(Main main) {
		initDir(main);
		initTable(main);
		reloadConfig();
	}

	private static void initTable(Main main) {
		DefaultTableModel model = (DefaultTableModel) main.jTable1.getModel();
		model.setRowCount(0);
		//ArrayList<ServiceBean> beans = new ArrayList<>();
		File[] files = new File("data").listFiles((File dir, String name) -> name.endsWith(".json"));
		int rowIndex = 0;
		for (File f : files) {
			try {
				String json = FileUtil.readString(f);
				ServiceBean bean = new JsonParser().parse(json, ServiceBean.class);
				bean.file = f;
				model.addRow(new Object[]{bean.Name, bean.Args, bean.IsLog, bean.CertFilePath, bean.KeyFilePath, "停止", bean.AutoStart, "查看", "启动/停止", "修改", f});
				if (bean.AutoStart) {
					String err = Utils.start(bean);
					if (err.isEmpty()) {
						model.setValueAt("运行中", rowIndex, 5);
					}
				}

			} catch (IOException ex) {
			}
			rowIndex++;
		}
	}

	private static void initDir(Main main) {
		if (!new File("data").isDirectory()) {
			new File("data").mkdir();
		}
		if (!new File("certs").isDirectory()) {
			new File("certs").mkdir();
		}
		if (!new File("logs").isDirectory()) {
			new File("logs").mkdir();
		}
	}

	public static void reloadConfig() {
		String f = "conf/app.conf";
		if (!new File(f).isFile()) {
			try {
				String json = new JsonSerializer().serialize(new ConfigBean());
				FileUtil.writeString(f, json);
			} catch (IOException ex) {
				Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		config = ConfigBean.factory(f);
		if (config.IsGlobalHttpProxy) {
			setGlobalHttpProxy(config.GlobalHttpProxyIP, config.GlobalHttpProxyPort);
		} else {
			unsetGlobalHttpProxy();
		}
		if (config.IsAutostart) {
			setAutostartup();
		} else {
			unsetAutostartup();
		}

	}

	public static boolean saveConfigBean(ConfigBean bean) {
		String json = new JsonSerializer().serialize(bean);
		try {
			String filename = "conf/app.conf";
			FileUtil.writeString(filename, json);
		} catch (IOException ex) {
			return false;
		}
		return true;
	}

	public static boolean saveBean(ServiceBean bean) {
		String json = new JsonSerializer().serialize(bean);
		try {
			if (bean.file != null) {
				FileUtil.writeString(bean.file.getAbsolutePath(), json);
			} else {
				String filename = String.valueOf(System.currentTimeMillis()) + ".json";
				FileUtil.writeString("data/" + filename, json);
			}
		} catch (IOException ex) {
			return false;
		}
		return true;
	}

	public static boolean setGlobalHttpProxy(String ip, String port) {
		if (SystemUtil.isHostWindows()) {
			try {
				ProcessBuilder pb = new ProcessBuilder(new String[]{new File("conf/proxysetting.exe").getAbsolutePath(), ip + ":" + port});
				pb.directory(new File("conf"));
				pb.redirectErrorStream(true);
				Process p = pb.start();
				System.out.println(new String(StreamUtil.readChars(p.getInputStream())));
				return true;
			} catch (IOException ex) {
				Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return false;
	}

	public static boolean unsetGlobalHttpProxy() {
		if (SystemUtil.isHostWindows()) {
			try {
				ProcessBuilder pb = new ProcessBuilder(new String[]{new File("conf/proxysetting.exe").getAbsolutePath(), "stop"});
				pb.directory(new File("conf"));
				pb.redirectErrorStream(true);
				Process p = pb.start();
				System.out.println(new String(StreamUtil.readChars(p.getInputStream())));
				return true;
			} catch (IOException ex) {
				Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return false;
	}

	public static boolean setAutostartup() {
		String[] start = null;
		if (SystemUtil.isHostWindows()) {
			start = new String[]{new File("conf/autostart.exe").getAbsolutePath(), "enable", "-n", "GoProxy", "-k", "goproxy", "-c", "wscript.exe \"" + new File("start.vbs").getAbsolutePath() + "\""};
		}
		if (SystemUtil.isHostLinux()) {
			new File("conf/autostart").setExecutable(true);
			start = new String[]{"./autostart", "enable", "-n", "GoProxy", "-k", "goproxy", "-c", "bash \"" + new File("start.sh").getAbsolutePath() + "\""};
		}
		if (start != null) {
			try {
				ProcessBuilder pb = new ProcessBuilder(start);
				pb.directory(new File("conf"));
				pb.redirectErrorStream(true);
				Process p = pb.start();
				System.out.println(new String(StreamUtil.readChars(p.getInputStream())));
				return true;
			} catch (Exception ex) {
				Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return false;
	}

	public static boolean unsetAutostartup() {
		String[] start = null;
		if (SystemUtil.isHostWindows()) {
			start = new String[]{new File("conf/autostart.exe").getAbsolutePath(), "disable", "-k", "goproxy"};
		}
		if (SystemUtil.isHostLinux()) {
			start = new String[]{"./autostart", "disable", "-k", "goproxy"};
		}
		try {
			Runtime.getRuntime().exec(start, null, new File("conf"));
			return true;
		} catch (IOException ex) {
			Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public static String start(ServiceBean bean) {
		String id = bean.file.getName();
		String args = bean.Args.trim().replaceAll("(\r|\n)", "").trim();
		if (bean.IsLog) {
			args += " --log logs/" + id + ".log";
		}
		if (!bean.CertFilePath.trim().isEmpty() && !bean.KeyFilePath.trim().isEmpty()) {
			args += " -C " + bean.CertFilePath + " -K " + bean.KeyFilePath;
		}
		return PLibrary.INSTANCE.Start(id, args);
	}

	public static void stop(ServiceBean bean) {
		String id = bean.file.getName();
		PLibrary.INSTANCE.Stop(id);
	}

}
