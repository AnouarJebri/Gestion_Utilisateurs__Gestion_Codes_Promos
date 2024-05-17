package tn.esprit.e_learning.services;

import com.google.gson.Gson;
import tn.esprit.e_learning.models.CodePromos;
import tn.esprit.e_learning.models.Utilisateur;
import tn.esprit.e_learning.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import static jdk.internal.net.http.common.Utils.close;

public class UtilisateurService {
    private Connection connection;
    public UtilisateurService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    public void addUser(Utilisateur utilisateur) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO utilisateur (email, roles, password, nom, prenom, gender, profession, solde, en_ligne, is_verified, count, birth_day) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, utilisateur.getEmail());
            preparedStatement.setObject(2, utilisateur.getRoles());
            preparedStatement.setString(3, utilisateur.getPassword());
            preparedStatement.setString(4, utilisateur.getNom());
            preparedStatement.setString(5, utilisateur.getPrenom());
            preparedStatement.setBoolean(6, utilisateur.isGender());
            preparedStatement.setString(7, utilisateur.getProfession());
            preparedStatement.setDouble(8, utilisateur.getSolde());
            preparedStatement.setBoolean(9, utilisateur.isEnLigne());
            preparedStatement.setBoolean(10, utilisateur.isVerified());
            preparedStatement.setInt(11, utilisateur.getCount());
            preparedStatement.setDate(12, new java.sql.Date(utilisateur.getBirthDay().getTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read operation
    public Utilisateur getUtilisateurByEmail(String email) {
        Utilisateur utilisateur = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM utilisateur WHERE email = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setId(resultSet.getInt("id"));
                utilisateur.setEmail(resultSet.getString("email"));
                String roles = resultSet.getString("roles");
               // List<String> roles = Arrays.asList(rolesJson.split(","));
                utilisateur.setRoles(roles);
                utilisateur.setPassword(resultSet.getString("password"));
                utilisateur.setNom(resultSet.getString("nom"));
                utilisateur.setPrenom(resultSet.getString("prenom"));
                utilisateur.setGender(resultSet.getBoolean("gender"));
                utilisateur.setProfession(resultSet.getString("profession"));
                utilisateur.setSolde(resultSet.getDouble("solde"));
                utilisateur.setEnLigne(resultSet.getBoolean("en_ligne"));
                utilisateur.setVerified(resultSet.getBoolean("is_verified"));
                utilisateur.setCount(resultSet.getInt("count"));
                utilisateur.setBirthDay(resultSet.getDate("birth_day"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    // Update operation
    public void updateUser(Utilisateur utilisateur) throws SQLException{


            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE utilisateur SET email=?, roles=?, password=?, nom=?, prenom=?, gender=?, profession=?, solde=?, en_ligne=?, is_verified=?, count=?, birth_day=? WHERE id=?");
            preparedStatement.setString(1, utilisateur.getEmail());
            preparedStatement.setObject(2, utilisateur.getRoles());
            preparedStatement.setString(3, utilisateur.getPassword());
            preparedStatement.setString(4, utilisateur.getNom());
            preparedStatement.setString(5, utilisateur.getPrenom());
            preparedStatement.setBoolean(6, utilisateur.isGender());
            preparedStatement.setString(7, utilisateur.getProfession());
            preparedStatement.setDouble(8, utilisateur.getSolde());
            preparedStatement.setBoolean(9, utilisateur.isEnLigne());
            preparedStatement.setBoolean(10, utilisateur.isVerified());
            preparedStatement.setInt(11, utilisateur.getCount());
            java.sql.Date sqlDate = new java.sql.Date(utilisateur.getBirthDay().getTime());
            preparedStatement.setDate(12, sqlDate);
            preparedStatement.setInt(13, utilisateur.getId());
            preparedStatement.executeUpdate();

    }

    // Delete operation
    public void deleteUser(int id) {
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM utilisateur WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   public List<Utilisateur> recupererU() throws SQLException {
    List<Utilisateur> utilisateurs = new ArrayList<>();
    String req = "SELECT * FROM utilisateur";

    try (PreparedStatement ps = connection.prepareStatement(req);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(rs.getInt("id"));
            utilisateur.setEmail(rs.getString("email"));

            // Parsing JSON string to List<String> for roles
            String roles = rs.getString("roles");
            //List<String> roles = Arrays.asList(rolesJson.split(","));
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
            utilisateurs.add(utilisateur);
        }
    } catch (SQLException e) {
        // Handle any SQL exceptions
        e.printStackTrace();
        throw e; // Rethrow the exception
    }

    return utilisateurs;
}

    public List<Utilisateur> RecupererUr(String query) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req = "SELECT * FROM utilisateur WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ?";

        try (PreparedStatement st = connection.prepareStatement(req)) {
            for (int i = 1; i <= 3; i++) {
                st.setString(i, "%" + query + "%");
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setEmail(rs.getString("email"));

                    // Parsing JSON string to List<String> for roles
                    String roles = rs.getString("roles");
                   // List<String> roles = Arrays.asList(rolesJson.split(","));
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
                    utilisateurs.add(utilisateur);
                }
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            throw e; // Rethrow the exception
        }

        return utilisateurs;
    }
   /* @Override
    public Utilisateur getUtilisateurByEmail(String email) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        String req = "SELECT * FROM utilisateur WHERE email = '" + email + "'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);
        if (rs.next()) {
            utilisateur.setId(rs.getInt("id"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setGender(rs.getBoolean("gender"));
            utilisateur.setProfession(rs.getString("profession"));
            utilisateur.setEnligne(rs.getBoolean("EnLigne"));
            utilisateur.setVerif(rs.getBoolean("verif"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setMdp(rs.getString("mdp"));
        }
        return utilisateur;
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
