
package it.pagopa.interop.probing.caller.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import it.pagopa.interop.probing.caller.annotations.validator.StringArrayValidator;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringArrayValidator.class)
public @interface ValidateStringArraySize {

	int maxSize();

	String message() default "One of the strings of the array is more than {maxSize} characters long";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
