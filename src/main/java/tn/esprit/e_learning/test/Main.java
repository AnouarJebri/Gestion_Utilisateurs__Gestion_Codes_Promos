package tn.esprit.e_learning.test;

import java.text.ParseException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws ParseException {
       /* MyDatabase db = MyDatabase.getInstance();
        UtilisateurService us = new UtilisateurService();
        CodePromosService cd = new CodePromosService();*/
       /* Mail mail = new Mail();
        mail.sendRecoveryCode("anouar.jebri@gmail.com","Salemou3alaykom");
*/
      /*try {
            us.ajouterUtilisateur(new Utilisateur("Jebri","Anouar",true,"IT Engeneer Student",false,false,"anouar.jebri@gmail.com","passwordTest"));

        } catch (SQLException e){
            System.err.println(e.getMessage());
        }*/
       /* try {
            us.modifierUtilisateur(new Utilisateur(2,"Hammemi","Momen",true,"IT Engeneer Student",true,false,"momen.hammemi@gmail.com","passwordTest1"));

        } catch (SQLException e){
            System.err.println(e.getMessage());
        }*/
        /*try {
            us.supprimerUtilisateur(1);

        } catch (SQLException e){
            System.err.println(e.getMessage());
        }*/
        /*try {
            System.out.println(us.recupererU());
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }*/

        /*try {

            //DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //java.util.Date utilDate = df.parse("2024_02_00");
            //sqlDate = new Date(utilDate.getTime());
            cd.ajouterCodePromos(new CodePromos(500,Date.valueOf("2024-02-07"),true));
        }
        catch (SQLException e){
            System.err.println(e.getMessage());
        }*/
        //Utilisateur utilisateur = new Utilisateur();
       /* String plaintextPassword = "passwordtest";
        String hashedPassword = "$2y$13$Yzj69vnk2biYXniDMv1TZ.xKiQd4q2JxmZXtnAbc9ksVS3WckP50C";
        //sha

        // Verify the hash
        boolean verified = BCrypt.checkpw(plaintextPassword, hashedPassword);
        if (verified) {
            System.out.println("Password is correct.");
        } else {
            System.out.println("Password is incorrect.");

        }*/
        String password = "passwordtest";
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
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                //.thenAccept(System.out::println)
                .join();
        System.out.println(response);
    }
}
