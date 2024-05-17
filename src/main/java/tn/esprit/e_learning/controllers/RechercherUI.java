package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class RechercherUI {

    @FXML
    private TextField BarreRecherche;

    @FXML
    private Button back;

    @FXML
    private ListView<Utilisateur> listUS;

    @FXML
    void RechercherUser(ActionEvent event) {
        UtilisateurService utilisateurService = new UtilisateurService();
        try {
            String searchQuery = BarreRecherche.getText();
            if (!searchQuery.isEmpty()) {
                listUS.getItems().clear(); // Clear the ListView before adding new items
                listUS.getItems().addAll(utilisateurService.RecupererUr(searchQuery));
            } else {
                listUS.getItems().clear(); // Clear the ListView when search bar is empty
            }
            BarreRecherche.setText("");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Admin_I.fxml"));
        try {
            back.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}