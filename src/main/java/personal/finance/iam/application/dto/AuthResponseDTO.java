
package personal.finance.iam.application.dto;

public record AuthResponseDTO(String username, Long id, String token, String expiresIn) {

}
