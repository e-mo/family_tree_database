package edu.vermontstate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a person (node) in a family tree.
 */
public class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final String id;
    private String name;
    private LocalDate birthDate;
    private LocalDate deathDate; // null if alive
    private Sex birthSex;
    private String genderNotes;
    private String medicalHistory;
    private List<Relationship> relationships;
    
    public Person(String id, String name, LocalDate birthDate, Sex birthSex, String genderNotes) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.birthDate = birthDate;
        this.birthSex = Objects.requireNonNull(birthSex, "Birth sex cannot be null");
        this.genderNotes = genderNotes != null ? genderNotes : "";
        this.medicalHistory = "";
        this.relationships = new ArrayList<>();
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
    public LocalDate getDeathDate() { return deathDate; }
    public Sex getBirthSex() { return birthSex; }
    public String getGenderNotes() { return genderNotes; }
    public String getMedicalHistory() { return medicalHistory; }
    public List<Relationship> getRelationships() { return new ArrayList<>(relationships); }
    
    // Setters
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }
    
    public void setBirthSex(Sex birthSex) {
        this.birthSex = Objects.requireNonNull(birthSex, "Birth sex cannot be null");
    }
    
    public void setGenderNotes(String genderNotes) {
        this.genderNotes = genderNotes != null ? genderNotes : "";
    }
    
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory != null ? medicalHistory : "";
    }
    
    public void addRelationship(Relationship relationship) {
        this.relationships.add(Objects.requireNonNull(relationship, "Relationship cannot be null"));
    }
    
    public void removeRelationship(Relationship relationship) {
        this.relationships.remove(relationship);
    }
    
    public void setRelationships(List<Relationship> relationships) {
        this.relationships = new ArrayList<>(relationships);
    }
    
    public boolean isAlive() {
        return deathDate == null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                ", birthSex=" + birthSex +
                ", genderNotes='" + genderNotes + '\'' +
                ", medicalHistory='" + (medicalHistory.length() > 50 ? 
                    medicalHistory.substring(0, 50) + "..." : medicalHistory) + '\'' +
                ", relationships=" + relationships.size() +
                '}';
    }
}
