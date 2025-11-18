package edu.vermontstate;

/**
 * Demonstration of the FamilyTreeDatabase functionality.
 */

public class CredentialDatabaseDemo {
    @SuppressWarnings("unused")
    static void main(String[] args) {
        CredentialDatabase credentialDB = new CredentialDatabase("credentials.db");

        String user = "emo";
        String pw = "securePassword86!";

        try {

            if (credentialDB.usernameExists(user)) {
                System.out.println("Username 'emo' exists! Varifying password...");
                if (credentialDB.verifyCredentials(user, pw))
                    System.out.println("Credentials verified!");
                else
                    System.out.println("Credentials denied!");

            } else {
                System.out.println("User 'emo' not found, creating credential and adding to DB...");
                Credential newCred = new Credential(user, pw);
                if (credentialDB.addCredential(newCred))
                    System.out.println("Credential added!");
            }

            credentialDB.saveToFile();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }


    }
}
