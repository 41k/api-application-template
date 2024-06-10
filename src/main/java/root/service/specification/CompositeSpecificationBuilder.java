package root.service.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import root.dto.search.Filter;

import java.util.*;
import java.util.function.Function;

import static java.lang.String.format;

@RequiredArgsConstructor
public class CompositeSpecificationBuilder<T> {

    private static final String FILTER_PROPERTY_ERROR_MSG_FORMAT = "Filtering by property [%s] is not supported";
    private static final String FILTER_VALUE_TYPE_ERROR_MSG_FORMAT = "Value type for filtration property [%s] is incorrect";

    private final Map<String, Function<Filter, Specification<T>>> propertyToSpecificationBuilderMap;

    public Specification<T> build(Collection<Filter> filters) {
        return Optional.ofNullable(filters).orElseGet(Set::of).stream()
                .map(this::toSpecification)
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElseGet(() -> (root, query, criteriaBuilder) -> null);
    }

    private Specification<T> toSpecification(Filter filter) {
        var propertyName = filter.getProperty();
        try {
            return Optional.ofNullable(propertyToSpecificationBuilderMap.get(propertyName))
                    .map(specificationBuilder -> specificationBuilder.apply(filter))
                    .orElseThrow(() -> new IllegalArgumentException(format(FILTER_PROPERTY_ERROR_MSG_FORMAT, propertyName)));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(format(FILTER_VALUE_TYPE_ERROR_MSG_FORMAT, propertyName), e);
        }
    }
}
