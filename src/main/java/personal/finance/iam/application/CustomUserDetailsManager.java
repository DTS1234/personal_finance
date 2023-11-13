package personal.finance.iam.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserRepository;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();

    @Override
    public void createUser(UserDetails user) {
        validateUserDetails(user);
        User newUser = new User();
        newUser.setEnabled(false);
        newUser.setEmail(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUsername(user.getUsername());
        userRepository.save(newUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        User newUser = new User();
        newUser.setEnabled(user.isEnabled());
        newUser.setEmail(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUsername(user.getUsername());
        userRepository.save(newUser);
    }

    @Override
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new IllegalCallerException("You should use facade method to change the password.");
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByEmail(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User found = userRepository.findByEmail(username);
        if (found == null) {
            throw new UsernameNotFoundException("User with email " + username + " not found");
        }
        return new CustomUserDetails(found);
    }

    private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username may not be empty or null");
    }

}
