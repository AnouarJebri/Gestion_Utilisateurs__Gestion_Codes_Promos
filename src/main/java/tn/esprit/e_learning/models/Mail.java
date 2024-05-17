package tn.esprit.e_learning.models;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {
    private final String username = "anouar.jebri@gmail.com";
    private final String password = "ihux xeib vlrp viws";

    private Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        return Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private void sendNotification(String userEmail, String subject, String content) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            System.out.println("Email notification sent to " + userEmail);
        } catch (MessagingException e) {
            System.out.println("Error sending email notification: " + e.getMessage());
        }
    }

    public void sendRecoveryCode(String userEmail, String verificationCode) {
        String subject = "Verification Code for Your Account";
        String content = "Votre code de vérification est : " + verificationCode;
        sendNotification(userEmail, subject, content);
    }

    public void sendPasswordUpdatedNotif(String userEmail) {
        String subject = "Password updated";
        String content = "Votre mot de passe a été réinitialisé vous pouvez maintenant profiter de nos services !";
        sendNotification(userEmail, subject, content);
    }

    public void SignedUpNotif(String userEmail) {
        String subject = "Inscription réussie";
        String content = "Vous êtes désormais inscrits chez Studentors vous pouvez maintenant cher utilisateur profiter de nos services !";
        sendNotification(userEmail, subject, content);
    }

    public void DeleteAccountNotif(String userEmail) {
        String subject = "Suppression réussie";
        String content = "Votre compte Studentors a été supprimé avec succès !";
        sendNotification(userEmail, subject, content);
    }

    public void UpdatedAccountNotif(String userEmail) {
        String subject = "Update success";
        String content = "Les mises à jour que vous avez adoptées ont été effectuées !";
        sendNotification(userEmail, subject, content);
    }
}
