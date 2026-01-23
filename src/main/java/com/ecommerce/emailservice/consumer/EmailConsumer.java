package com.ecommerce.emailservice.consumer;

import com.ecommerce.emailservice.dto.SendEmailDto;

import com.ecommerce.emailservice.util.EmailUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import java.util.Properties;

@Service
@RequiredArgsConstructor
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


    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void sendEmail(String message) {
        try {
            SendEmailDto dto = objectMapper.readValue(message, SendEmailDto.class);

            final String smtpUser = this.smtpUser;
            final String smtpPass = this.smtpPass;

            final String toEmail = dto.getTo();
            final String subject = dto.getSubject();
            final String body = dto.getBody();

            Properties props = new Properties();
            props.put("mail.smtp.host", this.smtpHost);
            props.put("mail.smtp.port", this.smtpPort);
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

            e.printStackTrace();
        }
    }
}