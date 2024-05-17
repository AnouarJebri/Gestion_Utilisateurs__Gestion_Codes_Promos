package tn.esprit.e_learning.models;


import java.sql.Date;
import java.util.Objects;
import java.util.Random;
public class CodePromos {
    private int id;
    private String code;
    private Date DateExpiration;
    private Utilisateur utilisateur;
    public CodePromos(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDateExpiration() {
        return DateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        DateExpiration = dateExpiration;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodePromos that = (CodePromos) o;
        return id == that.id && Objects.equals(code, that.code) && Objects.equals(DateExpiration, that.DateExpiration) && Objects.equals(utilisateur, that.utilisateur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, DateExpiration, utilisateur);
    }

    @Override
    public String toString() {
        return "CodePromos :" +

                ", code='" + code + '\'' +
                ", DateExpiration=" + DateExpiration +
                ", utilisateur=" + utilisateur +
                '}';
    }
    public String generateRandomCode(){
        String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int length = 8;
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }
        return codeBuilder.toString();
    }
}
