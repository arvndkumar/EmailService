# Email Service

A microservice responsible for handling email communications in the e-commerce system. It processes email requests
through Kafka messages and sends emails using SMTP.

## Features

- Asynchronous email processing
- Kafka-based message consumption
- SMTP email sending
- Configurable email templates

## Tech Stack

- Java 17
- Spring Boot 4.0.1
- Apache Kafka
- Jakarta Mail
- Lombok
- Jackson

## Configuration

### SMTP Settings

- SMTP Host
- SMTP Port
- SMTP Username
- SMTP Password

### Kafka Settings

- Topic: sendEmail
- Group ID: emailService
- Bootstrap Server: localhost:9092

## Kafka Consumer

The service listens to the "sendEmail" topic and processes email requests with the following structure:

- to: Recipient email address
- subject: Email subject
- body: Email content

## Setup and Installation

1. Clone the repository
2. Configure email settings in application.properties:
   ```
   email.smtp.host=<your-smtp-host>
   email.smtp.port=<your-smtp-port>
   email.smtp.username=<your-username>
   email.smtp.password=<your-password>
   ```
3. Start Kafka using docker-compose: `docker-compose up -d`
4. Build the project: `mvn clean install`
5. Run the application: `mvn spring-boot:run`
