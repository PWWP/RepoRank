package pk.reporank.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    //TODO mail service
//    @Autowired
//    private JavaMailSender javaMailSender;

    public void sendActivationEmail(String to, String activationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Account Activation");
        message.setText("Please click the following link to activate your account:\n" + activationLink);

/*        javaMailSender.send(message);*/
    }

    public void sendResetPasswordEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Password Email");
        message.setText("Please click the following link to Reset Password to your account:\n" + resetToken);

//        javaMailSender.send(message);
    }
}
