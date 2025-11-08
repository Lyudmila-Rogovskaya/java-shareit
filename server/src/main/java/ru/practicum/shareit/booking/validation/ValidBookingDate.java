package ru.practicum.shareit.booking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingDateValidator.class)
@Documented
public @interface ValidBookingDate {

    String message() default "Invalid booking dates";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
