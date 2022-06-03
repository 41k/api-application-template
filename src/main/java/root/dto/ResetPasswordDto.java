package root.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordDto {
    @Email
    @NotBlank
    private String email;
}
