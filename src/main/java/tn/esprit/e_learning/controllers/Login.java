package tn.esprit.e_learning.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;

import java.sql.SQLException;
import java.text.ParseException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.IOException;



public class Login {
    private static String userEmail;
    private static int id;
    @FXML
    private Button signin;
    @FXML
    private Button login;


    @FXML
    private TextField emailTF;

    @FXML
    private PasswordField mdpTF;
    private boolean isValidEmail(String email) {
        // Simple email validation using regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    @FXML
    void inscrire(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/inscription.fxml"));
        try {
            signin.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*public static String doHashing(String mdp){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(mdp.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : resultByteArray){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }*/
    public String hashPassword(Utilisateur utilisateur) {
        String jsonPayload = "{\"email\": \"" + utilisateur.getEmail() + "\", \"password\": \""+mdpTF.getText()+"\"}";

        // Create an HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create an HttpRequest with POST method and the JSON payload
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8000/hash/password"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        // Send the request asynchronously
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                //.thenAccept(System.out::println)
                .join();
        return response;
    }

    @FXML
    void LoginUtilisateur(ActionEvent event) {
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = new Utilisateur();
        //Controle de saisie
        if(emailTF.getText().isEmpty()||mdpTF.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
            return;
        }
        if (!isValidEmail(emailTF.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez saisir une adresse e-mail valide");
            alert.showAndWait();
            return;
        }
        //utilisateur.setEmail(emailTF.getText());
        //utilisateur.setMdp(mdpTF.getText());
        try{
            utilisateur = utilisateurService.getUtilisateurByEmail(emailTF.getText());
            System.out.println(utilisateur);
            if (utilisateur != null){
                id = utilisateur.getId();
                //here change method necessary of hash
                if(hashPassword(utilisateur).equals("Credentials are correct")){
                    utilisateur.setEnLigne(true);
                    utilisateurService.updateUser(utilisateur);
                    //SessionManager.startSession(utilisateur);
                    if (utilisateur.getRoles().contains("ROLE_ADMIN")){
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        login.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/Admin_I.fxml")));
                    }
                    else {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/Utilisateur_I.fxml"));
                        login.getScene().setRoot(fxmlLoader.load());
                    }
                }
                else if(hashPassword(utilisateur).equals("Invalid credentials")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Mot de passe incorrect");
                    alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Aucun utilisateur trouvé avec cet email");
                alert.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void CodeValidation(ActionEvent event) {
        if (emailTF.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Remplir le champ du mail svp");
            alert.showAndWait();
        } else {
            /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mail sent");
            alert.setContentText("Vous recevrez un email contenant un code de réinitialisation");
            alert.showAndWait();*/
            userEmail = emailTF.getText();
            Utilisateur utilisateur = new Utilisateur();
            UtilisateurService utilisateurService = new UtilisateurService();
            try {
                utilisateur = utilisateurService.getUtilisateurByEmail(userEmail);
                if(utilisateur.getNom()!=null){
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/CodeResetCompte.fxml"));
                    login.getScene().setRoot(fxmlLoader.load());
                    System.out.println("Utilisateur Connecté :"+utilisateur);
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Utilisateur non existant veuillez vous inscrire");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static String getEmailTf(){
        return userEmail;
    }
    public static int getID(){
        return id;
    }
}

