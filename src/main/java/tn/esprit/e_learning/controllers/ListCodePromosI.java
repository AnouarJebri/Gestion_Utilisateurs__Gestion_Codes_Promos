package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import tn.esprit.e_learning.models.CodePromos;
import tn.esprit.e_learning.services.CodePromosService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class ListCodePromosI {
    @FXML
    private Button back;

    @FXML
    private ListView<CodePromos> listC;

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
    void initialize() {
        CodePromosService codePromosService = new CodePromosService();
        try {
            listC.getItems().addAll(codePromosService.recupererC());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}