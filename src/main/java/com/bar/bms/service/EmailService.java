package com.bar.bms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("BMS Password Reset Code");
        message.setText("Your BMS password reset code is: " + code);

        mailSender.send(message);
    }
    public void sendClientBill(String toEmail,
                               String customerName,
                               String items,
                               Double totalAmount,
                               String type) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("BMS " + type + " Bill");

        message.setText(
                "Hello " + customerName + ",\n\n" +
                        "Your " + type.toLowerCase() + " bill has been recorded.\n\n" +
                        "Items:\n" + items + "\n\n" +
                        "Total Amount: " + totalAmount + " RWF\n\n" +
                        "Thank you for buying from us.\n\n" +
                        "BMS Bar Management System"
        );

        mailSender.send(message);
    }
}