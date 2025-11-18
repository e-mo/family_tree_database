package edu.vermontstate;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serial;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * A simple class to store user credentials with secure password hashing.
 * Uses bcrypt for password hashing.
 */
@SuppressWarnings("unused, FieldMayBeFinal")
public class Credential implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String hashedUsername;
    private String hashedPassword;
    /**
     * Creates a new Credential with the given username and raw password.
     * The password is automatically hashed using bcrypt.
     *
     * @param username the username
     * @param password the raw (unhashed) password
     */
    public Credential(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        this.hashedUsername = Credential.hashUsername(username);
        this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    static String hashUsername(String username) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(username.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String hashedUsername() {
        return hashedUsername;
    }

    /**
     * Gets the hashed password.
     *
     * @return the hashed password
     */
    public String hashedPassword() {
        return hashedPassword;
    }

    /**
     * Verifies if a raw password matches the stored hashed password.
     *
     * @param rawPassword the password to verify
     * @return true if the password matches, false otherwise
     */
    public boolean verifyPassword(String rawPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    /**
     * Verifies if a raw password matches the stored hashed password.
     * This method accepts a char array which can be zeroed out after use.
     * <p>
     * NOTE: While the input char[] can be cleared, bcrypt internally converts
     * to String, so this provides only partial security improvement.
     *
     * @param rawPassword the password to verify as a char array
     * @return true if the password matches, false otherwise
     */
    public boolean verifyPassword(char[] rawPassword) {
        // Convert to String for bcrypt
        // NOTE: This creates an immutable String in memory that cannot be cleared
        String passwordStr = new String(rawPassword);
        return BCrypt.checkpw(passwordStr, hashedPassword);
    }

    @Override
    public String toString() {
        return "Credential{username='" + hashedUsername + "', pw='" + hashedPassword + "'}";
    }
}