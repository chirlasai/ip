package athena.main;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Athena athena;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/Images/DaUser.png"));
    private final Image athenaImage = new Image(this.getClass().getResourceAsStream("/Images/DaAthena.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setAthena(Athena a) {
        assert a != null : "The Athena instance passed to MainWindow should not be null";
        athena = a;

        dialogContainer.getChildren().addAll(
                DialogBox.getAthenaDialog(athena.getWelcomeMessage(), athenaImage)
        );

    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing
     * Athena's reply and then appends them to the dialog container.
     * Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        assert athena != null : "Athena logic should be initialized before handling user input";
        String response = athena.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getAthenaDialog(response, athenaImage)
        );
        userInput.clear();

        if (input.equalsIgnoreCase("bye") || input.equalsIgnoreCase("Bye") ) {
            // Wait 1.5 seconds so the user can read the "Bye" message
            PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
