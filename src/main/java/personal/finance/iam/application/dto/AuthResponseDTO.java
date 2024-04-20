
package personal.finance.iam.application.dto;

import personal.finance.iam.domain.UserInformation;

public record AuthResponseDTO(String username, String id, String token, String expiresIn, UserInformation userInformation) {

}
