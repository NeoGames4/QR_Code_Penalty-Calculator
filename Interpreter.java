package Main;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * This class can be used to receive information about a QR Code.
 * @author Mika Thein
 * @version 1.0
 * @see #Interpreter(BufferedImage)
 * @see #getVersion()
 * @see #getModuleSize()
 * @see #getPenalty()
 */
public class Interpreter {
	
	/**
	 * The QR Code.
	 */
	public final BufferedImage image;
	
	/**
	 * The dark module color / foreground color.
	 */
	public static final Color onColor = Color.BLACK;
	/**
	 * The light module color / background color.
	 */
	public static final Color offColor = Color.WHITE;
	
	/**
	 * Creates a new Interpreter which can be used to determine some information about the QR code.
	 * <p> Originally this class was created for calculating the penalty score.
	 * @param image the QR Code.
	 * @see #getModuleSize()
	 * @see #getVersion()
	 * @see #getPenalty()
	 */
	public Interpreter(BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * Determines the module size.
	 * @return the module size in pixels.
	 */
	public int getModuleSize() {
		int size = 0;
		for(int y = 0; y<image.getHeight(); y++) {
			for(int x = 0; x<image.getWidth(); x++) {
				if(Color.decode(image.getRGB(x, y) + "").equals(offColor)) continue;
				while(Color.decode(image.getRGB(x, y) + "").equals(onColor)) {
					size++;
					x++;
				} if(size > 0) return size / 7;
			}
		} throw new RuntimeException("Can't determine module size.");
	}
	
	/**
	 * Returns the version number of the QR code.
	 * @return the version number.
	 */
	public int getVersion() { // s = v * 4 + 17 <=> v = (s - 17) / 4
		return (image.getWidth()/getModuleSize() - 17) / 4;
	}
	
	/**
	 * Returns the penalty score which can be used for picking the best mask pattern. (The lower the penalty score the better.)
	 * @return the penalty score.
	 */
	public int getPenalty() {
		int penalty = 0;
		final int moduleSize = getModuleSize();
		// #1
		Color last = Color.BLUE;
		int count = 0;
		for(int y = 0; y<image.getHeight(); y += moduleSize) {
			for(int x = 0; x<image.getWidth(); x += moduleSize) {
				Color c = Color.decode(image.getRGB(x, y) + "");
				if(c.equals(last)) count++;
				else count = 0;
				if(count == 4) penalty += 3;
				else if(count > 4) penalty++;
				last = c;
			}
			last = Color.BLUE;
			count = 0;
		}
		for(int x = 0; x<image.getWidth(); x += moduleSize) {
			for(int y = 0; y<image.getHeight(); y += moduleSize) {
				Color c = Color.decode(image.getRGB(x, y) + "");
				if(c.equals(last)) count++;
				else count = 0;
				if(count == 4) penalty += 3;
				else if(count > 4) penalty++;
				last = c;
			}
			last = Color.BLUE;
			count = 0;
		}
		// #2
		for(int y = 0; y<image.getHeight()-moduleSize; y += moduleSize) {
			for(int x = 0; x<image.getWidth()-moduleSize; x += moduleSize) {
				int a = image.getRGB(x, y),
				b = image.getRGB(x+1, y),
				c = image.getRGB(x, y+1),
				d = image.getRGB(x+1, y+1);
				if(a == b && b == c && c == d) penalty += 3;
			}
		}
		// #3
		for(int y = 0; y<image.getHeight(); y += moduleSize) {
			String row = "";
			for(int x = 0; x<image.getWidth(); x += moduleSize) {
				row += Color.decode(image.getRGB(x, y) + "").equals(onColor) ? 1 : 0;
			}
			while(row.contains("10111010000")) {
				row = row.replaceFirst("10111010000", "");
				penalty += 40;
			}
			while(row.contains("00001011101")) {
				row = row.replaceFirst("00001011101", "");
				penalty += 40;
			}
		}
		for(int x = 0; x<image.getWidth(); x += moduleSize) {
			String column = "";
			for(int y = 0; y<image.getHeight(); y += moduleSize) {
				column += Color.decode(image.getRGB(x, y) + "").equals(onColor) ? 1 : 0;
			}
			while(column.contains("10111010000")) {
				column = column.replaceFirst("10111010000", "");
				penalty += 40;
			}
			while(column.contains("00001011101")) {
				column = column.replaceFirst("00001011101", "");
				penalty += 40;
			}
		}
		// #4
		count = 0;
		for(int y = 0; y<image.getHeight(); y += moduleSize) {
			for(int x = 0; x<image.getWidth(); x += moduleSize) {
				if(Color.decode(image.getRGB(x, y) + "").equals(onColor)) count++;
			}
		}
		int p = (int) Math.round(count / Math.pow(image.getWidth()/moduleSize, 2) * 100),
		a = Math.abs(((int) Math.ceil(p / 5d) * 5) - 50) * 2,
		b = Math.abs(((int) Math.floor(p / 5d) * 5) - 50) * 2;
		penalty += Math.min(a, b);
		return penalty;
	}

}
