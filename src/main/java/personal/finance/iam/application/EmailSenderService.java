package personal.finance.iam.application;

public interface EmailSenderService {

    void sendEmailConfirmation(String to, String token);
    void sendPasswordReset(String to, String token);
}
