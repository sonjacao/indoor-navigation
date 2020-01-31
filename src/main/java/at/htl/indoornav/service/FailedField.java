package at.htl.indoornav.service;

import javax.json.bind.annotation.JsonbTransient;
import java.lang.annotation.Annotation;

public class FailedField {
    private String key;
    private String value;
    private String message;
    @JsonbTransient
    private Annotation constraint;

    public FailedField(String key, String value, String message) {
        this.key = key;
        this.value = value;
        this.message = message;
    }

    public FailedField(String key, String value, String message, Annotation constraint) {
        this.key = key;
        this.value = value;
        this.message = message;
        this.constraint = constraint;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Annotation getConstraint() {
        return constraint;
    }

    public void setConstraint(Annotation constraint) {
        this.constraint = constraint;
    }
}
