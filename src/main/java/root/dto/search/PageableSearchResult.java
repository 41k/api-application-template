package root.dto.search;

import lombok.Builder;
import lombok.Value;

import java.util.Collection;

@Value
@Builder
public class PageableSearchResult<T> {
    Integer start;
    Integer limit;
    Integer totalPages;
    Long totalElements;
    Collection<T> data;
}
