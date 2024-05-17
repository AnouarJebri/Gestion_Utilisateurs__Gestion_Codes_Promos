package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class Liste_Utilisateurs {
    @FXML
    private Button back;
    @FXML
    private ListView<Utilisateur> listU;
    @FXML
    public void initialize(){
        UtilisateurService utilisateurService = new UtilisateurService();
        try {
            listU.getItems().addAll(utilisateurService.recupererU());
            //listU.getItems().addAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void goBack(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            back.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/Admin_I.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
