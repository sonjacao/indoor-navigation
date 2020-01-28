package at.htl.indoornav.validator;

import at.htl.indoornav.repository.NodeRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    @Inject
    NodeRepository nodeRepository;

    @Override
    public void initialize(UniqueName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name != null && nodeRepository.getNode(name) == null;
    }
}
