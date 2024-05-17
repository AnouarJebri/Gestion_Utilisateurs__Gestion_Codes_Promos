package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Alert;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.util.Random;

public class CaptchaI {
    @FXML
    private Pane captchaLabel;

    @FXML
    private TextField captchaTf;

    private String captchaText;
    private String capthaGotText;
    @FXML
    void Verify(ActionEvent event) {
        String userInput = captchaTf.getText();
        if (userInput.equals(capthaGotText)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Correct");
            alert.setContentText("Correct input !!");
            alert.showAndWait();
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                captchaTf.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/login.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect");
            alert.setContentText("Incorrect input check again !!");
            alert.showAndWait();
            System.out.println(capthaGotText);
        }
    }
    private void setCaptcha(){
        captchaLabel.getChildren().clear();

        double xPos = 0;

        String[] fonts = {"cursive", "sans-serif", "serif", "monospace"};

        for (char charValue : captchaText.toCharArray()) {
            Text text = new Text(String.valueOf(charValue));
            int rotate = -20 + new Random().nextInt(30);
            int padding = new Random().nextInt(10) + 5;

            String randomColor = String.format("#%06X", new Random().nextInt(0xFFFFFF));

            // Randomly select a font from the 'fonts' array
            String randomFont = fonts[new Random().nextInt(fonts.length)];

            text.setStyle("-fx-rotate: " + rotate + "; -fx-font-family: '" + randomFont + "'; -fx-font-size: 26; -fx-fill: " + randomColor + ";");

            text.setTranslateX(xPos);
            text.setTranslateY(0);

            captchaLabel.getChildren().add(text);

            xPos += text.getBoundsInLocal().getWidth() + padding;
        }
    }
    @FXML
    void initialize() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) { // Generating 6 characters CAPTCHA
            char c = (char) (random.nextInt(26) + 'A'); // Uppercase letters
            sb.append(c);
        }
        capthaGotText = sb.toString();
        captchaText = sb.toString().trim();
        System.out.println(capthaGotText);
        System.out.println(captchaText);

        setCaptcha();
    }
}
