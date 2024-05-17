package tn.esprit.e_learning.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import tn.esprit.e_learning.models.CodePromos;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.CodePromosService;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GenerateCodePromosI {

    @FXML
    private DatePicker selectedDate;

    @FXML
    private ComboBox<Utilisateur> utilisateursComboBox;

    @FXML
    private Button back;
    private ObservableList<Utilisateur> utilisateursList;
    @FXML
    void initialize(){
        utilisateursList = FXCollections.observableArrayList();
        UtilisateurService utilisateurService = new UtilisateurService();
        try {
            utilisateursList.addAll(utilisateurService.recupererU());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(utilisateursList);
        utilisateursComboBox.setItems(utilisateursList);
        utilisateursComboBox.setCellFactory(param -> new ListCell<Utilisateur>() {

            public void updateItem(Utilisateur utilisateur, boolean empty) {
                super.updateItem(utilisateur, empty);

                if (empty || utilisateur == null) {
                    setText(null);
                } else {
                    setText(utilisateur.getEmail()); // Adjust this to the property you want to display
                }
            }
        });
        utilisateursComboBox.setPromptText("Select a user");
    }

    @FXML
    void GenrerCodePromos(ActionEvent event) throws SQLException {
        UtilisateurService utilisateurService = new UtilisateurService();
        CodePromosService codePromosService = new CodePromosService();
        CodePromos codePromos = new CodePromos();
        if(utilisateursComboBox.getSelectionModel().isEmpty()||selectedDate.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir les champs svp");
            alert.showAndWait();
        } else {
            codePromos.setUtilisateur(utilisateurService.getUtilisateurByEmail(utilisateursComboBox.getValue().getEmail()));

            codePromos.setDateExpiration(Date.valueOf(selectedDate.getValue()));
            String cd = codePromos.generateRandomCode();
            while (codePromosService.verifier(cd)) {
                cd = codePromos.generateRandomCode();
                try {
                    codePromosService.verifier(cd);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            codePromos.setCode(cd);

            try {
                codePromosService.ajouterCodePromos(codePromos);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Code promos ajouté avec succés");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
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