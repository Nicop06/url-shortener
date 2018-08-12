package urlshortener.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<UrlConstraint, String> {

    @Override
    public void initialize(UrlConstraint url) {
    }

    @Override
    public boolean isValid(String urlField, ConstraintValidatorContext cxt) {
        org.apache.commons.validator.routines.UrlValidator urlValidator
            = new org.apache.commons.validator.routines.UrlValidator();
        return urlValidator.isValid(urlField) || urlValidator.isValid("http://" + urlField);
    }

}
