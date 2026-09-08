
package com.mycompany.a1;


import java.util.ArrayList;
import java.util.Scanner;


public class A1 {

    public static void main(String[] args) {
        PatientManager manager = new PatientManager();
        manager.run();
    }
}



 
class Patient {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String medicalCondition;
    private String category; // Inpatient, Outpatient, or Emergency
    private Integer bedNumber; // null if not assigned a bed

    public Patient(int id, String firstName, String lastName, int age,
                   String gender, String medicalCondition, String category) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.category = category;
        this.bedNumber = null;
    }

    // getr
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getMedicalCondition() { return medicalCondition; }
    public String getCategory() { return category; }
    public Integer getBedNumber() { return bedNumber; }

    // setr
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }
    public void setCategory(String category) { this.category = category; }
    public void setBedNumber(Integer bedNumber) { this.bedNumber = bedNumber; }

    //geeks for geeks: Overriding toString() Method in Java
    //https://www.geeksforgeeks.org/java/overriding-tostring-method-in-java/
    @Override
    public String toString() {
        String bedInfo = (bedNumber == null) ? "-" : String.valueOf(bedNumber);
        return String.format(
            "%-6d %-12s %-12s %-4d %-8s %-20s %-12s %-4s",
                 // W3schools:java String format() Method
                //https://www.w3schools.com/java/ref_string_format.asp
            id, firstName, lastName, age, gender, medicalCondition, category, bedInfo
        );
    }
}


class Bed {
    private final int bedNumber; // 1 - 20
    private boolean occupied;
    private Integer patientId; // null if unoccupied

    public Bed(int bedNumber) {
        this.bedNumber = bedNumber;
        this.occupied = false;
        this.patientId = null;
    }

    public int getBedNumber() { return bedNumber; }
    public boolean isOccupied() { return occupied; }
    public Integer getPatientId() { return patientId; }

    public void occupy(int patientId) {
        this.occupied = true;
        this.patientId = patientId;
    }

    public void release() {
        this.occupied = false;
        this.patientId = null;
    }
}


class WardManager {
    private static final int ROWS = 4;
    private static final int COLS = 5;
    private static final int TOTAL_BEDS = ROWS * COLS;

    private final ArrayList<Bed> beds = new ArrayList<>();

    public WardManager() {
        for (int i = 1; i <= TOTAL_BEDS; i++) {
            beds.add(new Bed(i));
        }
    }

    /** Returns the first available bed, or null if the ward is full. */
    private Bed findFirstAvailableBed() {
        for (Bed b : beds) {
            if (!b.isOccupied()) {
                return b;
            }
        }
        return null;
    }

    private Bed findBedByNumber(int bedNumber) {
        for (Bed b : beds) {
            if (b.getBedNumber() == bedNumber) {
                return b;
            }
        }
        return null;
    }

    public boolean isWardFull() {
        return findFirstAvailableBed() == null;
    }

    
    public int allocateBed(Patient patient) {
        if (!patient.getCategory().equalsIgnoreCase("Inpatient")) {
            return -1; // only inpatients can be allocated a bed
        }
        if (patient.getBedNumber() != null) {
            return -2; // already has a bed
        }
        Bed bed = findFirstAvailableBed();
        if (bed == null) {
            return 0; // no beds available
        }
        bed.occupy(patient.getId());
        patient.setBedNumber(bed.getBedNumber());
        return bed.getBedNumber();
    }

    // Releases the bed assigned to the given patient, if any. Returns true if a bed was released. 
    public boolean releaseBedForPatient(Patient patient) {
        if (patient.getBedNumber() == null) {
            return false;
        }
        Bed bed = findBedByNumber(patient.getBedNumber());
        if (bed != null) {
            bed.release();
        }
        patient.setBedNumber(null);
        return true;
    }

    public void displayWardLayout() {
        System.out.println("--- Ward Layout (4x5) ---");
        System.out.println("Legend: [Bed#:Empty] or [Bed#:P<PatientID>]");
        System.out.println();
        int bedIndex = 0;
        for (int r = 0; r < ROWS; r++) {
            StringBuilder rowLine = new StringBuilder();
            for (int c = 0; c < COLS; c++) {
                Bed bed = beds.get(bedIndex);
                String cell;
                if (bed.isOccupied()) {
                    cell = String.format("[%02d:P%-4d]", bed.getBedNumber(), bed.getPatientId());
                } else {
                    cell = String.format("[%02d:Empty]", bed.getBedNumber());
                }
                rowLine.append(cell).append(" ");
                bedIndex++;
            }
            System.out.println(rowLine.toString().trim());
        }
        System.out.println();
        System.out.println("Occupied: " + countOccupied() + " / " + TOTAL_BEDS
                + "   Available: " + countAvailable() + " / " + TOTAL_BEDS);
    }

    public void displayAvailableBeds() {
        System.out.println("--- Available Beds ---");
        boolean any = false;
        for (Bed b : beds) {
            if (!b.isOccupied()) {
                System.out.println("Bed " + b.getBedNumber());
                any = true;
            }
        }
        if (!any) {
            System.out.println("No beds available.");
        } else {
            System.out.println("Total available: " + countAvailable());
        }
    }

    public void displayOccupiedBeds() {
        System.out.println("--- Occupied Beds ---");
        boolean any = false;
        for (Bed b : beds) {
            if (b.isOccupied()) {
                System.out.println("Bed " + b.getBedNumber() + " -> Patient ID: " + b.getPatientId());
                any = true;
            }
        }
        if (!any) {
            System.out.println("No beds are currently occupied.");
        } else {
            System.out.println("Total occupied: " + countOccupied());
        }
    }

    private int countOccupied() {
        int count = 0;
        for (Bed b : beds) {
            if (b.isOccupied()) count++;
        }
        return count;
    }

    private int countAvailable() {
        return TOTAL_BEDS - countOccupied();
    }
}

/**
 * Handles all patient record operations and links to ward/bed management.
 */
class PatientManager {
    private final ArrayList<Patient> patients = new ArrayList<>();
    private final WardManager ward = new WardManager();
    
    Scanner kb = new Scanner(System.in);
    
    
    
    private int nextId = 1; // auto-incrementing patient ID

      // Helper methods
    //display
    private void printTableHeader() {
        System.out.printf("%-6s %-12s %-12s %-4s %-8s %-20s %-12s %-4s%n",
                // W3schools:java String format() Method
                //https://www.w3schools.com/java/ref_string_format.asp
                "ID", "FirstName", "LastName", "Age", "Gender", "Condition", "Category", "Bed");
        System.out.println("-".repeat(20));
    }

    private Patient findPatientById(int id) {
        for (Patient p : patients) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private String readType() {
        while (true) {
            System.out.print("Enter category (Inpatient/Outpatient/Emergency): ");
            String input = kb.nextLine().trim();
            String normalized = normalizeType(input);
            if (normalized != null) {
                return normalized;
            }
            System.out.println("Invalid category. Please enter Inpatient, Outpatient, or Emergency.");
        }
    }

    private String normalizeType(String input) {
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
                case 6 -> allocateBed();
                case 7 -> releaseBed();
                case 8 -> ward.displayWardLayout();
                case 9 -> ward.displayAvailableBeds();
                case 10 -> ward.displayOccupiedBeds();
                case 11 -> {
                    running = false;
                    System.out.println("Exiting Patient Management System. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please select a valid option (1-11).");
            }
            System.out.println();
        }
        kb.close();
    }

    private void printMenu() {
        System.out.println("---- Patient & Ward Management System ----");
        System.out.println("1. Register a new patient");
        System.out.println("2. Search for a patient by ID");
        System.out.println("3. Update patient details");
        System.out.println("4. Delete a patient");
        System.out.println("5. Display all patients");
        System.out.println("6. Allocate a bed to an inpatient");
        System.out.println("7. Release a bed (discharge patient)");
        System.out.println("8. Display ward layout");
        System.out.println("9. Display available beds");
        System.out.println("10. Display occupied beds");
        System.out.println("11. Exit");
        System.out.println("-".repeat(20));
    }


    //  Register a new patient
   
    private void registerPatient() {
        System.out.println("--- Register New Patient ---");

        String firstName = readNonEmptyString("Enter first name: ");
        String lastName = readNonEmptyString("Enter last name: ");
        int age = readPositiveInt("Enter age: ");
        String gender = readNonEmptyString("Enter gender: ");
        String medicalCondition = readNonEmptyString("Enter medical condition: ");
        String category = readType();

        Patient patient = new Patient(nextId, firstName, lastName, age, gender, medicalCondition, category);
        patients.add(patient);

        System.out.println("Patient registered successfully with ID: " + nextId);
        nextId++;
    }

   
    // Search for a patient by ID
  
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

   
    //  Update patient details
    
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
        String firstName = kb .nextLine().trim();
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
        String condition = kb .nextLine().trim();
        if (!condition.isEmpty()) patient.setMedicalCondition(condition);

        System.out.print("New category (Inpatient/Outpatient/Emergency) [" + patient.getCategory() + "]: ");
        String category = kb.nextLine().trim();
        if (!category.isEmpty()) {
            String normalized = normalizeType(category);
            if (normalized != null) {
                // If patient currently holds a bed but is being changed away from Inpatient
                // automatically release the bed since only inpatients may occupy one
                if (!normalized.equalsIgnoreCase("Inpatient") && patient.getBedNumber() != null) {
                    ward.releaseBedForPatient(patient);
                    System.out.println("Note: Bed released automatically since patient is no longer an Inpatient.");
                }
                patient.setCategory(normalized);
            } else {
                System.out.println("Invalid category entered. Keeping previous value.");
            }
        }

        System.out.println("Patient details updated successfully.");
    }

 
    //  Delete a patient
    
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
            // Free up any bed the patient is occupying before removing the record.
            if (patient.getBedNumber() != null) {
                ward.releaseBedForPatient(patient);
                System.out.println("Note: Patient's bed has been released.");
            }
            patients.remove(patient);
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    
    //  Display all patients
  
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

    
    //  Allocate a bed to an inpatient
   
    private void allocateBed() {
        System.out.println("--- Allocate Bed ---");

        if (ward.isWardFull()) {
            System.out.println("Cannot allocate a bed: the ward is full (0 beds available).");
            return;
        }

        int id = readInt("Enter patient ID to allocate a bed to: ");
        Patient patient = findPatientById(id);
        if (patient == null) {
            System.out.println("No patient found with ID: " + id);
            return;
        }

        if (!patient.getCategory().equalsIgnoreCase("Inpatient")) {
            System.out.println("Bed allocation failed: only Inpatients can be allocated a bed. "
                    + "This patient's category is: " + patient.getCategory());
            return;
        }

        int result = ward.allocateBed(patient);
        if (result == -2) {
            System.out.println("This patient already has bed number " + patient.getBedNumber() + " assigned.");
        } else if (result == 0) {
            System.out.println("Cannot allocate a bed: no beds available.");
        } else if (result > 0) {
            System.out.println("Bed " + result + " successfully allocated to patient "
                    + patient.getFirstName() + " " + patient.getLastName() + " (ID: " + id + ").");
        } else {
            System.out.println("Bed allocation failed.");
        }
    }

   
    // Release a bed 
   
    private void releaseBed() {
        System.out.println("--- Release Bed / Discharge Patient ---");
        int id = readInt("Enter patient ID to discharge: ");

        Patient patient = findPatientById(id);
        if (patient == null) {
            System.out.println("No patient found with ID: " + id);
            return;
        }

        if (patient.getBedNumber() == null) {
            System.out.println("This patient does not currently occupy a bed.");
            return;
        }

        int bedNumber = patient.getBedNumber();
        boolean released = ward.releaseBedForPatient(patient);
        if (released) {
            System.out.println("Bed " + bedNumber + " has been released. Patient "
                    + patient.getFirstName() + " " + patient.getLastName() + " discharged from ward.");
        } else {
            System.out.println("Failed to release bed.");
        }
    }

   
  
    }
