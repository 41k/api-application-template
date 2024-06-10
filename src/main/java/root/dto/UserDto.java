package root.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class UserDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String city;
}
