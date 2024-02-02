package pk.reporank.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pk.reporank.backend.exception.TokenExpiredException;
import pk.reporank.backend.exception.TokenNotFoundException;
import pk.reporank.backend.repository.UserRepository;
import pk.reporank.backend.model.AppUser;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static pk.reporank.backend.service.ResetTokenGenerator.generateToken;

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    public boolean requestPasswordReset(String email) {

        String resetToken = generateToken();
        Date expiryDate = calculateExpiryDate(); // Implementuj tę metodę zgodnie z Twoimi wymaganiami
        Optional<AppUser> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            AppUser user = optionalUser.get();
            user.setResetToken(resetToken);
            user.setResetTokenExpiryDate(expiryDate);
            userRepository.save(user);
            emailService.sendResetPasswordEmail(email, resetToken);
            return true;
        } return false;
    }

    public void confirmPasswordReset(String token, String newPassword) {
        Optional<AppUser> optionalUser = userRepository.findByResetToken(token);
        if (optionalUser.isPresent()) {
            AppUser user = optionalUser.get();

            // Sprawdź, czy token nie wygasł
            if (user.getResetTokenExpiryDate() != null && user.getResetTokenExpiryDate().after(new Date())) {
                user.setPassword(encoder.encode(newPassword));
                user.setResetToken(null);
                user.setResetTokenExpiryDate(null);
                userRepository.save(user);
            } else {
                throw new TokenExpiredException("Ważność tokenu resetującego hasło skończyła się!");
            }
        } else {
            throw new TokenNotFoundException("Niepoprawny token!");
        }
    }


    private Date calculateExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 24); // Ustawia wygaśnięcie na 24 godziny od obecnej daty
        return calendar.getTime();
    }
}
