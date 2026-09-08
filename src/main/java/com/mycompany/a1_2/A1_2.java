
package com.mycompany.a1_2;

import java.util.ArrayList;
import java.util.Scanner;

public class A1_2 {

    public static void main(String[] args) {
        PatientManager manager = new PatientManager();
        manager.run();
    }
}
enum PatientCategory {
    INPATIENT, OUTPATIENT, EMERGENCY;
 
   
    @Override
    public String toString() {
        String n = name();
        return n.charAt(0) + n.substring(1).toLowerCase();
    }
 
    }

class Patient {
    
    private final int id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String medicalCondition;
    private final PatientCategory category;
    
    public Patient(int id, String firstName, String lastName, int age,
                   String gender, String medicalCondition ,PatientCategory category ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.category = category;
    }

    // getr
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getMedicalCondition() { return medicalCondition; }
     public PatientCategory getCategory() { return category; }

    // setr
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }

    
    
 public String displayDetails() {
        return "Patient ID: " + id
                + " | Name: " + firstName + " " + lastName
                + " | Age: " + age
                + " | Gender: " + gender
                + " | Condition: " + medicalCondition
                + " | Category: " + category;
    }
 
    @Override
    public String toString() {
        return displayDetails();
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

    private final Bed[][] beds= new Bed[ROWS][COLS];

    public WardManager() {
       int bedNumber = 1;
        for (int r=0 ; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                beds[r][c] = new Bed(bedNumber);
                bedNumber++;
            }
            
        }
        }
    

    /** Returns the first available bed, or null if the ward is full. */
    private Bed findFirstAvailableBed() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!beds[r][c].isOccupied()) {
                    return beds[r][c];
                }
            }
        }
          
        return null;
    }

    private Bed findBedByNumber(int bedNumber) {
        if (bedNumber < 1 || bedNumber > TOTAL_BEDS) {
        return null;
    }
        int index = bedNumber - 1;
        int row = index/COLS;
        int col = index%COLS;
        return beds[row][col];
    }

    public boolean isWardFull() {
        return findFirstAvailableBed() == null;
    }

    
    public int allocateBed(Patient patient) {
        if (patient instanceof inpatient) {
        } else {
            return -1; // only inpatients can be allocated a bed
        }
        inpatient inpatient = (inpatient) patient;
 
        if (inpatient.getBedNumber() != null) {
            return -2; // already has a bed
        }
        Bed bed = findFirstAvailableBed();
        if (bed == null) {
            return 0; // no beds available
        }
        bed.occupy(inpatient.getId());
        inpatient.setBedNumber(bed.getBedNumber());
        return bed.getBedNumber();
    }

    // Releases the bed assigned to the given patient, if any. Returns true if a bed was released. 
    public boolean releaseBedForPatient(Patient patient) {
        if (!(patient instanceof inpatient)) {
            return false;
        }
        inpatient inpatient = (inpatient) patient;
        if (inpatient.getBedNumber() == null) {
            return false;
        }
        Bed bed = findBedByNumber(inpatient.getBedNumber());
        if (bed != null) {
            bed.release();
        }
        inpatient.setBedNumber(null);
        return true;
    }

    public void displayWardLayout() {
        System.out.println("--- Ward Layout (4x5) ---");
        System.out.println();
        int bedIndex = 0;
        for (int r = 0; r < ROWS; r++) {
            StringBuilder rowLine = new StringBuilder();
            for (int c = 0; c < COLS; c++) {
                Bed bed = beds[r][c];
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
      for (int r = 0; r < ROWS; r++) {
    for (int c = 0; c < COLS; c++) {
        Bed b = beds[r][c];

        if (!b.isOccupied()) {
            System.out.println("Bed " + b.getBedNumber());
            }
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
            for (int r = 0; r < ROWS; r++) {
    for (int c = 0; c < COLS; c++) {
        Bed b = beds[r][c];
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
    }
    private int countOccupied() {
        int count = 0;
           for (int r = 0; r < ROWS; r++) {
    for (int c = 0; c < COLS; c++) {
        
            if (beds[r][c].isOccupied()) {
                count++;
            }
            }
        }
        return count;
    }    
    
    
    private int countAvailable() {
        return TOTAL_BEDS - countOccupied();
    }
 public int getTotalBeds() { return TOTAL_BEDS; }
    public int getOccupiedBedsCount() { return countOccupied(); }
    public int getAvailableBedsCount() { return countAvailable(); }
 
    /** Returns the percentage of beds currently occupied, rounded to 2 decimal places. */
    public double getOccupancyPercentage() {
        return (countOccupied() * 100.0) / TOTAL_BEDS;
    }
}



class PatientManager {
    private final ArrayList<Patient> patients = new ArrayList<>();
    private final WardManager ward = new WardManager();
    
    Scanner kb = new Scanner(System.in);
    
    
    
    private int nextId = 1; // auto-incrementing patient ID

      // Helper methods
    //display
    private Patient findPatientById(int id) {
        for (Patient p : patients) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
 
    private PatientCategory readCategory() {
        while (true) {
            System.out.print("Enter category (Inpatient/Outpatient/Emergency): ");
            String input = kb.nextLine().trim();
            PatientCategory normalized = normalizeCategory(input);
            if (normalized != null) {
                return normalized;
            }
            System.out.println("Invalid category. Please enter Inpatient, Outpatient, or Emergency.");
        }
    }
 
    private PatientCategory normalizeCategory(String input) {
        try {
            return PatientCategory.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
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

            report generateFullReport = new report(patients, ward);

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
                case 11->generateFullReport.generateFullReport();
                case 12 -> {
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
        System.out.println("11. Report");
        System.out.println("12. Exit");
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
        PatientCategory category = readCategory();
 
        // Create the correct subclass based on the chosen category.
        Patient patient;
        switch (category) {
            case INPATIENT -> patient = new inpatient(nextId, firstName, lastName, age, gender, medicalCondition, category);
            case OUTPATIENT -> patient = new outPatient(nextId, firstName, lastName, age, gender, medicalCondition, category);
            case EMERGENCY -> patient = new emergency(nextId, firstName, lastName, age, gender, medicalCondition, category);
            default -> throw new IllegalStateException("Unexpected category: " + category);
        }
 
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
           System.out.println(patient.displayDetails());
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
        System.out.println(patient.displayDetails());
        System.out.println("Leave a field blank to keep the current value.");
        System.out.println("Note: patient category cannot be changed after registration.");

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

                System.out.println("Patient details updated successfully.");
        System.out.println("Updated details:");
        System.out.println(patient.displayDetails());

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
            if (patient instanceof inpatient && ((inpatient) patient).getBedNumber() != null){
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

          if (!(patient instanceof inpatient)) {
            System.out.println("Bed allocation failed: only Inpatients can be allocated a bed. "
                    + "This patient's category is: " + patient.getCategory());
            return;
        }

        int result = ward.allocateBed(patient);
        if (result == -2) {
            Integer bedNumber = ((inpatient) patient).getBedNumber();
            System.out.println("This patient already has bed number " + bedNumber + " assigned.");
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

         if (!(patient instanceof inpatient) || ((inpatient) patient).getBedNumber() == null) {
            System.out.println("This patient does not currently occupy a bed.");
            return;
        }

      int bedNumber = ((inpatient) patient).getBedNumber();
        boolean released = ward.releaseBedForPatient(patient);
        if (released) {
            System.out.println("Bed " + bedNumber + " has been released. Patient "
                    + patient.getFirstName() + " " + patient.getLastName() + " discharged from ward.");
        } else {
            System.out.println("Failed to release bed.");
        }
    }
    }

   
  
    