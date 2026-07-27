package com.core.covid19.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Service
public class EmailSender {

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private String port;

    @Value("${mail.smtp.user}")
    private String username;

    @Value("${mail.smtp.pass}")
    private String password;

    /**
     * Sanitiza un email eliminando caracteres no ASCII y espacios sobrantes
     */
    private String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }
        // Elimina caracteres no ASCII y espacios
        return email.replaceAll("[^\\x00-\\x7F]", "").trim();
    }

    public void send(String correoDestinatario, String asunto, String mensaje) {

        // Sanitizar el email antes de usarlo
        String sanitizedEmail = sanitizeEmail(correoDestinatario);
        
        // Validar que el email no quedó vacío después de la sanitización
        if (sanitizedEmail == null || sanitizedEmail.isEmpty()) {
            System.err.println("El email es inválido o quedó vacío tras la sanitización. No se enviará correo.");
            return;
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", port);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sanitizedEmail));
            message.setSubject(asunto);
            message.setContent(mensaje, "text/html; charset=utf-8");

            Transport.send(message);

            System.err.println("Correo enviado a : " + sanitizedEmail);

        } catch (MessagingException e) {
            System.err.println("Error al enviar correo a " + sanitizedEmail + " : " + e.getMessage());
        }
    }

    public void sendEmail(String correoDestinatario, String asunto, String mensaje) throws Exception {

        // Sanitizar el email antes de usarlo
        String sanitizedEmail = sanitizeEmail(correoDestinatario);
        
        // Validar que el email no quedó vacío después de la sanitización
        if (sanitizedEmail == null || sanitizedEmail.isEmpty()) {
            System.err.println("El email es inválido o quedó vacío tras la sanitización. No se enviará correo.");
            throw new Exception("El email proporcionado es inválido o contiene caracteres no permitidos");
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", port);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sanitizedEmail));
            message.setSubject(asunto);
            message.setContent(mensaje, "text/html; charset=utf-8");

            Transport.send(message);

            System.err.println("Correo enviado a : " + sanitizedEmail);

        } catch (MessagingException e) {
            System.err.println("Error al enviar correo a " + sanitizedEmail + " : " + e.getMessage());
            throw new Exception("Ocurrio un Error al enviar correo");
        }
    }
}
