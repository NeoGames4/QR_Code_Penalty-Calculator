package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * This class can be used to apply a mask pattern on a QR Code.
 * @author Mika Thein
 * @version 1.0
 * @see #CodeMasking(BufferedImage)
 * @see #applyMaskPattern(int)
 */
public class CodeMasking {
	
	/**
	 * The QR Code.
	 */
	public final BufferedImage image;
	
	/**
	 * A class for applying mask patterns on a QR code.
	 * @param image the QR code.
	 * @see #applyMaskPattern(int)
	 */
	public CodeMasking(BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * Applies a mask pattern.<br>
	 * <b>Note: This does not affect any modules of the format information. Those modules need to be modified manually.</b><p>
	 * <b>Patterns</b>
	 <table>
<thead>
  <tr>
    <th>Index</th>
    <th>Pattern</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>0</td>
    <td>(x+y) % 2 == 0</td>
  </tr>
  <tr>
    <td>1</td>
    <td>y % 2 == 0</td>
  </tr>
  <tr>
    <td>2</td>
    <td>x % 3 == 0</td>
  </tr>
  <tr>
    <td>3</td>
    <td>(x+y) % 3 == 0</td>
  </tr>
  <tr>
    <td>4</td>
    <td>(floor(y/2) + floor(x/3)) % 2 == 0</td>
  </tr>
  <tr>
    <td>5</td>
    <td>((x * y) % 2) + ((x * y) % 3) == 0</td>
  </tr>
  <tr>
    <td>6</td>
    <td>(((x * y) % 2) + ((x * y) % 3)) % 2 == 0</td>
  </tr>
  <tr>
    <td>7</td>
    <td>(((x + y) % 2) + ((x * y) % 3)) % 2 == 0</td>
  </tr>
</tbody>
</table>
	 * @param index the mask pattern's index from 0 to 7.
	 * @return the QR code with the specified mask pattern.
	 */
	public BufferedImage applyMaskPattern(int index) {
		if(index < 0 || index > 7) throw new RuntimeException("The mask pattern with index " + index + " does not exist.");
		final int version = new Interpreter(image).getVersion();
		if(version > 6) throw new RuntimeException("Currently, only QR codes of version 1 to 6 are being supported.");
		final int moduleSize = new Interpreter(image).getModuleSize();
		final int size = image.getWidth()/moduleSize;
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		for(int y = 0; y<image.getHeight(); y += moduleSize) {
			for(int x = 0; x<image.getWidth(); x += moduleSize) {
				Color c = Color.decode(image.getRGB(x, y) + "");
				int column = x/moduleSize;
				int row = y/moduleSize;
				if((row > 8 || column > 8) && (row < size-8 || column > 8) && (row > 8 || column < size-8)) {
					switch(index) {
						case 0: if((row+column) % 2 == 0) c = inverseColor(c);
							break;
						case 1: if(row % 2 == 0) c = inverseColor(c);
							break;
						case 2: if(column % 3 == 0) c = Color.RED;
							break;
						case 3: if((row+column) % 3 == 0) c = inverseColor(c);
							break;
						case 4: if((Math.floor(row/2) + Math.floor(column/3)) % 2 == 0) c = inverseColor(c);
							break;
						case 5: if(((row * column) % 2) + ((row * column) % 3) == 0) c = inverseColor(c);
							break;
						case 6: if((((row * column) % 2) + ((row * column) % 3)) % 2 == 0) c = inverseColor(c);
							break;
						case 7: if((((row + column) % 2) + ((row * column) % 3)) % 2 == 0) c = inverseColor(c);
							break;
					}
				}
				g2.setColor(c);
				g2.fillRect(x, y, moduleSize, moduleSize);
			}
		}
		// Recreation of the timing pattern
		for(int i = moduleSize * 7; i<image.getWidth()-moduleSize * 7; i += moduleSize) {
			g2.setColor((i/moduleSize) % 2 == 0 ? Interpreter.onColor : Interpreter.offColor);
			g2.fillRect(i, 6 * moduleSize, moduleSize, moduleSize);
			g2.fillRect(6 * moduleSize, i, moduleSize, moduleSize);
		}
		// Recreation of the alignment pattern
		//int amount = version == 1 ? 0 : (int) Math.pow(Math.floor(version/7f) + 2, 2) - 3;
		for(int y = image.getHeight() - moduleSize * 7; y>moduleSize * 7 && version > 1; y -= moduleSize * 18) {
			for(int x = image.getWidth() - moduleSize * 7; x>moduleSize * 7; x -= moduleSize * 18) {
				g2.setColor(Interpreter.onColor);
				g2.fillRect(x - 2 * moduleSize, y - 2 * moduleSize, 5 * moduleSize, 5 * moduleSize);
				g2.setColor(Interpreter.offColor);
				g2.fillRect(x - moduleSize, y - moduleSize, 3 * moduleSize, 3 * moduleSize);
				g2.setColor(Interpreter.onColor);
				g2.fillRect(x, y, moduleSize, moduleSize);
			}
		}
		g2.dispose();
		return img;
	}
	
	private Color inverseColor(Color c) {
		return c.equals(Interpreter.onColor) ? Interpreter.offColor : Interpreter.onColor;
	}

}
