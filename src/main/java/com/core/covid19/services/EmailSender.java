package com.core.covid19.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.regex.Pattern;
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

    // Patrón para validar formato de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    /**
     * Verifica si el servicio de email está configurado
     */
    private boolean isEmailConfigured() {
        return username != null && !username.trim().isEmpty() 
            && password != null && !password.trim().isEmpty();
    }

    /**
     * Sanitiza un email eliminando caracteres no ASCII, espacios y caracteres de control
     */
    private String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }
        
        // Eliminar TODOS los espacios en blanco (incluyendo tabs, newlines, etc.)
        String cleaned = email.replaceAll("\\s+", "");
        
        // Eliminar caracteres de control (0x00-0x1F y 0x7F-0x9F)
        cleaned = cleaned.replaceAll("[\\p{Cntrl}]", "");
        
        // Eliminar caracteres no ASCII (mantener solo ASCII imprimible)
        cleaned = cleaned.replaceAll("[^\\x20-\\x7E]", "");
        
        // Trim final por si acaso
        cleaned = cleaned.trim();
        
        // Validar formato básico de email
        if (!cleaned.isEmpty() && !EMAIL_PATTERN.matcher(cleaned).matches()) {
            System.err.println("[EMAIL] Formato inválido después de sanitización: '" + cleaned + "'");
            return null;
        }
        
        return cleaned.isEmpty() ? null : cleaned;
    }

    /**
     * Valida que el InternetAddress sea válido antes de usarlo
     */
    private boolean isValidInternetAddress(String email) {
        try {
            InternetAddress address = new InternetAddress(email);
            address.validate(); // Valida el formato según RFC 822
            return true;
        } catch (AddressException e) {
            System.err.println("[EMAIL] Dirección no válida según RFC 822: '" + email + "' - " + e.getMessage());
            return false;
        }
    }

    public void send(String correoDestinatario, String asunto, String mensaje) {

        // Verificar si el servicio de email está configurado
        if (!isEmailConfigured()) {
            System.err.println("[EMAIL] Servicio de email NO configurado. No se enviará correo a: " + correoDestinatario);
            return;
        }

        // Sanitizar el email antes de usarlo
        String sanitizedEmail = sanitizeEmail(correoDestinatario);
        
        // Validar que el email no quedó vacío después de la sanitización
        if (sanitizedEmail == null || sanitizedEmail.isEmpty()) {
            System.err.println("[EMAIL] Email original '" + correoDestinatario + "' es inválido o quedó vacío tras la sanitización. No se enviará correo.");
            return;
        }

        // Validar con InternetAddress antes de intentar enviar
        if (!isValidInternetAddress(sanitizedEmail)) {
            System.err.println("[EMAIL] Email '" + sanitizedEmail + "' no es válido. No se enviará correo.");
            return;
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");  // Cambio aquí
        prop.put("mail.smtp.starttls.required", "true"); // Nueva línea

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

            System.out.println("[EMAIL] ✓ Correo enviado exitosamente a: " + sanitizedEmail);

        } catch (MessagingException e) {
            System.err.println("[EMAIL] ✗ Error al enviar correo a '" + sanitizedEmail + "' : " + e.getMessage());
            System.err.println("[EMAIL] Causa raíz: " + (e.getCause() != null ? e.getCause().getMessage() : "No disponible"));
        }
    }

    public void sendEmail(String correoDestinatario, String asunto, String mensaje) throws Exception {

        // Verificar si el servicio de email está configurado
        if (!isEmailConfigured()) {
            String errorMsg = "Servicio de email NO configurado. Configure mail.smtp.user y mail.smtp.pass";
            System.err.println("[EMAIL] " + errorMsg);
            throw new Exception(errorMsg);
        }

        // Sanitizar el email antes de usarlo
        String sanitizedEmail = sanitizeEmail(correoDestinatario);
        
        // Validar que el email no quedó vacío después de la sanitización
        if (sanitizedEmail == null || sanitizedEmail.isEmpty()) {
            String errorMsg = "El email original '" + correoDestinatario + "' es inválido o contiene caracteres no permitidos";
            System.err.println("[EMAIL] " + errorMsg);
            throw new Exception(errorMsg);
        }

        // Validar con InternetAddress antes de intentar enviar
        if (!isValidInternetAddress(sanitizedEmail)) {
            String errorMsg = "El email '" + sanitizedEmail + "' no tiene un formato válido";
            System.err.println("[EMAIL] " + errorMsg);
            throw new Exception(errorMsg);
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");  // Cambio aquí
        prop.put("mail.smtp.starttls.required", "true"); // Nueva línea

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

            System.out.println("[EMAIL] ✓ Correo enviado exitosamente a: " + sanitizedEmail);

        } catch (MessagingException e) {
            System.err.println("[EMAIL] ✗ Error al enviar correo a '" + sanitizedEmail + "' : " + e.getMessage());
            throw new Exception("Ocurrió un error al enviar correo: " + e.getMessage());
        }
    }
}
