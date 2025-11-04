# Family Tree Database

A simple, single-file database system for storing and managing family tree information in Java.

## Features

- **Single-file storage** using Java serialization
- **Fast lookups** by ID using HashMap
- **Multiple search methods**: by name, birth year, gender, relationships, etc.
- **Comprehensive relationship types**: parent/child, siblings, spouses, extended family, adoptive, foster, etc.
- **Separate Sex and Gender fields** for medical and identity purposes
- **Relationship notes** for additional context
- **Atomic file writes** to prevent data corruption
- **Relationship validation** to ensure data integrity

## Core Classes

### FamilyTreeDatabase (Interface)
Defines all the core database operations. This interface allows you to swap storage backends without changing your application code. Current implementations:
- **FamilyTreeDatabaseSimple**: Uses Java serialization (binary format)

#### Why Use an Interface?

The `FamilyTreeDatabase` interface provides several benefits:

1. **Flexibility**: Switch storage backends without changing application code
2. **Testing**: Create mock implementations for unit testing
3. **Future-proofing**: Add new storage options as needs evolve
4. **Separation of concerns**: Business logic is independent of storage format

### Person
Represents an individual in the family tree with:
- Unique ID (within the family tree)
- Name
- Birth date
- Death date (optional)
- Birth sex (for medical purposes)
- Gender notes (free-form text for gender identity, pronouns, etc.)
- Medical history (free-form text for health information, conditions, allergies, etc.)
- List of relationships

### Relationship
Connects two people with:
- Related person's ID
- Relationship type (from enum)
- Optional notes field

### Enums
- **Sex**: MALE, FEMALE, INTERSEX, UNKNOWN
- **RelationshipType**: 70+ relationship types including parent/child, siblings, spouses, extended family, adoptive, foster, godparents, etc. See [RelationshipType](src/main/java/edu/vermontstate/RelationshipType.java) for details.

### Gender Notes
Gender is stored as a free-form text field, allowing for maximum flexibility. You can store:
- Gender identity (e.g., "Male", "Female", "Non-binary", "Genderfluid")
- Pronouns (e.g., "she/her", "he/him", "they/them", "xe/xem")
- Any combination or custom description

### Medical History
Medical history is also stored as a free-form text field for flexibility.

## Usage

### Creating a Database

```java
import edu.vermontstate.FamilyTreeDatabase;
import edu.vermontstate.FamilyTreeDatabaseSimple;

// Using the interface type for flexibility
FamilyTreeDatabase db = new FamilyTreeDatabaseSimple("family_tree.db");

// Easy to swap implementations later:
// IFamilyTreeDatabase db = new JsonFamilyTreeDatabase("family_tree.json");
// IFamilyTreeDatabase db = new SqlFamilyTreeDatabase("jdbc:sqlite:family_tree.db");
```



### Adding People

```java
Person person = new Person(
    db.generateUniqueId(),
    "John Doe",
    LocalDate.of(1980, 5, 15),
    Sex.MALE,
    "Male, he/him"
);

db.addPerson(person);
```

### Setting up Relationships

```java
person.addRelationship(new Relationship(
    otherId, 
    RelationshipType.SPOUSE,
    "Married in 2005"
));
```

### Searching

```java
// Find by ID
Optional<Person> person = db.findById("P12345");

// Find by name (partial match)
List<Person> smiths = db.findByName("Smith");

// Find living people
List<Person> living = db.findLiving();

// Find by birth year
List<Person> bornIn1980 = db.findByBirthYear(1980);

// Find by gender notes (e.g., pronouns or identity)
List<Person> nonBinary = db.findByGenderNotes("non-binary");
List<Person> theyThem = db.findByGenderNotes("they/them");

// Find by medical history
List<Person> diabetics = db.findByMedicalHistory("diabetes");
List<Person> allergies = db.findByMedicalHistory("allergy");
List<Person> heartConditions = db.findByMedicalHistory("heart");

// Find related people
List<Person> relatives = db.findRelatedTo(personId);
```

### Saving

```java
db.saveDatabase(); // Automatically saves to the file specified in constructor
```

## Compiling and Running

```bash
# Compile all files
javac *.java

# Run the demo
java FamilyTreeDemo
```

## File Format

The database uses Java's built-in serialization to store a `Map<String, Person>` object. This provides:
- Simple implementation
- Automatic handling of complex object graphs
- Backward compatibility through serialVersionUID

## Design Decisions

1. **HashMap for storage**: O(1) lookups by ID, which is the most common operation
2. **Interface-based design**: Allows swapping storage backends (serialization, JSON, SQL, etc.)
3. **Serialization**: Simple and reliable default implementation, though not human-readable
4. **Atomic writes**: Write to temp file first, then rename to prevent corruption
5. **Bidirectional relationships**: Stored separately on each person for query flexibility
6. **Optional notes**: Allows context without rigid structure
7. **Separate Sex/Gender**: Birth sex stored as enum for medical purposes; gender stored as free-form text to accommodate any identity, pronouns, or description without constraints

## Creating Alternative Implementations

To create a new storage backend, simply implement the `FamilyTreeDatabase` interface:

```java
import edu.vermontstate.FamilyTreeDatabase;

public class FamilyTreeDatabaseMySql implements FamilyTreeDatabase {
    private Connection connection;

    public FamilyTreeDatabaseMySql(String jdbcUrl) {
        // Connect to MySQL database
    }

    @Override
    public void saveDatabase() throws IOException {
        // SQL INSERT/UPDATE statements
    }

    @Override
    public void loadDatabase() throws IOException {
        // SQL SELECT statements
    }

    // Implement all other interface methods...
}
```

## TODO

- Photo storage (as file paths or embedded)
- Event tracking (marriages, divorces, migrations, etc.)
- Source citations for genealogical research
- Migration to JSON or XML for human-readable files
- Add search by date ranges
- Support for uncertain/approximate dates
- Structured medical data (separate fields for conditions, allergies, medications)
- Medical history privacy controls
- Backup of old database file on save instead of overwrite
- Automatically add inverse relationships to reduce errors
- Catch and invalidate illegal relationship combinations (you can't have 2 birth mothers)

## Thread Safety

This implementation is **not thread-safe**.

## License

No license(s) currently applied.
