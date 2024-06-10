package root.dto.search;

import lombok.Value;
import org.springframework.data.domain.Sort.Direction;

@Value
public class Sort {
    String property;
    Direction direction;
}
