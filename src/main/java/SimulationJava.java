import javax.swing.*;
import java.awt.*;

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

        ImageIcon icon = new ImageIcon(pet.toLowerCase() + ".png");

        // Check if image exists
        if (icon.getIconWidth() == -1) {
            imageLabel.setText("Image not found: " + pet.toLowerCase() + ".png");
            imageLabel.setIcon(null);
        } else {
            imageLabel.setText("");
            imageLabel.setIcon(icon);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimulationJava());
    }
}