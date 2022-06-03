package root.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    String id;
    String email;
    String firstName;
    String lastName;
    String countryCode;
    String city;
}
