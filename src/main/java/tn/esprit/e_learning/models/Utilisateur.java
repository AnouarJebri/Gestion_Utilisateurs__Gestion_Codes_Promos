package tn.esprit.e_learning.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Utilisateur {
    private int id;
    private String email;
    @JsonProperty("roles")
    private String roles;
    private String password;
    private String nom;
    private String prenom;
    private boolean gender;
    private String profession;
    private double solde;
    private boolean enLigne;
    private boolean isVerified;
    private int count;
    private Date birthDay;

    public Utilisateur() {
    }

    public Utilisateur(int id, String email, String roles, String password, String nom, String prenom, boolean gender, String profession, boolean enLigne, boolean isVerified, Date birthDay) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.gender = gender;
        this.profession = profession;
        this.solde = 0;
        this.enLigne = enLigne;
        this.isVerified = isVerified;
        this.count = 0;
        this.birthDay = birthDay;
    }

    public String getRoles() {
        return this.roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public boolean isEnLigne() {
        return enLigne;
    }

    public void setEnLigne(boolean enLigne) {
        this.enLigne = enLigne;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return id == that.id && gender == that.gender && Double.compare(solde, that.solde) == 0 && enLigne == that.enLigne && isVerified == that.isVerified && count == that.count && Objects.equals(email, that.email) && Objects.equals(roles, that.roles) && Objects.equals(password, that.password) && Objects.equals(nom, that.nom) && Objects.equals(prenom, that.prenom) && Objects.equals(profession, that.profession) && Objects.equals(birthDay, that.birthDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, roles, password, nom, prenom, gender, profession, solde, enLigne, isVerified, count, birthDay);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +

                ", email='" + email + '\'' +
                ", roles=" + roles +

                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", gender=" + gender +
                ", profession='" + profession + '\'' +

                ", enLigne=" + enLigne +
                ", isVerified=" + isVerified +

                ", birthDay=" + birthDay +
                '}';
    }
    public String hashPassword(String password) {
        String jsonPayload = "{ \"password\": \""+password+"\"}";

        // Create an HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create an HttpRequest with POST method and the JSON payload
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8000/hash"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        // Send the request asynchronously
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                //.thenAccept(System.out::println)
                .join();
    }

}