package com.wesledev.libraryapi.service.impl;

import com.wesledev.libraryapi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("application.mail.default-remetent")
    private String remetent;

    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void sendMails(String message, List<String> mailsList) {
        String[] mails = mailsList.toArray(new String[mailsList.size()]);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(remetent);
        mailMessage.setSubject("Livro com emprestimo atrasado");
        mailMessage.setText(message);
        mailMessage.setTo(mails);

        javaMailSender.send(mailMessage);
    }
}
