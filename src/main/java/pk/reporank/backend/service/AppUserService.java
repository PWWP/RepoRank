package pk.reporank.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pk.reporank.backend.model.AppUser;
import pk.reporank.backend.repository.UserRepository;

import java.util.UUID;


@Service
public class AppUserService {

    @Autowired
    private UserRepository userRepository;

    public AppUser getUserById(UUID userID) {
        return userRepository.getById(userID);
    }
}
