package com.niton.jchad.verification;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Size(min = 3, max = 32)
@Pattern(regexp = "[a-zA-Z0-9_ -]+")
@NotBlank
@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidName {
	String message() default "This is not an valid name";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
