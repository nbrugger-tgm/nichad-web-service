package com.niton.jchad.verification;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Size(min = 1, max = 200 * 1024 * 2)
@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@NotBlank
@Constraint(validatedBy = {})
@Documented
public @interface ValidMessageText {
	String message() default "This is not an valid message";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
