package com.ecommerce.emailservice.consumer;

import com.ecommerce.emailservice.dto.SendEmailDto;

import com.ecommerce.emailservice.util.EmailUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import java.util.Properties;

@Service
public class EmailConsumer {

    @Value("${email.smtp.username}")
    private String smtpUser;

    @Value("${email.smtp.password}")
    private String smtpPass;

    @Value("${email.smtp.host}")
    private String smtpHost;

    @Value("${email.smtp.port}")
    private int smtpPort;

    private final ObjectMapper objectMapper;

    public EmailConsumer(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void sendEmail(String message) {
        try {
            SendEmailDto dto = objectMapper.readValue(message, SendEmailDto.class);

            final String smtpUser = dto.getFrom(); // better: use configured smtp username
            final String smtpPass = "REPLACE_WITH_APP_PASSWORD";
            final String toEmail = dto.getTo();

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUser, smtpPass);
                }
            };

            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, toEmail, dto.getSubject(), dto.getBody());
        } catch (Exception e) {
            // don't crash the listener container forever; log and return
            e.printStackTrace();
        }
    }
}