package edu.vermontstate;

import java.time.LocalDate;

/**
 * Demonstration of the FamilyTreeDatabase functionality.
 */

public class FamilyTreeDemo {
    @SuppressWarnings("unused")
    static void main(String[] args) {
        // Create or load the database
        FamilyTreeDatabase db = new FamilyTreeDatabaseSimple("family_tree.db");
        
        try {
            // Create some sample people
            System.out.println("\n=== Creating Sample Family ===");
            
            Person john = new Person(
                db.generateUniqueId(),
                "John Smith",
                LocalDate.of(1950, 5, 15),
                Sex.MALE,
                "Male, he/him"
            );
            john.setDeathDate(LocalDate.of(2020, 3, 10));
            john.setMedicalHistory("Type 2 diabetes diagnosed 1995. Heart disease. Passed from cardiac arrest.");
            
            Person mary = new Person(
                db.generateUniqueId(),
                "Mary Smith",
                LocalDate.of(1952, 8, 22),
                Sex.FEMALE,
                "Female, she/her"
            );
            mary.setMedicalHistory("Hypertension managed with medication. Family history of heart disease.");
            
            Person david = new Person(
                db.generateUniqueId(),
                "David Smith",
                LocalDate.of(1975, 3, 8),
                Sex.MALE,
                "Male, he/him"
            );
            david.setMedicalHistory("Seasonal allergies. No major health issues.");
            
            Person sarah = new Person(
                db.generateUniqueId(),
                "Sarah Johnson",
                LocalDate.of(1977, 11, 30),
                Sex.FEMALE,
                "Female, she/her"
            );
            sarah.setMedicalHistory("Asthma (mild, controlled with inhaler). Lactose intolerant.");
            
            Person emma = new Person(
                db.generateUniqueId(),
                "Emma Smith",
                LocalDate.of(2005, 6, 14),
                Sex.FEMALE,
                "Non-binary, they/them"
            );
            emma.setMedicalHistory("Generally healthy. Seasonal allergies like father.");
            
            // Set up relationships
            john.addRelationship(new Relationship(mary.getId(), RelationshipType.SPOUSE, "Married 1973"));
            john.addRelationship(new Relationship(david.getId(), RelationshipType.SON));
            
            mary.addRelationship(new Relationship(john.getId(), RelationshipType.SPOUSE, "Married 1973"));
            mary.addRelationship(new Relationship(david.getId(), RelationshipType.SON));
            
            david.addRelationship(new Relationship(john.getId(), RelationshipType.FATHER));
            david.addRelationship(new Relationship(mary.getId(), RelationshipType.MOTHER));
            david.addRelationship(new Relationship(sarah.getId(), RelationshipType.SPOUSE, "Married 2000"));
            david.addRelationship(new Relationship(emma.getId(), RelationshipType.DAUGHTER));
            
            sarah.addRelationship(new Relationship(david.getId(), RelationshipType.SPOUSE, "Married 2000"));
            sarah.addRelationship(new Relationship(emma.getId(), RelationshipType.DAUGHTER));
            
            emma.addRelationship(new Relationship(david.getId(), RelationshipType.FATHER));
            emma.addRelationship(new Relationship(sarah.getId(), RelationshipType.MOTHER));
            emma.addRelationship(new Relationship(john.getId(), RelationshipType.GRANDFATHER));
            emma.addRelationship(new Relationship(mary.getId(), RelationshipType.GRANDMOTHER));
            
            // Add people to database
            db.addPerson(john);
            db.addPerson(mary);
            db.addPerson(david);
            db.addPerson(sarah);
            db.addPerson(emma);
            
            System.out.println("Added 5 people to the database");
            
            // Demonstrate various search capabilities
            System.out.println("\n=== Search Demonstrations ===");
            
            // Search by ID
            System.out.println("\n1. Find by ID (" + david.getId() + "):");
            db.findById(david.getId()).ifPresent(p -> 
                System.out.println("   Found: " + p.getName() + " (Born: " + p.getBirthDate() + ")")
            );
            
            // Search by name
            System.out.println("\n2. Find by name 'Smith':");
            db.findByName("Smith").forEach(p -> 
                System.out.println("   " + p.getName() + " - " + p.getId())
            );
            
            // Find by birth year
            System.out.println("\n5. Find people born in 1975:");
            db.findByBirthYear(1975).forEach(p -> 
                System.out.println("   " + p.getName())
            );
            
            // Find by gender notes
            System.out.println("\n6. Find people with 'they/them' pronouns:");
            db.findByGenderNotes("they/them").forEach(p -> 
                System.out.println("   " + p.getName() + " - " + p.getGenderNotes())
            );
            
            // Find by medical history
            System.out.println("\n7. Find people with 'allergies' in medical history:");
            db.findByMedicalHistory("allergies").forEach(p -> 
                System.out.println("   " + p.getName() + " - " + p.getMedicalHistory())
            );
            
            // Find people with heart conditions
            System.out.println("\n8. Find people with heart-related conditions:");
            db.findByMedicalHistory("heart").forEach(p -> 
                System.out.println("   " + p.getName() + " - " + p.getMedicalHistory())
            );
            
            // Validate relationships
            System.out.println("\n=== Relationship Validation ===");
            var errors = db.validateRelationships();
            if (errors.isEmpty()) {
                System.out.println("All relationships are valid!");
            } else {
                System.out.println("Found " + errors.size() + " validation errors:");
                errors.forEach(e -> System.out.println("   " + e));
            }
            
            // Save the database
            System.out.println("\n=== Saving Database ===");
            db.saveDatabase();
            
            // Show statistics
            System.out.println("\n=== Database Statistics ===");
            System.out.println("Total people: " + db.size());
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("Database saved to: family_tree.db");
    }
}
