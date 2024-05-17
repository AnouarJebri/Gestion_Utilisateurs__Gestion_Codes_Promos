package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.esprit.e_learning.models.CodePromos;
import tn.esprit.e_learning.services.CodePromosService;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class RechercherCodePromosI {

    @FXML
    private Button back;

    @FXML
    private TextField BarreRecherche;

    @FXML
    private ListView<CodePromos> listCodeR;

    @FXML
    void ChercherCodePromos(ActionEvent event) {
        CodePromosService codePromosService = new CodePromosService();
        try {
            String searchQuery = BarreRecherche.getText();
            if (!searchQuery.isEmpty()) {
                listCodeR.getItems().clear(); // Clear the ListView before adding new items
                listCodeR.getItems().addAll(codePromosService.RecupererCr(searchQuery));
            } else {
                listCodeR.getItems().clear(); // Clear the ListView when search bar is empty
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