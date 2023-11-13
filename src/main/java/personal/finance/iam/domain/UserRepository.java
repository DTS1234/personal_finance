package personal.finance.iam.domain;

public interface UserRepository {

    User findByEmail(String email);

    User save(User user);

    void deleteByEmail(String email);

}
