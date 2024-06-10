package root.service;

import root.model.User;
import root.service.specification.CompositeSpecificationBuilder;
import root.service.specification.SpecificationBuilder;

import java.util.Map;

public class UserSpecificationBuilder extends CompositeSpecificationBuilder<User> {

    public UserSpecificationBuilder() {
        super(Map.of(
                "id", SpecificationBuilder::amongProvidedValues,
                "email", SpecificationBuilder::amongProvidedValues,
                "firstName", SpecificationBuilder::containsSubstring,
                "active", SpecificationBuilder::equals
        ));
    }
}
