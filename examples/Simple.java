
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Simple {
	
	public static void main(String[] args) {
		try {
			BufferedImage image = ImageIO.read(new File("./qr_code.png"));
			Interpreter interpreter = new Interpreter(image);
			System.out.println("Module size: " + interpreter.getModuleSize() + ", version: " + interpreter.getVersion());
			System.out.println("Penalty: " + interpreter.getPenalty());
			Image maskedCode = new CodeMasking(image).applyMaskPattern(0); // Applies mask 0 to the QR Code.
		} catch(Exception e) { e.printStackTrace(); }
	}

}
