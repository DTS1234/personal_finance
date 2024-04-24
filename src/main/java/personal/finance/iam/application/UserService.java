package personal.finance.iam.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import personal.finance.iam.application.dto.UserInformationDTO;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserInformation;
import personal.finance.iam.domain.UserRepository;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserInformationDTO updateUserInfo(UserId userId, UserInformationDTO userInformationDTO) {
        User userFound = userRepository.findById(userId);
        if (userFound == null) {
            throw new IllegalStateException("User not found. " + userId.value);
        }
        UserInformation currentUserInfo = userFound.getUserInformation();
        UserInformation newUserInfo = new UserInformation(currentUserInfo.getEmail(), currentUserInfo.getEmail(),
            currentUserInfo.getPassword(), userInformationDTO.birthdate(), userInformationDTO.gender(),
            userInformationDTO.firstname(), userInformationDTO.lastname(), currentUserInfo.isEnabled());
        User user = userRepository.save(userFound.updateUserInfo(newUserInfo));
        return new UserInformationDTO(
            user.getUserInformation().getFirstname(),
            user.getUserInformation().getLastname(),
            user.getUserInformation().getBirthdate(),
            user.getUserInformation().getGender(),
            user.getUserInformation().getEmail()
        );
    }
}
