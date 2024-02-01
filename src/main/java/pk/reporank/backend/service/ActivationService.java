package pk.reporank.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pk.reporank.backend.model.AppUser;
import pk.reporank.backend.repository.UserRepository;

@Service
public class ActivationService {
    @Autowired
    UserRepository userRepository;

    public void activateAccount(AppUser user) {

        user.setActivated(true);
        user.setActivationToken(null);
        userRepository.save(user);

    }
}
