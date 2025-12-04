package edu.vermontstate;
import java.io.*;
import java.util.HashMap;

/**
 * A file-based credential database that stores Credential objects.
 * Uses Java serialization to persist the HashMap to disk.
 * Credentials are indexed by hashed usernames for security.
 */
@SuppressWarnings("unused")
public class UserDatabase {
    // HashMap with hashed username as key, Credential as value
    private HashMap<String, User> users;
    private final String usersFilePath;

    /**
     * Creates a new CredentialDatabase with the specified file path.
     * If the file exists, it loads the existing database.
     *
     * @param filePath the path to the database file
     */
    public UserDatabase(String filePath) {
        this.usersFilePath = filePath;
        File file = new File(filePath);

        // If the file load fails, an empty users map is created.
        if (!loadFromFile()) {
            users = new HashMap<>();
        }
    }

    /**
     * Retrieves a User by username.
     *
     * @param username the username to look up
     * @return the User object, or null if not found
     */
    public User getUser(String username) {
        return users.get(username);
    }

    /**
     * Adds a new user to the database.
     *
     * @param user User to add
     * @return true if added successfully, false if user already exists
     */
    public boolean addUser(User user) {
        if (users.containsKey(user.getUserName())) {
            return false; // Username already exists
        }

        users.put(user.getUserName(), user);
        return true;
    }

    /**
     * Removes a user from the database.
     *
     * @param user the User to remove
     * @return true if removed successfully, false if not found
     */
    public boolean removeUser(User user) {
        return users.remove(user.getUserName()) != null;
    }

    /**
     * Checks if a user exists in the database.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    /**
     * Gets the number of users stored in the database.
     *
     * @return the number of users
     */
    public int size() {
        return users.size();
    }

    /**
     * Saves the database to the file.
     */
    public boolean saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.usersFilePath))) {
            oos.writeObject(this.users);
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
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.usersFilePath))) {
            this.users = (HashMap<String, User>) ois.readObject();
            System.out.println("Database loaded successfully. " + users.size() + " records found.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading database: " + e.getMessage());
            System.err.println("Starting with empty database.");
            this.users = new HashMap<>();
            return false;
        }
    }

    /**
     * Clears all users from the loaded database (does not delete the file).
     */
    public void clear() {
        users.clear();
    }

    @Override
    public String toString() {
        return "UsersDatabase{file='" + usersFilePath +
                "', size=" + users.size() + "}";
    }
}