package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class SupprimerUtilisateurI {
    @FXML
    private TextField SupprimerIdTf;

    @FXML
    private Button back;

    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            back.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/Admin_I.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void SupprimerFromID(ActionEvent event) {
        UtilisateurService utilisateurService = new UtilisateurService();
        if (SupprimerIdTf.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INPUT ERROR");
            alert.setContentText("Veuillez remplir le champ svp");
            alert.showAndWait();
        } else if (!SupprimerIdTf.getText().matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INPUT ERROR");
            alert.setContentText("ID invalide remplir seulement des chiffres");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Verification");
            alert.setContentText("Etes vous sures de vouloir supprimer cet utilisateur ?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    utilisateurService.deleteUser(Integer.parseInt(SupprimerIdTf.getText()));
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Success");
                    alert1.setContentText("Utilisateur supprimé avec succés!");
                    alert1.showAndWait();
                }
                else {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Failed");
                    alert1.setContentText("Operation abandonnée");
                    alert1.showAndWait();
                }
            });
        }
    }
}

