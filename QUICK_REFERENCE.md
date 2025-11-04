# Quick Reference Guide

## Common Operations

### Initialization

```java
import edu.vermontstate.FamilyTreeDatabaseSimple;
import edu.vermontstate.FamilyTreeDatabase;

// Create/load database
FamilyTreeDatabase db = new FamilyTreeDatabaseSimple("family_tree.db");
```

### Adding People

```java
// Create a new person
Person person = new Person(
    db.generateUniqueId(),
    "Jane Doe",
    LocalDate.of(1985, 6, 15),
    Sex.FEMALE,
    "Female, she/her"
);

// Add optional information
person.setMedicalHistory("No known conditions");
person.setDeathDate(LocalDate.of(2045, 12, 31)); // if deceased

// Add to database
db.addPerson(person);
```

### Adding Relationships

```java
// Create relationship
Relationship rel = new Relationship(
    otherPersonId,
    RelationshipType.SPOUSE,
    "Married June 12, 2010"
);

// Add to person
person.addRelationship(rel);

// Update in database
db.updatePerson(person);
```

### Searching

```java
// By ID
Optional<Person> person = db.findById("P12345678");

// By name (partial match)
List<Person> results = db.findByName("Smith");

// By exact name
List<Person> results = db.findByExactName("John Smith");

// By birth year
List<Person> born1980 = db.findByBirthYear(1980);

// By status
List<Person> living = db.findLiving();
List<Person> deceased = db.findDeceased();

// By attributes
List<Person> males = db.findByBirthSex(Sex.MALE);
List<Person> nonBinary = db.findByGenderNotes("non-binary");
List<Person> diabetics = db.findByMedicalHistory("diabetes");

// By relationships
List<Person> relatives = db.findRelatedTo(personId);
List<Person> children = db.findByRelationshipType(personId, RelationshipType.CHILD);
```

### Updating

```java
// Get person
Optional<Person> opt = db.findById("P12345678");
if (opt.isPresent()) {
    Person person = opt.get();
    
    // Modify
    person.setName("Jane Smith-Doe");
    person.setMedicalHistory("Diagnosed with hypertension 2023");
    
    // Save changes
    db.updatePerson(person);
}
```

### Deleting

```java
boolean removed = db.removePerson("P12345678");
if (removed) {
    System.out.println("Person removed");
}
```

### Saving

```java
try {
    db.saveDatabase();
    System.out.println("Database saved successfully");
} catch (IOException e) {
    System.err.println("Failed to save: " + e.getMessage());
}
```

### Validation

```java
List<String> errors = db.validateRelationships();
if (errors.isEmpty()) {
    System.out.println("All relationships are valid");
} else {
    System.out.println("Found issues:");
    errors.forEach(System.out::println);
}
```

## RelationshipType Enum Values

### Immediate Family
- PARENT, MOTHER, FATHER
- CHILD, SON, DAUGHTER
- SIBLING, BROTHER, SISTER

### Partners
- SPOUSE, HUSBAND, WIFE
- PARTNER, EX_SPOUSE, EX_PARTNER

### Half/Step Siblings
- HALF_SIBLING, HALF_BROTHER, HALF_SISTER
- STEP_SIBLING, STEP_BROTHER, STEP_SISTER

### In-Laws
- PARENT_IN_LAW, FATHER_IN_LAW, MOTHER_IN_LAW
- CHILD_IN_LAW, SON_IN_LAW, DAUGHTER_IN_LAW
- SIBLING_IN_LAW, BROTHER_IN_LAW, SISTER_IN_LAW

### Step Relations
- STEPPARENT, STEPFATHER, STEPMOTHER
- STEPCHILD, STEPSON, STEPDAUGHTER

### Adoptive Relations
- ADOPTIVE_PARENT, ADOPTIVE_FATHER, ADOPTIVE_MOTHER
- ADOPTED_CHILD, ADOPTED_SON, ADOPTED_DAUGHTER

### Foster Relations
- FOSTER_PARENT, FOSTER_CHILD

### Guardian Relations
- GUARDIAN, WARD

### Other
- OTHER, UNKNOWN

## Sex Enum Values
- MALE
- FEMALE
- INTERSEX
- UNKNOWN

## Common Patterns

### Building a Family Tree

```java
// Create parents
Person father = new Person(db.generateUniqueId(), "John", birthDate, Sex.MALE, "Male");
Person mother = new Person(db.generateUniqueId(), "Jane", birthDate, Sex.FEMALE, "Female");

// Link parents as spouses
father.addRelationship(new Relationship(mother.getId(), RelationshipType.SPOUSE));
mother.addRelationship(new Relationship(father.getId(), RelationshipType.SPOUSE));

// Create child
Person child = new Person(db.generateUniqueId(), "Junior", birthDate, Sex.MALE, "Male");

// Link child to parents
child.addRelationship(new Relationship(father.getId(), RelationshipType.FATHER));
child.addRelationship(new Relationship(mother.getId(), RelationshipType.MOTHER));

// Link parents to child
father.addRelationship(new Relationship(child.getId(), RelationshipType.SON));
mother.addRelationship(new Relationship(child.getId(), RelationshipType.SON));

// Add all to database
db.addPerson(father);
db.addPerson(mother);
db.addPerson(child);

// Save
db.saveDatabase();
```

### Exporting Data

```java
// Get all people
List<Person> everyone = db.getAllPeople();

// Sort by birth year
everyone.sort(Comparator.comparing(Person::getBirthDate, 
    Comparator.nullsLast(Comparator.naturalOrder())));

// Print summary
for (Person p : everyone) {
    System.out.println(p.getName() + " (" + p.getBirthDate() + ")");
    System.out.println("  Relationships: " + p.getRelationships().size());
    System.out.println("  Status: " + (p.isAlive() ? "Living" : "Deceased"));
}
```

