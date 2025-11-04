package edu.vermontstate;
/**
 * Types of relationships between people in a family tree.
 */
public enum RelationshipType {
    // Parent-child relationships
    PARENT,
    CHILD,
    MOTHER,
    FATHER,
    SON,
    DAUGHTER,
    
    // Partner relationships
    SPOUSE,
    HUSBAND,
    WIFE,
    PARTNER,
    EX_SPOUSE,
    EX_PARTNER,
    
    // Sibling relationships
    SIBLING,
    BROTHER,
    SISTER,
    HALF_SIBLING,
    HALF_BROTHER,
    HALF_SISTER,
    STEP_SIBLING,
    STEP_BROTHER,
    STEP_SISTER,
    
    // Extended family
    GRANDPARENT,
    GRANDMOTHER,
    GRANDFATHER,
    GRANDCHILD,
    GRANDSON,
    GRANDDAUGHTER,
    
    GREAT_GRANDPARENT,
    GREAT_GRANDCHILD,
    
    AUNT,
    UNCLE,
    NIECE,
    NEPHEW,
    
    COUSIN,
    
    // Step and in-law relationships
    STEPPARENT,
    STEPFATHER,
    STEPMOTHER,
    STEPCHILD,
    STEPSON,
    STEPDAUGHTER,
    
    PARENT_IN_LAW,
    FATHER_IN_LAW,
    MOTHER_IN_LAW,
    CHILD_IN_LAW,
    SON_IN_LAW,
    DAUGHTER_IN_LAW,
    SIBLING_IN_LAW,
    BROTHER_IN_LAW,
    SISTER_IN_LAW,
    
    // Adoptive relationships
    ADOPTIVE_PARENT,
    ADOPTIVE_FATHER,
    ADOPTIVE_MOTHER,
    ADOPTED_CHILD,
    ADOPTED_SON,
    ADOPTED_DAUGHTER,
    
    // Foster relationships
    FOSTER_PARENT,
    FOSTER_CHILD,
    
    // Guardian relationships
    GUARDIAN,
    WARD,

    // Other
    OTHER,
    UNKNOWN
}
