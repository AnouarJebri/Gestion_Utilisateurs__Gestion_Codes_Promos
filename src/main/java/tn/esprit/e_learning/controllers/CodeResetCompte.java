package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Check;
import tn.esprit.e_learning.models.Mail;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.util.Random;

public class CodeResetCompte {
    @FXML
    private TextField resetCodeTextField;
    private String generatedCode;
    public String CodeResetCompte(String generatedCode) {
        return this.generatedCode = generatedCode;
    }
    public String generateCode() {
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            codeBuilder.append(allowedCharacters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
    @FXML
    void initialize() {
        String userEmail = Login.getEmailTf();
        Mail mail = new Mail();
        generatedCode = generateCode();
        mail.sendRecoveryCode(userEmail,generatedCode);
    }
    @FXML
    void Goback(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/login.fxml"));
        try {
            resetCodeTextField.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void checkResetCode(ActionEvent event) {
        String enteredCode = resetCodeTextField.getText();
        if (enteredCode.equals(generatedCode)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Matches");
            alert.setContentText("Code matches");
            alert.showAndWait();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/ResetPassword.fxml"));
            try {
                resetCodeTextField.getScene().setRoot(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Code doesn't match");
            alert.showAndWait();
        }

    }
}
