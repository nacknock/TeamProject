package com.example.HamuPochi.Service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface MailService {

    public String createNumber();

    public String createMailForSellerJoin(String email) throws MessagingException;

    String createMailForEmailFind(String email, String email1) throws MessagingException;

    String createMailForFindPw(String email);
}
