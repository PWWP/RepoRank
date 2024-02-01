package pk.reporank.backend.util;

import org.springframework.stereotype.Component;
import pk.reporank.backend.dto.response.ValidationError;

@Component
public class PasswordValidator {

    public ValidationError validatePassword(String password) {
        ValidationError validationError = new ValidationError();
        validationError.setStatusCode(400);
        validationError.setError(null);

        if (password.length() < 8) {
            validationError.getMessage().put("password", "Hasło musi mieć co najmniej 8 znaków");
        }

        if (!password.matches(".*\\d.*")) {
            validationError.getMessage().put("password", "Hasło musi zawierać przynajmniej jedną cyfrę");
        }

        if (!password.matches(".*[a-z].*")) {
            validationError.getMessage().put("password", "Hasło musi zawierać przynajmniej jedną małą literę");
        }

        if (!password.matches(".*[A-Z].*")) {
            validationError.getMessage().put("password", "Hasło musi zawierać przynajmniej jedną wielką literę");
        }

        return validationError;
    }
}