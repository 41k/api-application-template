package root.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    @NotBlank
    private String salt;
    @NotBlank
    private String tokenKey;
    @Min(60000)
    private long tokenTtlInMillis;
}
