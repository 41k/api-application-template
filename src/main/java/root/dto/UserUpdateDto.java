package root.dto;

import lombok.Data;

@Data
public class UserUpdateDto {

    private String password;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String city;
}
