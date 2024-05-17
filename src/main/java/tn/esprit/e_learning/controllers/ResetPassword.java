package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import tn.esprit.e_learning.models.Mail;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class ResetPassword {

    @FXML
    private Button back;

    @FXML
    private PasswordField ResetmdpTf;

    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            back.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/login.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void UpdateMdp(ActionEvent event) {
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur = utilisateurService.getUtilisateurByEmail(Login.getEmailTf());
        utilisateur.setPassword(utilisateur.hashPassword(ResetmdpTf.getText()));
        try {
            utilisateurService.updateUser(utilisateur);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("Password successfully updated");
        alert.showAndWait();
        Mail mail = new Mail();
        mail.sendPasswordUpdatedNotif(Login.getEmailTf());
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            back.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/login.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
