package snail007;

import javax.swing.JOptionPane;


public class Checker {

	public static boolean check(AddService addFrame) {
		if (addFrame.jTextField1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null,"名称不能为空!");
			return false;
		}
		if (addFrame.jTextArea1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null,"参数不能为空!");
			return false;
		}
		return  true;
	}
}
