package br.com.raroacademy.projetofinal.validation.usuario;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhaForteValidator implements ConstraintValidator<SenhaForte, String> {

    @Override
    public boolean isValid(String senha, ConstraintValidatorContext contexto) {
        if (senha == null) return false;

        boolean senhaValida = true;
        contexto.disableDefaultConstraintViolation();

        if (senha.length() < 8) {
            contexto.buildConstraintViolationWithTemplate(
                    "A senha deve ter no mínimo 8 caracteres."
            ).addConstraintViolation();
            senhaValida = false;
        }

        if (senha.length() > 255) {
            contexto.buildConstraintViolationWithTemplate(
                    "A senha deve ter no máximo 255 caracteres."
            ).addConstraintViolation();
            senhaValida = false;
        }

        if (!senha.matches(".*[A-Z].*")) {
            contexto.buildConstraintViolationWithTemplate(
                    "A senha deve conter pelo menos uma letra maiúscula."
            ).addConstraintViolation();
            senhaValida = false;
        }

        if (!senha.matches(".*\\d.*")) {
            contexto.buildConstraintViolationWithTemplate(
                    "A senha deve conter pelo menos um número."
            ).addConstraintViolation();
            senhaValida = false;
        }

        return senhaValida;
    }
}
