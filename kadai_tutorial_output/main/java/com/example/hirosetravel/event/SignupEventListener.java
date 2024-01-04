package com.example.hirosetravel.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.hirosetravel.entity.User;
import com.example.hirosetravel.service.VerificationTokenService;

@Component
public class SignupEventListener {
    private final VerificationTokenService VERIFICATION_TOKEN_SERVICE;
    private final JavaMailSender JAVA_MAIL_SENDER;
    
    public SignupEventListener(VerificationTokenService VERIFICATION_TOKEN_SERVICE,JavaMailSender mailSender) {
    	this.VERIFICATION_TOKEN_SERVICE = VERIFICATION_TOKEN_SERVICE;
    	this.JAVA_MAIL_SENDER = mailSender;
    }
    
    @EventListener
    private void onSignupEvent(SignupEvent signupEvent) {
    	User user = signupEvent.getUser();
    	String token = UUID.randomUUID().toString();
    	VERIFICATION_TOKEN_SERVICE.create(user, token);
    	
    	String recipientAddress = user.getEmail();
    	String subject = "メール認証";
    	String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
    	String message = "以下のリンクをクリックして会員登録を完了してください。";
    	
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
    	mailMessage.setTo(recipientAddress);
    	mailMessage.setSubject(subject);
    	mailMessage.setText(message + "\n" + confirmationUrl);
    	JAVA_MAIL_SENDER.send(mailMessage);
    }
}
