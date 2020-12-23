package com.niton.jchad.verification;

import javax.validation.Constraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Size(min=1,max=200*1024*2)
@Target({ METHOD, FIELD, ANNOTATION_TYPE,PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface ValidMessageText {}
