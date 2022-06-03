package root.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private String firstName;
    private String lastName;
    private String countryCode;
    private String city;
    private String verificationCode;
    private boolean active;
}
