package edu.vermontstate;
import java.io.*;
import java.util.HashMap;

/**
 * A file-based credential database that stores Credential objects.
 * Uses Java serialization to persist the HashMap to disk.
 * Credentials are indexed by hashed usernames for security.
 */
@SuppressWarnings("unused")
public class CredentialDatabase {
    // HashMap with hashed username as key, Credential as value
    private HashMap<String, Credential> credentials;
    private final String databaseFilePath;

    /**
     * Creates a new CredentialDatabase with the specified file path.
     * If the file exists, it loads the existing database.
     *
     * @param filePath the path to the database file
     */
    public CredentialDatabase(String filePath) {
        this.databaseFilePath = filePath;
        File file = new File(filePath);

        // If the file load fails, an empty credentials map is created.
        if (!loadFromFile()) {
            credentials = new HashMap<>();
        }
    }

    /**
     * Adds a new credential to the database.
     *
     * @param credential the credential to add
     * @return true if added successfully, false if username already exists
     */
    public boolean addCredential(Credential credential) {
        String hashedUsername = credential.hashedUsername();

        if (credentials.containsKey(hashedUsername)) {
            return false; // Username already exists
        }

        credentials.put(hashedUsername, credential);
        return true;
    }

    /**
     * Retrieves a credential by username.
     *
     * @param username the username to look up
     * @return the Credential object, or null if not found
     */
    public Credential getCredential(String username) {
        String hashedUsername = Credential.hashUsername(username);
        return credentials.get(hashedUsername);
    }

    /**
     * Removes a credential from the database.
     *
     * @param username the username of the credential to remove
     * @return true if removed successfully, false if not found
     */
    public boolean removeCredential(String username) {
        String hashedUsername = Credential.hashUsername(username);
        return credentials.remove(hashedUsername) != null;
    }

    /**
     * Checks if a username exists in the database.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        String hashedUsername = Credential.hashUsername(username);
        return credentials.containsKey(hashedUsername);
    }

    /**
     * Verifies a username and password combination.
     *
     * @param username the username
     * @param rawPassword the raw password to verify
     * @return true if the credentials are valid, false otherwise
     */
    public boolean verifyCredentials(String username, String rawPassword) {
        Credential credential = getCredential(username);
        if (credential == null) {
            return false;
        }
        return credential.verifyPassword(rawPassword);
    }

    public boolean verifyCredentials(String username, char[] rawPassword) {
        Credential credential = getCredential(username);
        if (credential == null) {
            return false;
        }
        return credential.verifyPassword(rawPassword);
    }

    /**
     * Gets the number of credentials stored in the database.
     *
     * @return the number of credentials
     */
    public int size() {
        return credentials.size();
    }

    /**
     * Saves the database to the file.
     */
    public boolean saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(databaseFilePath))) {
            oos.writeObject(credentials);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads the database from the file.
     */
    @SuppressWarnings("unchecked")
    private boolean loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(databaseFilePath))) {
            credentials = (HashMap<String, Credential>) ois.readObject();
            System.out.println("Database loaded successfully. " + credentials.size() + " records found.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading database: " + e.getMessage());
            System.err.println("Starting with empty database.");
            credentials = new HashMap<>();
            return false;
        }
    }

    /**
     * Clears all credentials from the database (does not delete the file).
     */
    public void clear() {
        credentials.clear();
    }

    @Override
    public String toString() {
        return "CredentialDatabase{file='" + databaseFilePath +
                "', size=" + credentials.size() + "}";
    }
}