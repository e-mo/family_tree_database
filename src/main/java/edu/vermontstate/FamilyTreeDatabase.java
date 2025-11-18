package edu.vermontstate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Interface defining the core operations for a family tree database.
 * Implementations can use different storage backends (serialization, JSON, XML, SQL, etc.).
 */
@SuppressWarnings("unused")
public interface FamilyTreeDatabase {
    
    // ==================== Persistence Operations ====================
    
    /**
     * Save the database to persistent storage.
     * @throws IOException if the save operation fails
     */
    void saveDatabase() throws IOException;
    
    /**
     * Load the database from persistent storage.
     * @throws IOException if the load operation fails
     */
    void loadDatabase() throws IOException;
    
    // ==================== CRUD Operations ====================
    
    /**
     * Add a new person to the database.
     * @param person the person to add
     * @throws IllegalArgumentException if a person with the same ID already exists
     */
    void addPerson(Person person);
    
    /**
     * Update an existing person in the database.
     * @param person the person to update
     * @throws IllegalArgumentException if the person doesn't exist
     */
    void updatePerson(Person person);
    
    /**
     * Remove a person from the database.
     * @param id the ID of the person to remove
     * @return true if the person was removed, false if not found
     */
    boolean removePerson(String id);
    
    // ==================== Basic Search Operations ====================
    /**
     * Find a person by their unique ID.
     * @param id the person's ID
     * @return an Optional containing the person if found, empty otherwise
     */
    Optional<Person> findById(String id);
    
    /**
     * Find all people with a matching name (case-insensitive partial match).
     * @param name the name to search for
     * @return list of matching people
     */
    List<Person> findByName(String name);
    
    /**
     * Find people by exact name match (case-insensitive).
     * @param name the exact name to match
     * @return list of matching people
     */
    List<Person> findByExactName(String name);
    
    // ==================== Attribute Search Operations ====================
    
    /**
     * Find all people with a specific birth sex.
     * @param sex the birth sex to search for
     * @return list of matching people
     */
    List<Person> findByBirthSex(Sex sex);
    
    /**
     * Find all people whose gender notes contain the specified text (case-insensitive).
     * @param searchText the text to search for
     * @return list of matching people
     */
    List<Person> findByGenderNotes(String searchText);
    
    /**
     * Find all people whose medical history contains the specified text (case-insensitive).
     * @param searchText the text to search for
     * @return list of matching people
     */
    List<Person> findByMedicalHistory(String searchText);
    
    // ==================== Date Search Operations ====================
    
    /**
     * Find all people born in a specific year.
     * @param year the birth year to search for
     * @return list of matching people
     */
    List<Person> findByBirthYear(int year);
    
    // ==================== Collection Operations ====================
    
    /**
     * Get all people in the database.
     * @return list of all people
     */
    List<Person> getAllPeople();
    
    /**
     * Get the total number of people in the database.
     * @return the number of people
     */
    int size();
    
    /**
     * Check if the database is empty.
     * @return true if empty, false otherwise
     */
    boolean isEmpty();
    
    /**
     * Clear all data from the database (does not delete the storage file).
     */
    void clear();
    
    // ==================== Utility Operations ====================
    
    /**
     * Generate a unique ID for a new person.
     * @return a unique ID string
     */
    String generateUniqueId();
    
    /**
     * Validate the integrity of relationships in the database.
     * @return list of validation error messages (empty if all valid)
     */
    List<String> validateRelationships();
}
