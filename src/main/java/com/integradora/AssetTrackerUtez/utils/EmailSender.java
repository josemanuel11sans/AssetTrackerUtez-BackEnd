package com.integradora.AssetTrackerUtez.utils;

import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    private final static Logger logger = LoggerFactory.getLogger(EmailSender.class);
    private final Resend resend;

    // Constructor para inicializar Resend con la API Key
    public EmailSender() {
        this.resend = new Resend("re_QjzJoPhZ_CpnJG5cyo2w8TWtTn6Eywin4"); // AssetTracker API_KEY
    }

    // Método para enviar correos
    public void sendEmail(String to, String subject, String htmlContent) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Acme <onboarding@resend.dev>")
                .to("rocitorres48@gmail.com")
                .subject(subject)
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            logger.info("Correo enviado con éxito. ID: {}", data.getId());
        } catch (ResendException e) {
            logger.error("Error al enviar el correo: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
