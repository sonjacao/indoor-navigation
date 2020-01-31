package at.htl.indoornav.service;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Result {

    private boolean isSuccessful;
    private List<FailedField> failedFields;

    Result() {
        this.isSuccessful = true;
    }

    Result(Set<? extends ConstraintViolation<?>> violations) {
        this.isSuccessful = false;
        this.failedFields = violations.stream()
                .map(o -> new FailedField(
                        o.getPropertyPath().toString(),
                        o.getInvalidValue() != null ? o.getInvalidValue().toString() : "",
                        o.getMessage(),
                        o.getConstraintDescriptor().getAnnotation()
                )).collect(Collectors.toList());
    }

    public void removeNameForUpdate(String name) {
        for (FailedField failedField : failedFields) {
            // When the new name is same as the old name, remove it from the failed fields
            if (failedField.getKey().equals("name") && failedField.getValue().equals(name)) {
                failedFields.remove(failedField);
                if (failedFields.size() == 0) {
                    isSuccessful = true;
                }
                break;
            }
        }
    }

    public boolean getIsSuccessful() {
        return isSuccessful;
    }

    public List<FailedField> getFailedFields() {
        return failedFields;
    }
}
