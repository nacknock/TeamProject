package com.example.HamuPochi.Util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;


@Component
@RequiredArgsConstructor
public class MailSenderRunner {
	
	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String from;
	
	public String sendMail(String mail) {
		Random random = new Random();
		int checkNum = random.nextInt(888888)+111111;
		System.out.println("인증번호: "+checkNum);
		
		SimpleMailMessage message = new SimpleMailMessage();
		System.out.println("전송할 이메일: " + mail);
		message.setFrom(from);
		message.setTo(mail);
		message.setSubject("하늘소 회원가입 인증 이메일 입니다.");
		String content = 
				"홈페이지를 방문해 주셔서 감사합니다."+
				"<br><br>"+
				"인증번호는 "+checkNum+"입니다"+
				"<br>" +
				"해당 인증번호를 복사하여 인증번호 확인란에 기입하여 주세요";
		
		message.setText(content);
		message.setSentDate(new Date());
		mailSender.send(message);
		
		String num = Integer.toString(checkNum);
		return num;
		
		
	}
	
	
	
}
