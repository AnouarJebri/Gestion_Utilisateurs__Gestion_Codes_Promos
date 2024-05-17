package tn.esprit.e_learning.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import tn.esprit.e_learning.models.CodePromos;
import tn.esprit.e_learning.models.Mail;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.services.CodePromosService;
import tn.esprit.e_learning.services.UtilisateurService;
import tn.esprit.e_learning.test.HelloApplication;
import tn.esprit.e_learning.utils.MyDatabase;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class UtilisateurI {
    @FXML
    private Label SoldeV;

    @FXML
    private ListView<CodePromos> ListCodePromos;

    @FXML
    private MenuButton Parametres;
    private Connection connection;

    @FXML
    void ModifierCompte(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/e_learning/ModifierOtherAttributesU_I.fxml"));
        try {
            Parametres.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Email entré : "+Login.getEmailTf());
    }

    @FXML
    void ModifyPassword(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            Parametres.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/UpdateMdpConnected.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void SupprimerC(ActionEvent event) {
        UtilisateurService utilisateurService = new UtilisateurService();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("You are about to delete your account  do you still want to proceed the operation ?");
        alert.showAndWait().ifPresent(response->{
            if(response == ButtonType.OK){
                Mail mail = new Mail();
                mail.DeleteAccountNotif(Login.getEmailTf());
                utilisateurService.deleteUser(Login.getID());
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setContentText("Your account has been deleted successfully");
                alert1.showAndWait();
                FXMLLoader fxmlLoader = new FXMLLoader();
                try {
                    Parametres.getScene().setRoot(fxmlLoader.load(HelloApplication.class.getResource("/tn/esprit/e_learning/login.fxml")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Abandon");
                alert1.setContentText("Opération abandonnée");
                alert1.showAndWait();
            }
        });
    }
    @FXML
    void logoutU(ActionEvent event) {
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
            Parametres.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*public float AfficherSoldes(int id)throws SQLException{
        Soldes soldes = new Soldes();
        connection = MyDatabase.getInstance().getConnection();
        String req = "SELECT valeur FROM soldes WHERE id_user = '" + id + "'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);
        while (rs.next()){
            soldes.setValeur(rs.getFloat("valeur"));
            soldes.setId_user(id);
        }
     return soldes.getValeur();
    }*/
    /*public void UpdateSoldes(float valeur)throws SQLException{
        connection = MyDatabase.getInstance().getConnection();
        String req = "UPDATE valeur FROM soldes WHERE id_user = ?";
        PreparedStatement  ps = connection.prepareStatement(req);
        ps.setInt(1,Login.getID());
        ps.executeUpdate();
    }*/
    @FXML
    void initialize(){
        /*CodePromosService codePromosService = new CodePromosService();*/
        try {
            /*ListCodePromos.getItems().addAll(codePromosService.recupererC());*/

            /*List<CodePromos> codePromosList = codePromosService.recupererC();
            ListCodePromos.getItems().clear();
            ListCodePromos.setCellFactory(param -> new ListCell<>(){
                @Override
                protected void updateItem(CodePromos item, boolean empty){
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Display the simplified representation of the CodePromos object
                        setText(item.toString(true));
                        Button acheterButton = new Button("Acheter");
                        acheterButton.setOnAction(event -> {
                            try {
                                acheterAction(item);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        // Add the button to the cell
                        setGraphic(acheterButton);
                    }
                }
            });
            ListCodePromos.getItems().addAll(codePromosList);*/
            UtilisateurService utilisateurService = new UtilisateurService();
            float floatValue = (float) utilisateurService.getUtilisateurByID(Login.getID()).getSolde();
            String formattedValue = String.format("%.3f", floatValue);
            SoldeV.setText("Tokens : " + formattedValue);
            System.out.println("Solde : " + floatValue);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*private void acheterAction(CodePromos item) throws SQLException {
        Mail mail = new Mail();
        Soldes soldes = new Soldes();
        CodePromosService codePromosService = new CodePromosService();
        CodePromos codePromos = item;
        codePromosService.TakeCodePromos();
        soldes.setValeur(AfficherSoldes(Login.getID()));
        soldes.setValeur(soldes.getValeur() + item.getValeur());
        UpdateSoldes(soldes.getValeur());
    }*/
}
