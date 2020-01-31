package at.htl.indoornav.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@ApplicationScoped
public class ValidationService {

    @Inject
    Validator validator;

    public Result getValidationResult(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (violations.isEmpty()) {
            return new Result();
        }
        return new Result(violations);
    }
}
