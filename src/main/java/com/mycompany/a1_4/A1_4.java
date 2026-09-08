
package com.mycompany.a1_4;


import java.util.ArrayList;
import java.util.Scanner;

public class A1_4 {

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
                   String gender, String medicalCondition, PatientCategory category) {
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



