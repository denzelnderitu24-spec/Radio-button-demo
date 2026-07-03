import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SimulationJava extends JFrame {

    private JLabel imageLabel;
    private final String[] pets = {"Bird", "Cat", "Dog", "Rabbit", "Pig"};

    public SimulationJava() {

        setTitle("Radio Button Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel for radio buttons
        JPanel panel = new JPanel(new GridLayout(5, 1));

        ButtonGroup group = new ButtonGroup();

        imageLabel = new JLabel("", JLabel.CENTER);

        // Create radio buttons
        for (String pet : pets) {

            JRadioButton button = new JRadioButton(pet);

            button.addActionListener(e -> {
                updateImage(pet);
                JOptionPane.showMessageDialog(this,
                        "You selected: " + pet);
            });

            group.add(button);
            panel.add(button);

            // Default selection
            if (pet.equals("Pig")) {
                button.setSelected(true);
                updateImage("Pig");   // Display Pig image immediately
            }
        }

        add(panel, BorderLayout.WEST);
        add(imageLabel, BorderLayout.CENTER);

        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateImage(String pet) {
        String filename = pet.toLowerCase() + ".png";
        File imageFile = new File(filename);

        if (!imageFile.exists()) {
            String message = "Image not found: " + filename;
            System.err.println(message);
            imageLabel.setText(message);
            imageLabel.setIcon(null);
            return;
        }

        try {
            BufferedImage image = ImageIO.read(imageFile);
            if (image == null) {
                String message = "Unable to read image (unsupported format): " + filename;
                System.err.println(message);
                imageLabel.setText(message);
                imageLabel.setIcon(null);
                return;
            }
            imageLabel.setText("");
            imageLabel.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            String message = "Error loading image: " + filename;
            System.err.println(message + " - " + e.getMessage());
            imageLabel.setText(message);
            imageLabel.setIcon(null);
        }
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Uncaught exception in thread " + thread.getName() + ": " + throwable.getMessage());
            throwable.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "An unexpected error occurred: " + throwable.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        });

        SwingUtilities.invokeLater(() -> {
            try {
                new SimulationJava();
            } catch (Exception e) {
                System.err.println("Failed to initialize application: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Failed to start the application: " + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}