package root.dto.search;

import lombok.Value;

@Value
public class Filter {
    String property;
    Object value;
}
