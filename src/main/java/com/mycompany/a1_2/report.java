
package com.mycompany.a1_2;

import java.util.ArrayList;

   class report {
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
 
    /** Section: all registered patients. */
    private void printAllPatients() {
        System.out.println("--- All Registered Patients ---");
        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        System.out.printf("%-6s %-12s %-12s %-4s %-8s %-20s %-12s %-4s%n",
                "ID", "FirstName", "LastName", "Age", "Gender", "Condition", "Category", "Bed");
        System.out.println("--------------------------------------------------------------------------------");
        for (Patient p : patients) {
            System.out.println(p);
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
