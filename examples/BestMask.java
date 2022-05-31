package Main;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class BestMask {
	
	public static void main(String[] args) {
		try {
			BufferedImage image = ImageIO.read(new File("./qr_code.png"));
			int mask = 0, penalty = Integer.MAX_VALUE;
			for(int i = 0; i<8; i++) {
				BufferedImage masked = new CodeMasking(image).applyMaskPattern(i);
				int p = new Interpreter(masked).getPenalty();
				if(p < penalty) {
					mask = i;
					penalty = p;
				} System.out.println("Mask: " + i + ", penalty: " + p);
			} System.out.println("Best mask: " + mask + " (" + penalty + ")");
		} catch(Exception e) { e.printStackTrace(); }
	}

}
 
