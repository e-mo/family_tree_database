package edu.vermontstate;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a relationship between two people in the family tree.
 */
public class Relationship implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String relatedPersonId;
    private RelationshipType type;
    private String notes;
    
    public Relationship(String relatedPersonId, RelationshipType type) {
        this.relatedPersonId = Objects.requireNonNull(relatedPersonId, "Related person ID cannot be null");
        this.type = Objects.requireNonNull(type, "Relationship type cannot be null");
        this.notes = "";
    }
    
    public Relationship(String relatedPersonId, RelationshipType type, String notes) {
        this(relatedPersonId, type);
        this.notes = notes != null ? notes : "";
    }
    
    public String getRelatedPersonId() {
        return relatedPersonId;
    }
    
    public RelationshipType getType() {
        return type;
    }
    
    public void setType(RelationshipType type) {
        this.type = Objects.requireNonNull(type, "Relationship type cannot be null");
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relationship that = (Relationship) o;
        return relatedPersonId.equals(that.relatedPersonId) && type == that.type;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(relatedPersonId, type);
    }
    
    @Override
    public String toString() {
        return "Relationship{" +
                "relatedPersonId='" + relatedPersonId + '\'' +
                ", type=" + type +
                ", notes='" + notes + '\'' +
                '}';
    }
}
