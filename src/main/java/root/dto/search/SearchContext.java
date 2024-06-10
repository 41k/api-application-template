package root.dto.search;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
public class SearchContext {

    @Min(value = 0, message = "Start must not be less than 0")
    private int start;
    @Min(value = 1, message = "Limit must not be less than 1")
    private int limit;
    private Collection<Filter> filters;
    private Collection<Sort> sorts;

    public Pageable getPageable() {
        var pageIndex = start / limit;
        var sort = getSort();
        return PageRequest.of(pageIndex, limit, sort);
    }

    private org.springframework.data.domain.Sort getSort() {
        var orders = Optional.ofNullable(sorts).orElseGet(List::of).stream()
                .map(sort -> new org.springframework.data.domain.Sort.Order(sort.getDirection(), sort.getProperty()))
                .collect(Collectors.toList());
        return org.springframework.data.domain.Sort.by(orders);
    }
}
