package root.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserActivationDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String verificationCode;
}
