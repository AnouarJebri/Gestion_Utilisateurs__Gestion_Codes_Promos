package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.e_learning.models.Mail;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;
import tn.esprit.e_learning.utils.MyDatabase;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Inscription {

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
    private TextField emailTf;

    @FXML
    private ToggleGroup gender;

    @FXML
    private PasswordField mdpTf;
    @FXML
    private DatePicker birthdayD;
    private Connection connection;
    private Utilisateur utilisateurP;
    //@FXML
    //private
    private boolean maleb;
    //Not to lie i have searched for the emailRegex Format

    private boolean isValidEmail(String email) {
        // Simple email validation using regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    public String hashPassword(String password) {
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        return password;
    }
    @FXML
    void ajouterUtilisateur(ActionEvent event) {
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = new Utilisateur();

        //Controle de saisie
        if(NomTf.getText().isEmpty()||PrenomTf.getText().isEmpty()||ProfessionTf.getText().isEmpty()||emailTf.getText().isEmpty()||mdpTf.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
            return;
        }
        if (!isValidEmail(emailTf.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez saisir une adresse e-mail valide");
            alert.showAndWait();
            return;
        }

        else if(utilisateurService.getUtilisateurByEmail(emailTf.getText()) == null){
            System.out.println("Hnee = "+utilisateurService.getUtilisateurByEmail(emailTf.getText()));
            //Partie ajout
            utilisateur.setNom(NomTf.getText());
            utilisateur.setPrenom(PrenomTf.getText());
            RadioButton selectedRadioButton = (RadioButton) gender.getSelectedToggle();
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
            utilisateur.setProfession(ProfessionTf.getText());
            utilisateur.setEmail(emailTf.getText());
            utilisateur.setPassword(hashPassword(mdpTf.getText()));
            utilisateur.setVerified(true);
            utilisateur.setEnLigne(false);
            utilisateur.setCount(0);
            utilisateur.setRoles("[\"ROLE_USER\"]");
            utilisateur.setBirthDay(Date.valueOf(birthdayD.getValue()));
            utilisateurService.addUser(utilisateur);
            utilisateurP = utilisateur;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Vous êtes inscrits");
            alert.showAndWait();
            Mail mail = new Mail();
            mail.SignedUpNotif(emailTf.getText());
            NomTf.setText("");
            PrenomTf.setText("");
            gender.selectToggle(null);
            ProfessionTf.setText("");
            emailTf.setText("");
            mdpTf.setText("");
        }
    }

    @FXML
    void authentifierUtilisateur(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/login.fxml"));
        try {
            ProfessionTf.getScene().setRoot(fxmlLoader.load());
            /*PreparedStatement getIdStatement = connection.prepareStatement("SELECT LAST_INSERT_ID()");
            var rs = getIdStatement.executeQuery();
            rs.next();
            int userId = rs.getInt(1);
            Soldes soldes = new Soldes(userId,0);
            //AjouterSolde(soldes);*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}