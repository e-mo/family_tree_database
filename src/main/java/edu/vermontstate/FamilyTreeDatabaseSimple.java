package edu.vermontstate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A single-file database for storing and retrieving family tree information.
 * Uses Java serialization to store Person objects in a file.
 * <p>
 * This implementation uses HashMap for in-memory storage and Java serialization
 * for persistence. Alternative implementations could use JSON, XML, SQL databases, etc.
 */
@SuppressWarnings("unused")
public class FamilyTreeDatabaseSimple implements FamilyTreeDatabase {
    private final Path databaseFile;
    private Map<String, Person> personMap; // ID -> Person mapping for fast lookups
    
    public FamilyTreeDatabaseSimple(String filename) {
        this.databaseFile = Path.of(filename);
        this.personMap = new HashMap<>();
        loadDatabase();
    }
    
    /**
     * Load the database from file. Creates empty database if file doesn't exist.
     */
    @Override
    public void loadDatabase() {
        if (!Files.exists(databaseFile)) {
            System.out.println("Database file not found. Creating new database.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(databaseFile.toFile())))) {
            @SuppressWarnings("unchecked")
            Map<String, Person> loaded = (Map<String, Person>) ois.readObject();
            personMap = loaded;
            System.out.println("Database loaded successfully. " + personMap.size() + " records found.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading database: " + e.getMessage());
            System.err.println("Starting with empty database.");
            personMap = new HashMap<>();
        }
    }
    
    /**
     * Save the database to file.
     * Uses atomic write by writing to temp file first, then renaming.
     */
    @Override
    public void saveDatabase() throws IOException {
        Path tempFile = Path.of(databaseFile.toString() + ".tmp");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(tempFile.toFile())))) {
            oos.writeObject(personMap);
        }
        
        // Atomic rename
        Files.move(tempFile, databaseFile, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Database saved successfully. " + personMap.size() + " records saved.");
    }
    
    /**
     * Add a new person to the database.
     * @throws IllegalArgumentException if ID already exists
     */
    @Override
    public void addPerson(Person person) {
        if (personMap.containsKey(person.getId())) {
            throw new IllegalArgumentException("Person with ID " + person.getId() + " already exists");
        }
        personMap.put(person.getId(), person);
    }
    
    /**
     * Update an existing person in the database.
     * @throws IllegalArgumentException if person doesn't exist
     */
    @Override
    public void updatePerson(Person person) {
        if (!personMap.containsKey(person.getId())) {
            throw new IllegalArgumentException("Person with ID " + person.getId() + " does not exist");
        }
        personMap.put(person.getId(), person);
    }
    
    /**
     * Remove a person from the database.
     * @return true if person was removed, false if not found
     */
    @Override
    public boolean removePerson(String id) {
        return personMap.remove(id) != null;
    }
    
    /**
     * Find a person by their unique ID.
     */
    @Override
    public Optional<Person> findById(String id) {
        return Optional.ofNullable(personMap.get(id));
    }
    
    /**
     * Find all people with a matching name (case-insensitive).
     */
    @Override
    public List<Person> findByName(String name) {
        String searchName = name.toLowerCase();
        return personMap.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }
    
    /**
     * Find people by exact name match (case-insensitive).
     */
    @Override
    public List<Person> findByExactName(String name) {
        String searchName = name.toLowerCase();
        return personMap.values().stream()
                .filter(p -> p.getName().toLowerCase().equals(searchName))
                .collect(Collectors.toList());
    }
    
    /**
     * Find all people with a specific birth sex.
     */
    @Override
    public List<Person> findByBirthSex(Sex sex) {
        return personMap.values().stream()
                .filter(p -> p.getBirthSex() == sex)
                .collect(Collectors.toList());
    }
    
    /**
     * Find all people whose gender notes contain the specified text (case-insensitive).
     */
    @Override
    public List<Person> findByGenderNotes(String searchText) {
        String search = searchText.toLowerCase();
        return personMap.values().stream()
                .filter(p -> p.getGenderNotes().toLowerCase().contains(search))
                .collect(Collectors.toList());
    }
    
    /**
     * Find all people whose medical history contains the specified text (case-insensitive).
     */
    @Override
    public List<Person> findByMedicalHistory(String searchText) {
        String search = searchText.toLowerCase();
        return personMap.values().stream()
                .filter(p -> p.getMedicalHistory().toLowerCase().contains(search))
                .collect(Collectors.toList());
    }

    /**
     * Find all people born in a specific year.
     */
    @Override
    public List<Person> findByBirthYear(int year) {
        return personMap.values().stream()
                .filter(p -> p.getBirthDate() != null && p.getBirthDate().getYear() == year)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all people in the database.
     */
    @Override
    public List<Person> getAllPeople() {
        return new ArrayList<>(personMap.values());
    }
    
    /**
     * Get the total number of people in the database.
     */
    @Override
    public int size() {
        return personMap.size();
    }
    
    /**
     * Check if the database is empty.
     */
    @Override
    public boolean isEmpty() {
        return personMap.isEmpty();
    }
    
    /**
     * Clear all data from the database (does not delete the file).
     */
    @Override
    public void clear() {
        personMap.clear();
    }
    
    /**
     * Generate a unique ID for a new person.
     * Simple implementation using UUID. You could implement a sequential ID if preferred.
     */
    @Override
    public String generateUniqueId() {
        String id;
        do {
            id = "P" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (personMap.containsKey(id));
        return id;
    }
    
    /**
     * Validate the integrity of relationships in the database.
     * Returns a list of validation errors found.
     */
    @Override
    public List<String> validateRelationships() {
        List<String> errors = new ArrayList<>();
        
        for (Person person : personMap.values()) {
            for (Relationship rel : person.getRelationships()) {
                String relatedId = rel.getRelatedPersonId();
                if (!personMap.containsKey(relatedId)) {
                    errors.add("Person " + person.getId() + " has relationship to non-existent person " + relatedId);
                }
            }
        }
        
        return errors;
    }
}
