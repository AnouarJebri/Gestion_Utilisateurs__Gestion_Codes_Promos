package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.CodePromosService;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class AdminI {
    //ObservableList<Object> list = FXCollections.observableArrayList();
    @FXML
    private MenuButton Utilisateurs_Services;
    /*@FXML
    private MenuItem Liste_U;*/
    @FXML
    private MenuButton CodePromos_services;
   /* @FXML
    private MenuItem RechercherU;
    @FXML
    private MenuItem modifierU;
*/
    @FXML
    private Button logoutB;
    @FXML
    void logout(ActionEvent event) {
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = utilisateurService.getUtilisateurByID(Login.getID());
            utilisateur.setEnLigne(false);
            utilisateurService.updateUser(utilisateur);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/login.fxml"));
        try {
            logoutB.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void Liste_Utilisateurs(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Liste_Utilisateurs.fxml"));
        try {
            Utilisateurs_Services.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void RechercherUtilisateur(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/RechercherU_I.fxml"));
        try {
            Utilisateurs_Services.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void modifier_Utilisateur(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Modifier_Utilisateur.fxml"));
        try {
            Utilisateurs_Services.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void GenerateCodePromos(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/GenerateCodePromosI.fxml"));
        try {
            CodePromos_services.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ListeCodePromos(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/ListCodePromos_I.fxml"));
        try {
            CodePromos_services.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void RechercherCodesPromos(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/RechercherCodePromosI.fxml"));
        try {
            CodePromos_services.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void RefreshCodesPromos(ActionEvent event) {
        CodePromosService codePromosService = new CodePromosService();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Vous allez supprimer des Codes Promos de date expiré Continuer l'opération ?");
        alert.showAndWait().ifPresent(response->{
            if(response == ButtonType.OK){
                try {
                    if(!codePromosService.TakeCodePromos()){
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Already fresh");
                        alert1.setContentText("Rien à supprimer");
                        alert1.showAndWait();
                    }
                    else {
                        codePromosService.supprimerCodePromos();
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Success");
                        alert1.setContentText("Les codes promos expirés ont été supprimé avec succés");
                        alert1.showAndWait();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Abandonné");
                alert1.setContentText("Opération annulé");
                alert1.showAndWait();
            }
        });
    }
    @FXML
    void ModiferCodePromos(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Modifier_CodePromosI.fxml"));
        try {
            CodePromos_services.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void SupprimerUtilisateur(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Supprimer_UtilisateurI.fxml"));
        try {
            Utilisateurs_Services.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*@FXML
    void modifier_soldes(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/ModifierSolde.fxml"));
        try {
            Utilisateurs_Services.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}