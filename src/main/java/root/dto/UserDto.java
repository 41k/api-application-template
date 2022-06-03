package root.dto;

import root.model.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class UserDto {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String city;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .countryCode(user.getCountryCode())
                .city(user.getCity())
                .build();
    }
}
