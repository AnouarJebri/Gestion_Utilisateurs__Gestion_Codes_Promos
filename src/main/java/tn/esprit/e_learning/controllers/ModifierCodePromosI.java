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
import javafx.scene.control.ComboBox;
;

public class ModifierCodePromosI {
    @FXML
    private DatePicker DateModif;

    @FXML
    private ComboBox<Utilisateur> utilisateursComboBox;

    @FXML
    private Button back;
    @FXML
    private ComboBox<CodePromos> codePromosComboBox;
    private ObservableList<Utilisateur> utilisateursList;
    private ObservableList<CodePromos> codePromosList;

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
    void initialize(){
        utilisateursList = FXCollections.observableArrayList();
        UtilisateurService utilisateurService = new UtilisateurService();
        try {
            utilisateursList.addAll(utilisateurService.recupererU());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        codePromosList = FXCollections.observableArrayList();
        CodePromosService codePromosService = new CodePromosService();
        try {
            codePromosList.addAll(codePromosService.recupererC());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        codePromosComboBox.setItems(codePromosList);
        codePromosComboBox.setCellFactory(param -> new ListCell<CodePromos>() {
            @Override
            protected void updateItem(CodePromos codePromos, boolean empty) {
                super.updateItem(codePromos, empty);

                if (empty || codePromos == null) {
                    setText(null);
                } else {
                    setText(codePromos.getCode()); // Adjust this to the property you want to display
                }
            }
        });
        codePromosComboBox.setPromptText("Select a code promo");
    }
    @FXML
    void ModifierCodePromosI(ActionEvent event) throws SQLException {
        CodePromosService codePromosService = new CodePromosService();
        CodePromos codePromos = new CodePromos();
        if(utilisateursComboBox.getSelectionModel().isEmpty()||DateModif.getValue()==null||utilisateursComboBox.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INPUT ERROR");
            alert.setContentText("Veuiller remplir tous les champs svp");
        } else {
            UtilisateurService utilisateurService = new UtilisateurService();
            codePromos.setCode(codePromosComboBox.getEditor().getText());
            codePromos.setDateExpiration(Date.valueOf(DateModif.getValue()));
            codePromos.setUtilisateur(utilisateurService.getUtilisateurByEmail(utilisateursComboBox.getValue().getEmail()));
            try {
                codePromosService.modifierCodePromos(codePromos);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Code Promos modifié avec succés");
                alert.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
