package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import tn.esprit.e_learning.models.Mail;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateMdpConnected {
    @FXML
    private Button Back;

    @FXML
    private PasswordField passwordTf;

    @FXML
    void ChangerMdp(ActionEvent event) {
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = utilisateurService.getUtilisateurByID(Login.getID());
            utilisateur.setPassword(utilisateur.hashPassword(passwordTf.getText()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("You are about to change you password , continue the operation ?");
        Utilisateur finalUtilisateur = utilisateur;
        alert.showAndWait().ifPresent(response->{
            if(response == ButtonType.OK){
                try {
                    utilisateurService.updateUser(finalUtilisateur);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setContentText("Password updated successfully !!");
                alert1.showAndWait();
                Mail mail = new Mail();
                mail.sendPasswordUpdatedNotif(finalUtilisateur.getEmail());
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Utilisateur_I.fxml"));
                try {
                    Back.getScene().setRoot(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Utilisateur_I.fxml"));
        try {
            Back.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}