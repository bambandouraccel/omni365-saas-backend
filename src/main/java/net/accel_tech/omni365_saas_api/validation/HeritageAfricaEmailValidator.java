package net.accel_tech.omni365_saas_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author NdourBamba18
 **/

public class HeritageAfricaEmailValidator implements ConstraintValidator<ValidHeritageAfricaAccount, String> {

    @Override
    public void initialize(ValidHeritageAfricaAccount constraintAnnotation) {
    }

    @Override
    public boolean isValid(String accountName, ConstraintValidatorContext context) {
        if (accountName == null || accountName.trim().isEmpty()) {
            return false;
        }

        // Valider la longueur
        if (accountName.length() < 3 || accountName.length() > 50) {
            return false;
        }

        // Valider les caractères autorisés
        if (!accountName.matches("^[a-zA-Z0-9._-]+$")) {
            return false;
        }

        // Empêcher les noms qui pourraient causer des problèmes
        String lowerName = accountName.toLowerCase();
        if (lowerName.startsWith("admin") ||
                lowerName.startsWith("root") ||
                lowerName.startsWith("system") ||
                lowerName.contains("..") ||
                lowerName.endsWith(".") ||
                lowerName.startsWith(".")) {
            return false;
        }

        return true;
    }
}
