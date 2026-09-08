
package com.mycompany.a1_2;


class inpatient extends Patient {
    private static final int DEFAULT_WARD_NUMBER = 1; // the hospital currently has a single ward
 
    private int wardNumber;
    private Integer bedNumber; // null until a bed is allocated
 
    public inpatient(int id, String firstName, String lastName, int age,
                      String gender, String medicalCondition, PatientCategory category) {
        super(id, firstName, lastName, age, gender, medicalCondition, category);
        this.wardNumber = DEFAULT_WARD_NUMBER;
        this.bedNumber = null;
    }
 
    public int getWardNumber() { return wardNumber; }
    public void setWardNumber(int wardNumber) { this.wardNumber = wardNumber; }
 
    public Integer getBedNumber() { return bedNumber; }
    public void setBedNumber(Integer bedNumber) { this.bedNumber = bedNumber; }
 
    @Override
    public String displayDetails() {
        String bedInfo = (bedNumber == null) ? "Not assigned" : String.valueOf(bedNumber);
        return super.displayDetails()
                + " | Ward: " + wardNumber
                + " | Bed: " + bedInfo;
    }
}