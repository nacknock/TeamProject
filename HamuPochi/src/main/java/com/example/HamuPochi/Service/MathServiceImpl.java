package com.example.HamuPochi.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MathServiceImpl implements MathService{


    @Override
    public long randomInt(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Maximum value must be greater than 0");
        }
        Random random = new Random();
        return (long)random.nextInt(size) + 1l; // 0부터 max-1까지의 랜덤 숫자에 1을 더하여 1부터 max까지의 범위로 조정
    }

    @Override
    public long randomInt(int size, long first) {
        if (size < 1) {
            throw new IllegalArgumentException("Maximum value must be greater than 0");
        }
        Random random = new Random();
        while(true){

            long second = random.nextInt(size) + 1l;

            if(second != first){
                return second;
            }
        }
    }
}
