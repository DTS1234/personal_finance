package personal.finance.iam.infrastracture.email;

import personal.finance.iam.application.EmailSenderService;

import java.util.ArrayList;
import java.util.List;

public class EmailSenderServiceMock implements EmailSenderService {

    public static List<String> emailsSend = new ArrayList<>();

    @Override
    public void sendEmailConfirmation(String to, String token) {
        emailsSend.add(to + ":" + token);
    }

    @Override
    public void sendPasswordReset(String to, String token) {
        emailsSend.add(to + ":" + token);
    }

    public void clear() {
        emailsSend.clear();
    }
}
