package br.com.sw2you.realmeet.validator;

import org.springframework.data.util.Streamable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ValidationErrors implements Streamable<ValidationError> {
    private final List<ValidationError> validationErrorList;

    public ValidationErrors(List<ValidationError> validatorErrorList) {
        this.validationErrorList = new ArrayList<>();
    }

    public ValidationErrors add(String field, String errorCode) {
        return add(new ValidationError(field, errorCode));
    }

    public ValidationErrors add(ValidationError validatorError) {
        validationErrorList.add(validatorError);
        return this;
    }

    public ValidationError getError(int index) {
        return validationErrorList.get(index);
    }

    public int getNumberOfErrors() {
        return validationErrorList.size();
    }

    public boolean hasErrors() {
        return !validationErrorList.isEmpty();
    }

    @Override
    public String toString() {
        return "ValidationErrors{" + "validationErrorList=" + validationErrorList + '}';
    }


    @Override
    public Iterator<ValidationError> iterator() {
        return validationErrorList.iterator();
    }
}
