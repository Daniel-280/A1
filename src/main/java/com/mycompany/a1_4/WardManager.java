
package com.mycompany.a1_4;

//bro code: java 2D arrays
//https://www.youtube.com/watch?v=alwukGslBG8
public class WardManager {
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
