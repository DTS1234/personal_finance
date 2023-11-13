package personal.finance.iam.infrastracture.persistance.repository;

import personal.finance.iam.domain.PasswordResetToken;
import personal.finance.iam.domain.PasswordResetTokenRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class InMemoryPasswordResetTokenRepository implements PasswordResetTokenRepository {

    private static final Map<Long, PasswordResetToken> TOKENS = new HashMap<Long, PasswordResetToken>();

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        long id = TOKENS.keySet().stream().max(Comparator.comparing(Long::longValue)).orElse(0L) + 1L;
        token.setId(id);
        token.setToken("mock token");
        TOKENS.put(id, token);
        return token;
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return TOKENS.values().stream().filter(tk -> tk.getToken().equals(token)).findFirst().orElse(null);
    }

    public InMemoryPasswordResetTokenRepository clear() {
        TOKENS.clear();
        return this;
    }
}
