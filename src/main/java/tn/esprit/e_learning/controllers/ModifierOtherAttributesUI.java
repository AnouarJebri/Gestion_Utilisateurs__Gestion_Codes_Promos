package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import tn.esprit.e_learning.models.Mail;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierOtherAttributesUI {
    @FXML
    private RadioButton FemaleButton;

    @FXML
    private RadioButton MaleButton;

    @FXML
    private TextField NomTf;

    @FXML
    private TextField PrenomTf;

    @FXML
    private TextField ProfessionTf;

    @FXML
    private ToggleGroup gender;

    @FXML
    private Button back;

    @FXML
    void ModifierCompte(ActionEvent event) {
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = utilisateurService.getUtilisateurByID(Login.getID());
            System.out.println("mail user = "+Login.getEmailTf());
            System.out.println("id user = "+Login.getID());
            System.out.println("User to change :"+utilisateur);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Vérification");
        alert.setContentText("Vous allez changer des attributs de votre compte , continuer l'opération ?");
        if(!NomTf.getText().isEmpty()){
            utilisateur.setNom(NomTf.getText());
        }
        if (!PrenomTf.getText().isEmpty()) {
            utilisateur.setPrenom(PrenomTf.getText());
        }
        if (!ProfessionTf.getText().isEmpty()) {
            utilisateur.setProfession(ProfessionTf.getText());
        }
        RadioButton selectedRadioButton = (RadioButton) gender.getSelectedToggle();
        boolean maleb = utilisateur.isGender();
        if(selectedRadioButton != null)
        {
            if (selectedRadioButton.getId().equals("MaleButton")){
                maleb = true;
            }
            else if (selectedRadioButton.getId().equals("FemaleButton")){
                maleb = false;
            }
        }
        utilisateur.setGender(maleb);
        Utilisateur finalUtilisateur = utilisateur;
        alert.showAndWait().ifPresent(response->{
            if(response == ButtonType.OK){
                try {
                    utilisateurService.updateUser(finalUtilisateur);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Mail mail = new Mail();
                mail.UpdatedAccountNotif(finalUtilisateur.getEmail());
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setContentText("Success");
                alert1.setContentText("Account updated successfully !!");
                alert1.showAndWait();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Utilisateur_I.fxml"));
                try {
                    NomTf.getScene().setRoot(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Utilisateur_I.fxml"));
                try {
                    NomTf.getScene().setRoot(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            back.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/Utilisateur_I.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}