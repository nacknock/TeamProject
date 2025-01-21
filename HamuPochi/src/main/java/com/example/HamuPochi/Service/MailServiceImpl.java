package com.example.HamuPochi.Service;

import com.example.HamuPochi.Repository.AdminRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MailServiceImpl implements MailService{


    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public String createNumber(){
        String number;
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<6; i++) { // 총 8자리 인증 번호 생성
            int idx = random.nextInt(3); // 0~2 사이의 값을 랜덤하게 받아와 idx에 집어넣습니다

            // 0,1,2 값을 switchcase를 통해 꼬아버립니다.
            // 숫자와 ASCII 코드를 이용합니다.
            switch (idx) {
                case 0 :
                    // 0일 때, A~Z 까지 랜덤 생성 후 key에 추가
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 1:
                    // 1일 때, A~Z 까지 랜덤 생성 후 key에 추가
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 2일 때, 0~9 까지 랜덤 생성 후 key에 추가
                    key.append(random.nextInt(9));
                    break;
            }
        }
        number = key.toString();

        return number;
    }

    @Override
    public String createMailForSellerJoin(String email) throws MessagingException {
        String checkIncode = createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setFrom(from);
        helper.setSubject("【リスコリ】会員登録の認証コード："+checkIncode);
        String content =
                "本人確認に必要な認証コードです\n" +
                        "以下の認証コードを入力し、<wbr>メールアドレスの本人確認を完了させてください。\n" +
                        checkIncode;


        helper.setText(content);

        javaMailSender.send(message);

        return checkIncode;
    }

    @Override
    public String createMailForEmailFind(String email, String email1) throws MessagingException {
        String result = "false";
        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email1);
            helper.setFrom(from);
            helper.setSubject("【リスコリ】ユーザーIDのお知らせ\n");
            String content =
                    "登録したアカウントのユーザーIDです\n" +
                            "以下のメールアドレスを入力し、ログインしてください。\n" +
                            "\tメールアドレス : "+email;

            helper.setText(content);

            javaMailSender.send(message);
            result = "true";
        }catch (Exception e){
            log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            e.printStackTrace();
        }


        return result;
    }

    @Override
    public String createMailForFindPw(String email) {
        String checkIncode = createNumber();
        String result = "false";
        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setFrom(from);
            helper.setSubject("【リスコリ】ユーザーPWのお知らせ\n");
            String content =
                    "本人確認に必要な認証コードです\n" +
                            "以下の認証コードを入力し、<wbr>メールアドレスの本人確認を完了させてください。\n" +
                            checkIncode;

            helper.setText(content);

            javaMailSender.send(message);
            result = checkIncode;
        }catch (Exception e){
            log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            e.printStackTrace();
        }


        return result;
    }


}
