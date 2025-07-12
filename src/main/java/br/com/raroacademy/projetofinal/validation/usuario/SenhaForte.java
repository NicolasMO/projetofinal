package br.com.raroacademy.projetofinal.validation.usuario;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SenhaForteValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhaForte {
    String message() default "{senha.forte.invalida}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}