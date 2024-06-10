package root.service.specification;

import org.springframework.data.jpa.domain.Specification;
import root.dto.search.Filter;

import java.util.Collection;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class SpecificationBuilder {

    private static final String SQL_LIKE_EXPRESSION_FORMAT = "%%%s%%";

    public static <T> Specification<T> amongProvidedValues(Filter filter) {
        var providedValues = (Collection<?>) filter.getValue();
        return isEmpty(providedValues) ?
                (root, query, criteriaBuilder) -> null :
                (root, query, builder) -> root.get(filter.getProperty()).in(providedValues);
    }

    public static <T> Specification<T> containsSubstring(Filter filter) {
        var providedSubstring = (String) filter.getValue();
        return isBlank(providedSubstring) ?
                (root, query, builder) -> null :
                (root, query, builder) -> builder.like(root.get(filter.getProperty()).as(String.class), like(providedSubstring));
    }

    public static <T> Specification<T> equals(Filter filter) {
        var providedValue = filter.getValue();
        return providedValue == null ?
                (root, query, builder) -> null :
                (root, query, builder) -> builder.equal(root.get(filter.getProperty()), providedValue);
    }

    private static String like(String value) {
        return String.format(SQL_LIKE_EXPRESSION_FORMAT, value);
    }
}
