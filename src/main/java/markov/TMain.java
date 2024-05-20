package markov;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class TMain extends Application {

    private TMarkov markov;
    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Log File");

        Button loadButton = new Button("Load Log File");
        loadButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    markov = new TMarkov("", 2);
                    markov.loadTextFromFile(file.getAbsolutePath());
                    textArea.appendText("Loaded file: " + file.getName() + "\n");
                } catch (IOException ex) {
                    textArea.appendText("Error reading file: " + ex.getMessage() + "\n");
                }
            }
        });

        TextField kEventField = new TextField();
        kEventField.setPromptText("Enter initial k-event");

        TextField lengthField = new TextField();
        lengthField.setPromptText("Enter length of sequence");

        Button generateButton = new Button("Generate Sequence");
        generateButton.setOnAction(e -> {
            if (markov != null) {
                String kevent = kEventField.getText();
                if (lengthField.getText().isBlank() || kEventField.getText().isBlank()) {
                    textArea.appendText("Initial k-event or length of sequence cannot be empty!\n");
                } else {
                    try {
                        int length = Integer.parseInt(lengthField.getText());
                        String result = markov.gen(kevent, length);
                        textArea.appendText("Generated sequence: " + result + "\n");
                    } catch (NumberFormatException ex) {
                        textArea.appendText("Please enter a valid integer for the length.\n");
                    }
                }
            } else {
                textArea.appendText("Please load a log file first.\n");
            }
        });

        textArea = new TextArea();
        textArea.setEditable(false);

        VBox vbox = new VBox(10, loadButton, kEventField, lengthField, generateButton, textArea);
        vbox.getStyleClass().add("main-container");
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
        primaryStage.getIcons().add(icon);

        primaryStage.setTitle("Markov Chain Text Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        String filePath = "src/main/resources/markov/logfile.txt";
        TMarkov markov = new TMarkov("", 2);

        try {
            markov.loadTextFromFile(filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        System.out.println("Frequency of 'xy': " + markov.freq("xy"));
        System.out.println("Frequency of 'xy' followed by 'z': " + markov.freq("xy", "z"));
        System.out.println("Random event following 'xy': " + markov.rand("xy"));
        System.out.println("Generated sequence starting with 'xy':" + markov.gen("xy", 20));

        launch(args);
    }
}

