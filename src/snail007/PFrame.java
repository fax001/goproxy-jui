/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snail007;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author pengmeng
 */
public class PFrame extends JFrame {

	public PFrame() {
		try {
			this.setIconImage(ImageIO.read(PFrame.class.getResource("/resources/logo.png")));
		} catch (IOException ex) {
			Logger.getLogger(PFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
