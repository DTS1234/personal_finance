package personal.finance.iam.infrastracture.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import personal.finance.iam.application.EmailSenderService;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender sender;

    public EmailSenderServiceImpl(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendEmailConfirmation(String to, String token) {

        String subject = "Verify your email - Wealth tracker";
        String content = "To confirm your account, please click here : "
            + "http://localhost:8080/registration/confirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);
        sender.send(email);
    }

    @Override
    public void sendPasswordReset(String to, String token) {

        String subject = "Password reset request - Wealth tracker";
        String content = "To reset you password, please click here : "
            + "http://localhost:4200/password_reset?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);
        sender.send(email);
    }
}
