import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class RadioButtonPanelBuilder {

    private final ButtonGroup group = new ButtonGroup();
    private final JPanel panel;
    private final Consumer<String> onSelection;
    private Consumer<String> onDefaultInit;
    private String defaultSelection;

    public RadioButtonPanelBuilder(Consumer<String> onSelection) {
        this.onSelection = onSelection;
        this.panel = new JPanel();
    }

    public RadioButtonPanelBuilder(int rows, int cols, Consumer<String> onSelection) {
        this.onSelection = onSelection;
        this.panel = new JPanel(new GridLayout(rows, cols));
    }

    /**
     * Sets which option should be selected by default.
     * Uses onSelection as the default init callback unless overridden
     * via {@link #withDefaultInit(Consumer)}.
     */
    public RadioButtonPanelBuilder withDefault(String defaultOption) {
        this.defaultSelection = defaultOption;
        return this;
    }

    /**
     * Sets a separate callback for the initial default selection,
     * so it can differ from the interactive selection callback.
     */
    public RadioButtonPanelBuilder withDefaultInit(Consumer<String> onDefaultInit) {
        this.onDefaultInit = onDefaultInit;
        return this;
    }

    /**
     * Builds the panel from the given options, wiring up each radio button
     * to invoke the shared selection callback.
     */
    public JPanel build(String[] options) {
        for (String option : options) {
            JRadioButton button = new JRadioButton(option);
            button.addActionListener(e -> onSelection.accept(option));
            group.add(button);
            panel.add(button);

            if (option.equals(defaultSelection)) {
                button.setSelected(true);
                Consumer<String> initCallback = onDefaultInit != null ? onDefaultInit : onSelection;
                initCallback.accept(option);
            }
        }
        return panel;
    }
}
