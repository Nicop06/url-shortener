package urlshortener.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.UrlValidator;

public class UrlFieldValidator implements ConstraintValidator<UrlConstraint, String> {

    @Override
    public void initialize(UrlConstraint url) {
    }

    @Override
    public boolean isValid(String urlField, ConstraintValidatorContext cxt) {
        UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
        return urlValidator.isValid(urlField) || urlValidator.isValid("http://" + urlField);
    }

}
