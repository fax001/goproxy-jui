package snail007;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.JFrame;

public class MySystemTray {
	
	JFrame frame;
	
	public MySystemTray(JFrame frame) {
		this.frame = frame;
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
			}
		});
	}

	//添加托盘显示：1.先判断当前平台是否支持托盘显示  
	public void setTray() {
		if (SystemTray.isSupported()) {//判断当前平台是否支持托盘功能  
			//创建托盘实例  
			SystemTray tray = SystemTray.getSystemTray();
			//创建托盘图标：1.显示图标Image 2.停留提示text 3.弹出菜单popupMenu 4.创建托盘图标实例  
			//1.创建Image图像  
			Image image = null;
			try {
				image = ImageIO.read(PFrame.class.getResource("/resources/tray.png"));
			} catch (IOException ex) {
				Logger.getLogger(MySystemTray.class.getName()).log(Level.SEVERE, null, ex);
			}
			//2.停留提示text  
			String text = "GoProxy";
			//3.弹出菜单popupMenu  
			PopupMenu popMenu = new PopupMenu();
			MenuItem itmOpen = new MenuItem("打开");
			itmOpen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Show();
				}
			});
			MenuItem itmHide = new MenuItem("隐藏");
			itmHide.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					UnVisible();
				}
			});
			MenuItem itmExit = new MenuItem("退出");
			itmExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Exit();
				}
			});
			popMenu.add(itmOpen);
			popMenu.add(itmHide);
			popMenu.add(itmExit);

			//创建托盘图标  
			TrayIcon trayIcon = new TrayIcon(image, text, popMenu);
			trayIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Show();
				}
			});
			//将托盘图标加到托盘上  
			try {
				tray.add(trayIcon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		}
	}

	//内部类中不能直接调用外部类（this不能指向）  
	public void UnVisible() {
		frame.setVisible(false);
	}
	
	public void Show() {
		frame.setVisible(true);
	}
	
	public void Exit() {
		System.exit(0);
	}
	
}
