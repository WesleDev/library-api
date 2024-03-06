package com.wesledev.libraryapi.service;

import com.wesledev.libraryapi.model.entity.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

    @Value("{apllication.mail.lateloans.message}")
    private String message;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoanService loanService;

    @Scheduled(cron = CRON_LATE_LOANS)
    void sendMailToLateLoans() {

        List<Loan> allLateLocans = loanService.getAllLateLocans();
        List<String> mailsList = allLateLocans.stream()
                .map(loan -> loan.getCustomerEmail())
                .collect(Collectors.toList());


        emailService.sendMails(message, mailsList);
    }
}
