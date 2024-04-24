package personal.finance.iam.infrastracture.web;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.iam.application.UserService;
import personal.finance.iam.application.dto.UserInformationDTO;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserInformation;

import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Timed(percentiles = {0.5, 0.75, 0.95, 0.99})
public class UserController {

    private final UserService userService;

    @PostMapping("/{userId}/update")
    public UserInformationDTO updateUserInformation(@PathVariable("userId") UUID userId, @RequestBody UserInformationDTO userInformation) {
        return userService.updateUserInfo(new UserId(userId), userInformation);
    }

}
