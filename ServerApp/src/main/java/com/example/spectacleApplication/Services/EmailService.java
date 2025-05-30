package com.example.spectacleApplication.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    public void sendReservationConfirmationEmail(String toEmail, String spectacleTitre, String date, String heure, Integer numberOfPlaces, String categorieBillet, Float prix, Integer billetId) throws MessagingException {
        System.out.println("Sending email to: " + toEmail + " for spectacle: " + spectacleTitre);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            helper.setFrom(new InternetAddress(fromEmail, "SpectaGo Team"));
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Failed to set 'From' address due to encoding issue", e);
        }
        helper.setTo(toEmail);
        helper.setSubject("Confirmation de votre réservation - " + spectacleTitre);

        String textBody = "Bonjour,\n\n" +
                "Votre réservation a été confirmée avec succès ! Voici les détails :\n" +
                "Spectacle : " + spectacleTitre + "\n" +
                "Date et heure : " + date + " " + heure + "\n" +
                "Nombre de places : " + numberOfPlaces + "\n" +
                "Catégorie de billet : " + categorieBillet + "\n" +
                "Prix total : " + prix + " TND\n" +
                "ID du billet : " + billetId + "\n\n" +
                "Merci de votre confiance !\n" +
                "L'équipe SpectaGo\n" +
                "Si vous n'avez pas effectué cette réservation, veuillez nous contacter à support@spectago.com.";

        String htmlBody = "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "  @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');" +
                "  body { font-family: 'Poppins', Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; background-color: #f5f7ff; line-height: 1.6; }" +
                "  .header { background: linear-gradient(135deg, #6e8efb, #4a6cf7); padding: 40px 20px; text-align: center; border-radius: 8px 8px 0 0; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }" +
                "  .content { padding: 40px; background: #ffffff; border-radius: 0 0 8px 8px; box-shadow: 0 10px 25px rgba(0,0,0,0.08); }" +
                "  .details { background: #f8f9fa; border-radius: 12px; padding: 25px; margin: 25px 0; border-left: 4px solid #6e8efb; }" +
                "  .detail-row { display: flex; margin-bottom: 12px; padding-bottom: 12px; border-bottom: 1px solid #e9ecef; align-items: center; }" +
                "  .detail-row:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }" +
                "  .detail-label { font-weight: 600; color: #495057; width: 45%; font-size: 14px; }" +
                "  .detail-value { color: #212529; width: 55%; font-weight: 500; font-size: 15px; }" +
                "  .footer { text-align: center; margin-top: 30px; font-size: 13px; color: #868e96; line-height: 1.5; }" +
                "  .button { background: #6e8efb; color: white; padding: 14px 28px; text-decoration: none; border-radius: 6px; display: inline-block; margin-top: 25px; font-weight: 500; letter-spacing: 0.5px; box-shadow: 0 4px 15px rgba(110, 142, 251, 0.3); transition: all 0.3s ease; }" +
                "  .button:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(110, 142, 251, 0.4); }" +
                "  h1 { color: white; margin: 0; font-size: 28px; font-weight: 600; letter-spacing: 0.5px; }" +
                "  .confirmation-icon { width: 60px; height: 60px; background-color: #4BB543; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 20px; }" +
                "  .confirmation-icon:before { content: '✓'; color: white; font-size: 30px; font-weight: bold; }" +
                "  .highlight { color: #6e8efb; font-weight: 600; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='header'>" +
                "  <h1>Confirmation de Réservation</h1>" +
                "</div>" +
                "<div class='content'>" +
                "  <div class='confirmation-icon'></div>" +
                "  <p style='font-size: 16px; text-align: center; margin-bottom: 25px;'>Votre réservation pour <span class='highlight'>" + spectacleTitre + "</span><br>a été confirmée avec succès !</p>" +
                "  <div class='details'>" +
                "    <div class='detail-row'><span class='detail-label'>Spectacle</span><span class='detail-value'>" + spectacleTitre + "</span></div>" +
                "    <div class='detail-row'><span class='detail-label'>Date et heure</span><span class='detail-value'>" + date + " • " + heure + "</span></div>" +
                "    <div class='detail-row'><span class='detail-label'>Nombre de places</span><span class='detail-value'>" + numberOfPlaces + " place(s)</span></div>" +
                "    <div class='detail-row'><span class='detail-label'>Catégorie</span><span class='detail-value'>" + categorieBillet + "</span></div>" +
                "    <div class='detail-row'><span class='detail-label'>Prix total</span><span class='detail-value' style='color: #4BB543; font-weight: 600;'>" + prix + " TND</span></div>" +
                "    <div class='detail-row'><span class='detail-label'>ID du billet</span><span class='detail-value' style='font-family: monospace;'>" + billetId + "</span></div>" +
                "  </div>" +
                "  <p style='font-size: 15px; text-align: center;'>Présentez ce billet à l'entrée de l'événement.<br>Une version imprimable est jointe à cet email.</p>" +
                "  <div style='text-align: center;'>" +
                "    <a href='https://www.spectago.com/mes-billets' class='button'>Accéder à mes billets</a>" +
                "  </div>" +
                "  <p style='font-size: 15px; margin-top: 40px; text-align: center;'>Merci de votre confiance,<br><strong>L'équipe SpectaGo</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "  <p>© 2025 SpectaGo. Tous droits réservés.</p>" +
                "  <p>Si vous n'avez pas effectué cette réservation,<br>veuillez nous contacter à <a href='mailto:support@spectago.com' style='color: #6e8efb; text-decoration: none;'>support@spectago.com</a>.</p>" +
                "</div>" +
                "</body></html>";
        helper.setText(textBody, htmlBody);

        try {
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
            throw new MessagingException("Failed to send email", e);
        }
    }

    public void sendTestEmail(String toEmail) throws MessagingException {
        System.out.println("Sending test email to: " + toEmail);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            helper.setFrom(new InternetAddress(fromEmail, "SpectaGo Team"));
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Failed to set 'From' address due to encoding issue", e);
        }
        helper.setTo(toEmail);
        helper.setSubject("Test Email from SpectaGo");

        String textBody = "This is a test email to verify email sending functionality.\n\nBest regards,\nSpectaGo Team";
        String htmlBody = "<html><body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<h2 style='color: #4CAF50;'>Test Email</h2>" +
                "<p>This is a test email to verify email sending functionality.</p>" +
                "<p>Best regards,<br>SpectaGo Team</p>" +
                "</body></html>";

        helper.setText(textBody, htmlBody);

        try {
            mailSender.send(message);
            System.out.println("Test email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send test email to " + toEmail + ": " + e.getMessage());
            throw new MessagingException("Failed to send test email", e);
        }
    }
}