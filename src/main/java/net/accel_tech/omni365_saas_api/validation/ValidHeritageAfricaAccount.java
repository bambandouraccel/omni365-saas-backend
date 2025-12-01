package net.accel_tech.omni365_saas_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * @author NdourBamba18
 **/

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HeritageAfricaEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHeritageAfricaAccount {
    String message() default "Invalid Gafa account name. " +
            "Must be 3-50 characters, letters, numbers, dots, underscores, hyphens only";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
