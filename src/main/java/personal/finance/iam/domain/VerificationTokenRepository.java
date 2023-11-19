package personal.finance.iam.domain;

public interface VerificationTokenRepository {

    VerificationToken findByToken(String token);
    VerificationToken save(VerificationToken token);
    VerificationToken findByUser(String email);

}
