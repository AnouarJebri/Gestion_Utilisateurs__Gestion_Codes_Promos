package tn.esprit.e_learning.services;

import tn.esprit.e_learning.models.CodePromos;
import tn.esprit.e_learning.models.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public interface IServices {
    void addUser(Utilisateur utilisateur)throws SQLException;
    public void updateUser(Utilisateur utilisateur) throws SQLException;
    List<Utilisateur> recupererU() throws SQLException;
    public void deleteUser(int id) throws SQLException;

    Utilisateur getUtilisateurByEmail(String email) throws SQLException;

    Utilisateur getUtilisateurByID(int id) throws SQLException;

    public List<Utilisateur> RecupererUr(String query) throws SQLException;
    public Utilisateur getUserByEmail(String email) throws SQLException;
    void ajouterCodePromos(CodePromos codePromos) throws SQLException;
    void modifierCodePromos(CodePromos codePromos) throws SQLException;
    void  supprimerCodePromos() throws SQLException;
    List<CodePromos> recupererC() throws SQLException;
    Boolean TakeCodePromos() throws SQLException;
    public Boolean verifier(int code) throws SQLException;
    List<CodePromos> RecupererCr(String query) throws SQLException;
}
