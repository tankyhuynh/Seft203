package com.kms.seft203.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailUtils {

	private final JavaMailSender javaMailSender;

	@Async
	public void sendMail(String email, String title, String content) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject(title);
		msg.setText(content);

		javaMailSender.send(msg);
	}

}
