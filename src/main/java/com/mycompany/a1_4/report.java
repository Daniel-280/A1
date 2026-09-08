
package com.mycompany.a1_4;

import java.util.ArrayList;

public class report {
  private final ArrayList<Patient> patients;
    private final WardManager ward;
 
    public report(ArrayList<Patient> patients, WardManager ward) {
        this.patients = patients;
        this.ward = ward;
    }
 
    /** Prints the full hospital report: patients, bed availability, and occupancy stats. */
    public void generateFullReport() {
        System.out.println("=====================================================");
        System.out.println("                 HOSPITAL FULL REPORT               ");
        System.out.println("=====================================================");
        System.out.println();
 
        printAllPatients();
        System.out.println();
 
        ward.displayAvailableBeds();
        System.out.println();
 
        ward.displayOccupiedBeds();
        System.out.println();
 
        printSummaryStatistics();
 
        System.out.println("=====================================================");
    }
 
    /** Section: all registered patients. Uses polymorphism -- each patient
     *  prints its own displayDetails(), so Inpatients automatically show
     *  their ward/bed info without any special-casing here. */
    private void printAllPatients() {
        System.out.println("--- All Registered Patients ---");
        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        for (Patient p : patients) {
            System.out.println(p.displayDetails());
        }
    }
 
    /** Section: totals and occupancy percentage. */
    private void printSummaryStatistics() {
        System.out.println("--- Summary Statistics ---");
        System.out.println("Total registered patients: " + patients.size());
        System.out.println("Total occupied beds: " + ward.getOccupiedBedsCount() + " / " + ward.getTotalBeds());
        System.out.printf("Ward occupancy: %.2f%%%n", ward.getOccupancyPercentage());
    }
}