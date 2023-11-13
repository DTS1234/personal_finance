package personal.finance.iam.domain;

public interface PasswordResetTokenRepository {

    PasswordResetToken save(PasswordResetToken token);

    PasswordResetToken findByToken(String token);
}
