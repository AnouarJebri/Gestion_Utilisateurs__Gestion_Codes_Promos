package tn.esprit.e_learning.services;

import com.google.gson.Gson;
import tn.esprit.e_learning.models.CodePromos;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodePromosService {
    private Connection connection;
    public CodePromosService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouterCodePromos(CodePromos codePromos) throws SQLException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO code_promos (date_expiration, utilisateur_id, code) VALUES (?, ?, ?)");

            // Set the values for the placeholders in the SQL query
            preparedStatement.setDate(1, codePromos.getDateExpiration());
            preparedStatement.setInt(2, codePromos.getUtilisateur().getId());
            preparedStatement.setString(3, codePromos.getCode());

            // Execute the SQL query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Code promo ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du code promo.");
            }

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du code promo : " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
    }

    public void modifierCodePromos(CodePromos codePromos) throws SQLException {
        try {
            String req = "UPDATE code_promos SET date_expiration = ?, utilisateur_id = ?, code = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(req);

            // Set the values for the placeholders in the SQL query
            preparedStatement.setDate(1, codePromos.getDateExpiration());
            preparedStatement.setInt(2, codePromos.getUtilisateur().getId());
            preparedStatement.setString(3, codePromos.getCode());
            preparedStatement.setInt(4, codePromos.getId()); // Assuming getId() returns the ID of the code promo to update

            // Execute the SQL query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Code promo modifié avec succès !");
            } else {
                System.out.println("Échec de la modification du code promo.");
            }

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du code promo : " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
    }

    public void supprimerCodePromos() throws SQLException {
        String req = "DELETE FROM code_promos WHERE date_expiration < ?";
        LocalDate currentDate = LocalDate.now();
        PreparedStatement ps = connection.prepareStatement(req);
        //ps.setInt(1,code);
        ps.setDate(1, Date.valueOf(currentDate));
        ps.executeUpdate();
    }

    public Boolean verifier(String code) throws SQLException {
        String req = "SELECT * FROM code_promos WHERE code = ?";
        try (PreparedStatement st = connection.prepareStatement(req)) {
            st.setString(1, code);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<CodePromos> RecupererCr(String query) throws SQLException {
        List<CodePromos> codePromosS = new ArrayList<>();
        String req = "SELECT * FROM code_promos WHERE code LIKE ?";
        PreparedStatement st = connection.prepareStatement(req);
        for (int i = 1; i <= 2; i++) {
            st.setString(i, "%" + query + "%");
        }
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            CodePromos codePromos = new CodePromos();
            codePromos.setCode(rs.getString("code"));
            codePromos.setId(rs.getInt("id"));
            codePromos.setDateExpiration(Date.valueOf(rs.getDate("date_expiration").toLocalDate()));
            codePromos.setUtilisateur(getUtilisateurByID(rs.getInt("utilisateur_id")));
            codePromosS.add(codePromos);
        }
        return codePromosS;
    }

    public List<CodePromos> recupererC() throws SQLException {
        List<CodePromos> codePromosList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM code_promos");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CodePromos codePromos = new CodePromos();
                codePromos.setId(resultSet.getInt("id"));
                codePromos.setDateExpiration(resultSet.getDate("date_expiration"));
                codePromos.setUtilisateur(getUtilisateurByID(resultSet.getInt("utilisateur_id")));
                codePromos.setCode(resultSet.getString("code"));
                codePromosList.add(codePromos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codePromosList;
    }
    public Boolean TakeCodePromos() throws SQLException {
        List<CodePromos> codePromos = new ArrayList<>();
        String req = "SELECT * FROM code_promos WHERE DateExpiration < ?";
        LocalDate currentDate = LocalDate.now();
        PreparedStatement ps = connection.prepareStatement(req);
        //ps.setInt(1,code);
        ps.setDate(1, Date.valueOf(currentDate));
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int count = rs.getInt(1);
            return count > 0;
        }
        return false;
    }
    /*public float GetCodePRomos(int code)throws SQLException{
        String req = "SELECT valeur FROM codepromos WHERE code = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        return 0;
    }*/
    public Utilisateur getUtilisateurByID(int id) throws SQLException {
        Utilisateur utilisateur = null;
        String req = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setEmail(rs.getString("email"));
                    // Parsing JSON string to List<String> for roles
                    String roles = rs.getString("roles");
                    //List<String> roles = Arrays.asList(new Gson().fromJson(rolesJson, String[].class));
                    utilisateur.setRoles(roles);
                    utilisateur.setPassword(rs.getString("password"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setGender(rs.getBoolean("gender"));
                    utilisateur.setProfession(rs.getString("profession"));
                    utilisateur.setSolde(rs.getDouble("solde"));
                    utilisateur.setEnLigne(rs.getBoolean("en_ligne"));
                    utilisateur.setVerified(rs.getBoolean("is_verified"));
                    utilisateur.setCount(rs.getInt("count"));
                    utilisateur.setBirthDay(rs.getDate("birth_day"));
                }
            }
        }
        return utilisateur;
    }
}
