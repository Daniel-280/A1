import com.mycompany.a1_4.PatientManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
 


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
 
import static org.junit.jupiter.api.Assertions.*;
 

class PatientManagementSystemTest {
 
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
 
    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
 
    /** Feeds the given text as simulated keyboard input and runs the app until it exits. */
    private String runApp(String simulatedInput) {
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));
 
        PatientManager manager = new PatientManager();
        manager.run();
 
        return captured.toString(StandardCharsets.UTF_8);
    }
 
   
    // Register a patient
   
    @Test
    void testRegisterPatient() {
        String input = String.join("\n",
                "1", "John", "Smith", "30", "Male", "Flu", "Outpatient",
                "12"
        ) + "\n";
 
        String output = runApp(input);
 
        assertTrue(output.contains("Patient registered successfully with ID: 1"));
    }

    // Search for a patient
   
    @Test
    void testSearchPatient() {
        String input = String.join("\n",
                "1", "Jane", "Doe", "25", "Female", "Asthma", "Emergency",
                "2", "1",
                "12"
        ) + "\n";
 
        String output = runApp(input);
 
        assertTrue(output.contains("Patient found:"));
        assertTrue(output.contains("Doe"));
        assertTrue(output.contains("Asthma"));
    }
 
    
    // Update patient details
    
    @Test
    void testUpdatePatientDetails() {
        String input = String.join("\n",
                "1", "Alex", "Brown", "40", "Male", "Diabetes", "Outpatient",
                "3", "1", "Alexander", "", "41", "", "Type 2 Diabetes",
                "12"
        ) + "\n";
 
        String output = runApp(input);
 
        assertTrue(output.contains("Patient details updated successfully."));
        assertTrue(output.contains("Alexander Brown")); // first name changed, surname kept
        assertTrue(output.contains("Age: 41"));
        assertTrue(output.contains("Type 2 Diabetes"));
    }
 
 
    // Delete a patient
  
    @Test
    void testDeletePatient() {
        String input = String.join("\n",
                "1", "Sam", "Lee", "50", "Male", "Hypertension", "Outpatient",
                "4", "1", "y",
                "2", "1",
                "12"
        ) + "\n";
 
        String output = runApp(input);
 
        assertTrue(output.contains("Patient deleted successfully."));
        assertTrue(output.contains("No patient found with ID: 1"));
    }
 
    
    // Allocate a bed

    @Test
    void testAllocateBed() {
        String input = String.join("\n",
                "1", "Mary", "Jones", "60", "Female", "Pneumonia", "Inpatient",
                "6", "1",
                "12"
        ) + "\n";
 
        String output = runApp(input);
 
        assertTrue(output.contains("Bed 1 successfully allocated to patient Mary Jones (ID: 1)."));
    }
 
    
    // Release a bed
 
    @Test
    void testReleaseBed() {
        String input = String.join("\n",
                "1", "Tom", "White", "45", "Male", "Broken Arm", "Inpatient",
                "6", "1",
                "7", "1",
                "12"
        ) + "\n";
 
        String output = runApp(input);
 
        assertTrue(output.contains("Bed 1 has been released. Patient Tom White discharged from ward."));
    }
 
 
    @Test
    void testDuplicatePatientIdsAreNeverIssued() {
        String input = String.join("\n",
                "1", "Anna", "Green", "33", "Female", "Migraine", "Outpatient",
                "1", "Bob", "Black", "29", "Male", "Cold", "Emergency",
                "12"
        ) + "\n";
 
        String output = runApp(input);
 
        assertTrue(output.contains("Patient registered successfully with ID: 1"));
        assertTrue(output.contains("Patient registered successfully with ID: 2"));
    }
 
 
    // Prevent allocating an already-occupied bed

    @Test
    void testCannotAllocateAnAlreadyOccupiedBed() {
        String input = String.join("\n",
                "1", "Kate", "Adams", "28", "Female", "Appendicitis", "Inpatient",
                "1", "Leo", "Adams", "32", "Male", "Fracture", "Inpatient",
                "6", "1",   // bed 1 -> Kate
                "6", "2",   // bed 2 -> Leo (must not reuse bed 1)
                "6", "1",   // try to allocate Kate a bed again -> already has one
                "12"
        ) + "\n";
 
        String output = runApp(input);
 
        assertTrue(output.contains("Bed 1 successfully allocated to patient Kate Adams (ID: 1)."));
        assertTrue(output.contains("Bed 2 successfully allocated to patient Leo Adams (ID: 2)."));
        assertTrue(output.contains("This patient already has bed number 1 assigned."));
    }
 
   
    // Prevent bed allocation when all beds are occupied
  
    @Test
    void testCannotAllocateBedWhenWardIsFull() {
        StringBuilder input = new StringBuilder();
 
        // Register 20 inpatients.
        for (int i = 1; i <= 20; i++) {
            input.append("1\n")
                 .append("First").append(i).append("\n")
                 .append("Last").append(i).append("\n")
                 .append("30\n")
                 .append("Male\n")
                 .append("Condition\n")
                 .append("Inpatient\n");
        }
        // Allocate a bed to each of the 20 inpatients (IDs 1-20).
        for (int i = 1; i <= 20; i++) {
            input.append("6\n").append(i).append("\n");
        }
        // 21st allocation attempt: the ward-full check fires before asking
        // for a patient ID, so no further ID is needed here.
        input.append("6\n");
        input.append("12\n");
 
        String output = runApp(input.toString());
 
        long successfulAllocations = output.lines()
                .filter(line -> line.contains("successfully allocated"))
                .count();
 
        assertEquals(20, successfulAllocations);
        assertTrue(output.contains("Cannot allocate a bed: the ward is full (0 beds available)."));
    }
 

  
    }
