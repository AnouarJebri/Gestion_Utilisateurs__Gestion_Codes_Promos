package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModifierUtilisateur {
    @FXML
    private TextField AdminAddTf;

    @FXML
    private Button back;

    @FXML
    void AjouterNewAdmin(ActionEvent event) throws SQLException {
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = new Utilisateur();
        if(AdminAddTf.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty input");
            alert.setContentText("Remplir le champ svp");
            alert.showAndWait();
        }
        else if(!AdminAddTf.getText().matches("^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$")){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("INPUT ERROR");
            alert2.setContentText("Incorrect Email Format");
            alert2.showAndWait();
        } else if (utilisateurService.getUtilisateurByEmail(AdminAddTf.getText())==null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("USER NOT FOUND");
            alert.setContentText("Cet Utilisateur n'existe pas");
            alert.showAndWait();
        } else {
            utilisateur = utilisateurService.getUtilisateurByEmail(AdminAddTf.getText());
            if (!utilisateur.getRoles().equals("ROLE_ADMIN")) {
                //ArrayList<String> roles = new ArrayList<>();
                String roles = "ROLE_ADMIN";
                utilisateur.setRoles(roles);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Vous etes sure de vouloir ajouter un autre admin ?");
                Utilisateur finalUtilisateur = utilisateur;
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            utilisateurService.updateUser(finalUtilisateur);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Success");
                        alert1.setContentText("New Admin added successfully!!");
                        alert1.showAndWait();
                        // Perform action when user clicks OK
                    } else {

                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Failed");
                        alert1.setContentText("Opération abandonnée");
                        alert1.showAndWait();
                        // Perform action when user clicks Cancel or closes the dialog
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Cet utilisateur est déjà un admnistrateur");
                alert.showAndWait();
            }
        }
    }
    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            back.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/Admin_I.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}