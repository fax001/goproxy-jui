package snail007.bean;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jodd.io.FileUtil;
import jodd.json.JsonParser;

public class ConfigBean {

	public boolean IsStartupAutoHide;
	public boolean IsGlobalHttpProxy;
	public boolean IsAutostart;
	public String GlobalHttpProxyIP;
	public String GlobalHttpProxyPort;

	public ConfigBean() {
		this.IsStartupAutoHide = false;
		this.IsGlobalHttpProxy = false;
		this.IsAutostart = false;
		this.GlobalHttpProxyIP = "127.0.0.1";
		this.GlobalHttpProxyPort = "";
	}

	public static ConfigBean factory(String configFilePath) {
		try {
			String json = FileUtil.readString(new File(configFilePath));
			ConfigBean bean = new JsonParser().parse(json, ConfigBean.class);
			return bean;
		} catch (IOException ex) {
			Logger.getLogger(ConfigBean.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
