
package com.mycompany.a1;

import java.util.ArrayList;
import java.util.Scanner;

public class A1 {


 
    public static void main(String[] args) {
        PatientManager manager = new PatientManager();
        manager.run();
    }
}
 
/**
 * Represents a single patient record.
 */
class Patient {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String medicalCondition;
    private String type; // Inpatient, Outpatient, or Emergency
 
    public Patient(int id, String firstName, String lastName, int age,
                   String gender, String medicalCondition, String category) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.type = category;
    }
 
    // getr
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getMedicalCondition() { return medicalCondition; }
    public String getCategory() { return type; }
 
    // setr
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }
    public void setCategory(String category) { this.type = category; }
 
    @Override
    public String toString() {
        return String.format(
            "%-6d %-12s %-12s %-4d %-8s %-20s %-12s", 
                // W3schools:java String format() Method
                //https://www.w3schools.com/java/ref_string_format.asp
            id, firstName, lastName, age, gender, medicalCondition, type
        );
    }
}
 
/**
 * Handles all patient record operations: register, search, update, delete, display.
 */
class PatientManager {
    private final ArrayList<Patient> patients = new ArrayList<>();
  Scanner kb = new Scanner(System.in);
    private int nextId = 1; // patient Id (+1)
 
    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
 
            switch (choice) {
                case 1 -> registerPatient();
                case 2 -> searchPatient();
                case 3 -> updatePatient();
                case 4 -> deletePatient();
                case 5 -> displayAllPatients();
                case 6 -> {
                    running = false;
                    System.out.println("Exiting Patient Management System. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please select a valid option (1-6).");
            }
            System.out.println();
        }
        kb.close();
    }
 
    private void printMenu() {
        System.out.println("*".repeat(10)+ "Patient Management System " + "*".repeat(10) );
        System.out.println("1. Register a new patient");
        System.out.println("2. Search for a patient by ID");
        System.out.println("3. Update patient details");
        System.out.println("4. Delete a patient");
        System.out.println("5. Display all patients");
        System.out.println("6. Exit");
        System.out.println("*".repeat(20));
    }
 
    
    // register
    
    private void registerPatient() {
        System.out.println("--- Register New Patient ---");
 
        String firstName = readNonEmptyString("Enter first name: ");
        String lastName = readNonEmptyString("Enter last name: ");
        int age = readPositiveInt("Enter age: ");
        String gender = readNonEmptyString("Enter gender: ");
        String medicalCondition = readNonEmptyString("Enter medical condition: ");
        String category = readCategory();
 
        Patient patient = new Patient(nextId, firstName, lastName, age, gender, medicalCondition, category);
        patients.add(patient);
 
        System.out.println("Patient registered successfully with ID: " + nextId);
        nextId++;
    }
 
  
    // search
   
    private void searchPatient() {
        System.out.println("--- Search Patient ---");
        int id = readInt("Enter patient ID to search: ");
 
        Patient patient = findPatientById(id);
        if (patient == null) {
            System.out.println("No patient found with ID: " + id);
        } else {
            System.out.println("Patient found:");
            printTableHeader();
            System.out.println(patient);
        }
    }
 

    //update details

    private void updatePatient() {
        System.out.println("--- Update Patient Details ---");
        int id = readInt("Enter patient ID to update: ");
 
        Patient patient = findPatientById(id);
        if (patient == null) {
            System.out.println("No patient found with ID: " + id);
            return;
        }
 
        System.out.println("Current details:");
        printTableHeader();
        System.out.println(patient);
        System.out.println("Leave a field blank to keep the current value.");
 
        System.out.print("New first name [" + patient.getFirstName() + "]: ");
        String firstName = kb.nextLine().trim();
        if (!firstName.isEmpty()) patient.setFirstName(firstName);
 
        System.out.print("New last name [" + patient.getLastName() + "]: ");
        String lastName = kb.nextLine().trim();
        if (!lastName.isEmpty()) patient.setLastName(lastName);
 
        System.out.print("New age [" + patient.getAge() + "]: ");
        String ageInput = kb.nextLine().trim();
        if (!ageInput.isEmpty()) {
            try {
                int age = Integer.parseInt(ageInput);
                if (age > 0) {
                    patient.setAge(age);
                } else {
                    System.out.println("Age must be positive. Keeping previous value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid age entered. Keeping previous value.");
            }
        }
 
        System.out.print("New gender [" + patient.getGender() + "]: ");
        String gender = kb.nextLine().trim();
        if (!gender.isEmpty()) patient.setGender(gender);
 
        System.out.print("New medical condition [" + patient.getMedicalCondition() + "]: ");
        String condition = kb.nextLine().trim();
        if (!condition.isEmpty()) patient.setMedicalCondition(condition);
 
        System.out.print("New category (Inpatient/Outpatient/Emergency) [" + patient.getCategory() + "]: ");
        String category = kb.nextLine().trim();
        if (!category.isEmpty()) {
            String normalized = normalizeCategory(category);
            if (normalized != null) {
                patient.setCategory(normalized);
            } else {
                System.out.println("Invalid category entered. Keeping previous value.");
            }
        }
 
        System.out.println("Patient details updated successfully.");
    }
 
  
    //remove patient
  
    private void deletePatient() {
        System.out.println("--- Delete Patient ---");
        int id = readInt("Enter patient ID to delete: ");
 
        Patient patient = findPatientById(id);
        if (patient == null) {
            System.out.println("No patient found with ID: " + id);
            return;
        }
 
        System.out.print("Are you sure you want to delete patient " + patient.getFirstName()
                + " " + patient.getLastName() + " (ID: " + id + ")? (y/n): ");
        String confirm = kb.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            patients.remove(patient);
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
 
    
    //  display all patients
   
    private void displayAllPatients() {
        System.out.println("--- All Registered Patients ---");
        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        printTableHeader();
        for (Patient p : patients) {
            System.out.println(p);
        }
        System.out.println("Total patients: " + patients.size());
    }
 
  
    // Helper methods
    //display for patient info
    private void printTableHeader() {
        System.out.printf("%-6s %-12s %-12s %-4s %-8s %-20s %-12s%n",
                "ID", "FirstName", "LastName", "Age", "Gender", "Condition", "Category");
        System.out.println("-".repeat(30));
    }
 
    private Patient findPatientById(int id) {
        for (Patient p : patients) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
 
    private String readCategory() {
        while (true) {
            System.out.print("Enter category (Inpatient/Outpatient/Emergency): ");
            String input = kb.nextLine().trim();
            String normalized = normalizeCategory(input);
            if (normalized != null) {
                return normalized;
            }
            System.out.println("Invalid category. Please enter Inpatient, Outpatient, or Emergency.");
        }
    }
 
    private String normalizeCategory(String input) {
        if (input.equalsIgnoreCase("Inpatient")) return "Inpatient";
        if (input.equalsIgnoreCase("Outpatient")) return "Outpatient";
        if (input.equalsIgnoreCase("Emergency")) return "Emergency";
        return null;
    }
 
    private String readNonEmptyString(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = kb.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be empty. Please try again.");
        }
    }
 
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = kb.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
 
    private int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be greater than 0. Please try again.");
        }
    }
}
 
