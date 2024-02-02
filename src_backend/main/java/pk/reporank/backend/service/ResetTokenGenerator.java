package pk.reporank.backend.service;

import java.security.SecureRandom;
import java.util.Base64;

public class ResetTokenGenerator {

    private static final int TOKEN_LENGTH = 4; // Długość tokena w bajtach

    public static String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
}}
