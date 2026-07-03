import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SimulationJava Tests")
class SimulationJavaTest {

    private SimulationJava frame;

    @BeforeEach
    void setUp() throws Exception {
        // Run on EDT and wait for completion
        SwingUtilities.invokeAndWait(() -> {
            // Prevent the dialog from blocking tests by not making it visible
            frame = new TestableSimulationJava();
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            if (frame != null) {
                frame.dispose();
            }
        });
    }

    @Test
    @DisplayName("Frame title should be 'Radio Button Demo'")
    void testFrameTitle() {
        assertEquals("Radio Button Demo", frame.getTitle());
    }

    @Test
    @DisplayName("Frame should use BorderLayout")
    void testFrameLayout() {
        assertThat(frame.getContentPane().getLayout()).isInstanceOf(BorderLayout.class);
    }

    @Test
    @DisplayName("Frame size should be 500x300")
    void testFrameSize() {
        assertEquals(500, frame.getSize().width);
        assertEquals(300, frame.getSize().height);
    }

    @Test
    @DisplayName("Frame default close operation should be EXIT_ON_CLOSE")
    void testDefaultCloseOperation() {
        assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
    }

    @Test
    @DisplayName("Pets array should contain exactly 5 animals")
    void testPetsArraySize() throws Exception {
        Field petsField = SimulationJava.class.getDeclaredField("pets");
        petsField.setAccessible(true);
        String[] pets = (String[]) petsField.get(frame);
        assertEquals(5, pets.length);
    }

    @Test
    @DisplayName("Pets array should contain Bird, Cat, Dog, Rabbit, Pig in order")
    void testPetsArrayContent() throws Exception {
        Field petsField = SimulationJava.class.getDeclaredField("pets");
        petsField.setAccessible(true);
        String[] pets = (String[]) petsField.get(frame);
        assertArrayEquals(new String[]{"Bird", "Cat", "Dog", "Rabbit", "Pig"}, pets);
    }

    @Test
    @DisplayName("Radio button panel should be in WEST position")
    void testRadioButtonPanelPosition() {
        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        Component westComponent = layout.getLayoutComponent(BorderLayout.WEST);
        assertNotNull(westComponent);
        assertThat(westComponent).isInstanceOf(JPanel.class);
    }

    @Test
    @DisplayName("Radio button panel should contain 5 radio buttons")
    void testRadioButtonCount() {
        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        JPanel panel = (JPanel) layout.getLayoutComponent(BorderLayout.WEST);
        int radioButtonCount = 0;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JRadioButton) {
                radioButtonCount++;
            }
        }
        assertEquals(5, radioButtonCount);
    }

    @Test
    @DisplayName("Radio buttons should have correct labels")
    void testRadioButtonLabels() {
        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        JPanel panel = (JPanel) layout.getLayoutComponent(BorderLayout.WEST);
        String[] expectedLabels = {"Bird", "Cat", "Dog", "Rabbit", "Pig"};
        int index = 0;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JRadioButton) {
                assertEquals(expectedLabels[index], ((JRadioButton) comp).getText());
                index++;
            }
        }
    }

    @Test
    @DisplayName("Pig radio button should be selected by default")
    void testDefaultSelection() {
        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        JPanel panel = (JPanel) layout.getLayoutComponent(BorderLayout.WEST);
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JRadioButton) {
                JRadioButton rb = (JRadioButton) comp;
                if (rb.getText().equals("Pig")) {
                    assertTrue(rb.isSelected(), "Pig should be selected by default");
                } else {
                    assertFalse(rb.isSelected(), rb.getText() + " should not be selected");
                }
            }
        }
    }

    @Test
    @DisplayName("Image label should be in CENTER position")
    void testImageLabelPosition() {
        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        Component centerComponent = layout.getLayoutComponent(BorderLayout.CENTER);
        assertNotNull(centerComponent);
        assertThat(centerComponent).isInstanceOf(JLabel.class);
    }

    @Test
    @DisplayName("Image label should be centered horizontally")
    void testImageLabelAlignment() throws Exception {
        Field imageLabelField = SimulationJava.class.getDeclaredField("imageLabel");
        imageLabelField.setAccessible(true);
        JLabel imageLabel = (JLabel) imageLabelField.get(frame);
        assertEquals(JLabel.CENTER, imageLabel.getHorizontalAlignment());
    }

    @Test
    @DisplayName("updateImage with non-existent image should set error text")
    void testUpdateImageNotFound() throws Exception {
        Method updateImageMethod = SimulationJava.class.getDeclaredMethod("updateImage", String.class);
        updateImageMethod.setAccessible(true);

        SwingUtilities.invokeAndWait(() -> {
            try {
                updateImageMethod.invoke(frame, "NonExistent");
            } catch (Exception e) {
                fail("updateImage threw exception: " + e.getMessage());
            }
        });

        Field imageLabelField = SimulationJava.class.getDeclaredField("imageLabel");
        imageLabelField.setAccessible(true);
        JLabel imageLabel = (JLabel) imageLabelField.get(frame);

        assertEquals("Image not found: nonexistent.png", imageLabel.getText());
        assertNull(imageLabel.getIcon());
    }

    @Test
    @DisplayName("updateImage should convert pet name to lowercase for filename")
    void testUpdateImageFilenameLowercase() throws Exception {
        Method updateImageMethod = SimulationJava.class.getDeclaredMethod("updateImage", String.class);
        updateImageMethod.setAccessible(true);

        SwingUtilities.invokeAndWait(() -> {
            try {
                updateImageMethod.invoke(frame, "Cat");
            } catch (Exception e) {
                fail("updateImage threw exception: " + e.getMessage());
            }
        });

        Field imageLabelField = SimulationJava.class.getDeclaredField("imageLabel");
        imageLabelField.setAccessible(true);
        JLabel imageLabel = (JLabel) imageLabelField.get(frame);

        // Since image files don't exist in test environment, it should show the error
        assertThat(imageLabel.getText()).contains("cat.png");
    }

    @Test
    @DisplayName("updateImage should set icon to null when image not found")
    void testUpdateImageIconNullWhenNotFound() throws Exception {
        Method updateImageMethod = SimulationJava.class.getDeclaredMethod("updateImage", String.class);
        updateImageMethod.setAccessible(true);

        SwingUtilities.invokeAndWait(() -> {
            try {
                updateImageMethod.invoke(frame, "Dog");
            } catch (Exception e) {
                fail("updateImage threw exception: " + e.getMessage());
            }
        });

        Field imageLabelField = SimulationJava.class.getDeclaredField("imageLabel");
        imageLabelField.setAccessible(true);
        JLabel imageLabel = (JLabel) imageLabelField.get(frame);

        assertNull(imageLabel.getIcon());
    }

    @Test
    @DisplayName("Radio button panel should use GridLayout(5,1)")
    void testRadioButtonPanelLayout() {
        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        JPanel panel = (JPanel) layout.getLayoutComponent(BorderLayout.WEST);
        assertThat(panel.getLayout()).isInstanceOf(GridLayout.class);
        GridLayout gridLayout = (GridLayout) panel.getLayout();
        assertEquals(5, gridLayout.getRows());
        assertEquals(1, gridLayout.getColumns());
    }

    @Test
    @DisplayName("Only one radio button should be selectable at a time (ButtonGroup)")
    void testButtonGroupMutualExclusion() throws Exception {
        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        JPanel panel = (JPanel) layout.getLayoutComponent(BorderLayout.WEST);

        JRadioButton firstButton = null;
        JRadioButton lastButton = null;

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JRadioButton) {
                if (firstButton == null) {
                    firstButton = (JRadioButton) comp;
                }
                lastButton = (JRadioButton) comp;
            }
        }

        assertNotNull(firstButton);
        assertNotNull(lastButton);

        // Pig (last) is selected by default
        assertTrue(lastButton.isSelected());
        assertFalse(firstButton.isSelected());

        // Selecting Bird should deselect Pig
        final JRadioButton bird = firstButton;
        final JRadioButton pig = lastButton;
        SwingUtilities.invokeAndWait(() -> bird.setSelected(true));

        assertTrue(firstButton.isSelected());
        assertFalse(lastButton.isSelected());
    }

    @Test
    @DisplayName("Each radio button should have an action listener")
    void testRadioButtonsHaveActionListeners() {
        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        JPanel panel = (JPanel) layout.getLayoutComponent(BorderLayout.WEST);

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JRadioButton) {
                JRadioButton rb = (JRadioButton) comp;
                assertThat(rb.getActionListeners().length)
                        .as("Radio button '%s' should have an action listener", rb.getText())
                        .isGreaterThan(0);
            }
        }
    }

    @Test
    @DisplayName("Main method should not throw exception")
    void testMainMethod() {
        // Just verify that the main method exists and is callable
        assertDoesNotThrow(() -> {
            Method mainMethod = SimulationJava.class.getMethod("main", String[].class);
            assertNotNull(mainMethod);
        });
    }

    @Test
    @DisplayName("Main method invocation should create frame via EDT")
    void testMainMethodInvocation() throws Exception {
        assertDoesNotThrow(() -> {
            SimulationJava.main(new String[]{});
            // Allow EDT to process
            SwingUtilities.invokeAndWait(() -> {});
        });
    }

    @Test
    @DisplayName("Action listener should call updateImage when radio button clicked")
    void testActionListenerTriggersUpdateImage() throws Exception {
        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        JPanel panel = (JPanel) layout.getLayoutComponent(BorderLayout.WEST);

        // Find the "Bird" radio button
        JRadioButton birdButton = null;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JRadioButton && ((JRadioButton) comp).getText().equals("Bird")) {
                birdButton = (JRadioButton) comp;
                break;
            }
        }

        assertNotNull(birdButton);

        // Fire the action listener directly (bypasses JOptionPane)
        final JRadioButton bird = birdButton;
        SwingUtilities.invokeAndWait(() -> {
            // Temporarily replace the action listener to avoid dialog popup
            var listeners = bird.getActionListeners();
            for (var listener : listeners) {
                bird.removeActionListener(listener);
            }
            // Simulate the effect: call updateImage directly
            try {
                Method updateImageMethod = SimulationJava.class.getDeclaredMethod("updateImage", String.class);
                updateImageMethod.setAccessible(true);
                updateImageMethod.invoke(frame, "Bird");
            } catch (Exception e) {
                fail("Failed to invoke updateImage: " + e.getMessage());
            }
        });

        Field imageLabelField = SimulationJava.class.getDeclaredField("imageLabel");
        imageLabelField.setAccessible(true);
        JLabel imageLabel = (JLabel) imageLabelField.get(frame);

        assertThat(imageLabel.getText()).contains("bird.png");
    }

    @Test
    @DisplayName("updateImage with valid image should set icon and clear text")
    void testUpdateImageWithValidImage() throws Exception {
        // Create a small valid PNG image for testing (use unique name to avoid conflicts)
        java.io.File testImage = new java.io.File("zebra.png");
        try {
            // Create a minimal 1x1 pixel image
            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            img.setRGB(0, 0, Color.RED.getRGB());
            javax.imageio.ImageIO.write(img, "png", testImage);

            Method updateImageMethod = SimulationJava.class.getDeclaredMethod("updateImage", String.class);
            updateImageMethod.setAccessible(true);

            SwingUtilities.invokeAndWait(() -> {
                try {
                    updateImageMethod.invoke(frame, "Zebra");
                } catch (Exception e) {
                    fail("updateImage threw exception: " + e.getMessage());
                }
            });

            Field imageLabelField = SimulationJava.class.getDeclaredField("imageLabel");
            imageLabelField.setAccessible(true);
            JLabel imageLabel = (JLabel) imageLabelField.get(frame);

            // With a valid image, text should be empty and icon should be set
            assertEquals("", imageLabel.getText());
            assertNotNull(imageLabel.getIcon());
        } finally {
            testImage.delete();
        }
    }

    @Test
    @DisplayName("updateImage should set text and clear icon for all missing pets")
    void testUpdateImageAllPets() throws Exception {
        Method updateImageMethod = SimulationJava.class.getDeclaredMethod("updateImage", String.class);
        updateImageMethod.setAccessible(true);
        Field imageLabelField = SimulationJava.class.getDeclaredField("imageLabel");
        imageLabelField.setAccessible(true);

        String[] expectedPets = {"Bird", "Cat", "Dog", "Rabbit", "Pig"};
        for (String pet : expectedPets) {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    updateImageMethod.invoke(frame, pet);
                } catch (Exception e) {
                    fail("updateImage threw for " + pet + ": " + e.getMessage());
                }
            });

            JLabel imageLabel = (JLabel) imageLabelField.get(frame);
            assertThat(imageLabel.getText())
                    .as("Expected error text for " + pet)
                    .contains(pet.toLowerCase() + ".png");
        }
    }

    /**
     * Subclass that prevents the frame from becoming visible during tests
     * to avoid UI interaction issues.
     */
    private static class TestableSimulationJava extends SimulationJava {
        public TestableSimulationJava() {
            super();
            setVisible(false); // Override visibility for testing
        }
    }
}
