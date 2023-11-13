package personal.finance.iam.infrastracture.persistance.repository;

import org.apache.commons.lang3.NotImplementedException;
import personal.finance.iam.domain.VerificationToken;
import personal.finance.iam.domain.VerificationTokenRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class InMemoryVerificationTokenRepository implements VerificationTokenRepository {

    private static final Map<Long, VerificationToken> TOKENS = new HashMap<>();

    @Override
    public VerificationToken findByToken(String token) {
        return TOKENS.values().stream()
            .filter(verificationToken -> verificationToken.getToken().equals(token))
            .findFirst().orElse(null);
    }

    @Override
    public VerificationToken save(VerificationToken token) {
        Long id = TOKENS.keySet().stream().max(Comparator.comparing(Long::longValue)).orElse(0L) + 1L;
        token.setId(id);
        TOKENS.put(token.getId(), new VerificationToken("mock token", token.getUser()));
        return token;
    }

    @Override
    public VerificationToken findByUser(String  email) {
        throw new NotImplementedException();
    }

    public InMemoryVerificationTokenRepository clear() {
        TOKENS.clear();
        return this;
    }
}
