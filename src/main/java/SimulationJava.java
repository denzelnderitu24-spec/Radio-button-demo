import javax.swing.*;
import java.awt.*;

public class SimulationJava extends JFrame {

    private JLabel imageLabel;
    private final String[] pets = {"Bird", "Cat", "Dog", "Rabbit", "Pig"};

    public SimulationJava() {

        setTitle("Radio Button Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        imageLabel = new JLabel("", JLabel.CENTER);

        JPanel panel = new RadioButtonPanelBuilder(5, 1, this::handlePetSelection)
                .withDefault("Pig")
                .withDefaultInit(this::updateImage)
                .build(pets);

        add(panel, BorderLayout.WEST);
        add(imageLabel, BorderLayout.CENTER);

        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handlePetSelection(String pet) {
        updateImage(pet);
        JOptionPane.showMessageDialog(this, "You selected: " + pet);
    }

    private void updateImage(String pet) {
        ImageUtils.displayImage(imageLabel, pet);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimulationJava());
    }
}
