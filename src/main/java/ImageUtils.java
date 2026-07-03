import javax.swing.*;

public class ImageUtils {

    /**
     * Loads an image icon from a file path derived from the given name.
     * Returns null if the image file does not exist or cannot be loaded.
     */
    public static ImageIcon loadImage(String name) {
        ImageIcon icon = new ImageIcon(name.toLowerCase() + ".png");
        if (icon.getIconWidth() == -1) {
            return null;
        }
        return icon;
    }

    /**
     * Updates a JLabel to display the image for the given name,
     * or shows an error message if the image is not found.
     */
    public static void displayImage(JLabel label, String name) {
        ImageIcon icon = loadImage(name);
        if (icon == null) {
            label.setText("Image not found: " + name.toLowerCase() + ".png");
            label.setIcon(null);
        } else {
            label.setText("");
            label.setIcon(icon);
        }
    }
}
