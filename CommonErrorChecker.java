import java.util.Map;

/**
 * Class for validating common errors in system.
 * Checks for missing students, missing spellbooks, and rental mismatches.
 */
public class CommonErrorChecker {
  private Map<Integer, Student> studentMap;
  private Map<Integer, SpellBook> spellbookMap;

  /**
   * Constructs a {@code CommonErrorChecker} with the given student and spellbook maps.
   *
   * @param studentMap    map of student IDs to {@code Student} objects
   * @param spellbookMap  map of spellbook serial numbers to {@code SpellBook} objects
   */
  public CommonErrorChecker(Map<Integer, Student> studentMap, Map<Integer, SpellBook> spellbookMap){
    this.studentMap = studentMap;
    this.spellbookMap = spellbookMap;
  }

  /**
   * Checks if the spellbook map is empty.
   * Prints an error message if no spellbooks are present.
   *
   * @return {@code true} if an error is found, otherwise {@code false}
   */
  public boolean checkEmptySpellbooks(){
    boolean errorsPresent = false;
    if (spellbookMap.isEmpty()){
      System.out.println("No spellbooks in system.");
      errorsPresent = true;
    }
    return errorsPresent;
  }

  /**
   * Checks if the student map is empty.
   * Prints an error message if no students are present.
   *
   * @return {@code true} if an error is found, otherwise {@code false}
   */
  public boolean checkEmptyStudents(){
    boolean errorsPresent = false;
    if (studentMap.isEmpty()){
      System.out.println("No students in system.");
      errorsPresent = true;
    }
    return errorsPresent;
  }

  /**
   * Checks if a student with the given ID exists.
   * Prints an error message if the student is missing.
   *
   * @param studentNumber the student ID to check
   * @return {@code true} if the student is missing, otherwise {@code false}
   */
  public boolean isMissingStudent(int studentNumber) {
    boolean errorsPresent = false;
    if (!studentMap.containsKey(studentNumber)) {
      System.out.println("No such student in system.");
      errorsPresent = true;
    }
    return errorsPresent;
  }

  /**
   * Checks if a spellbook with the given serial number exists.
   * Prints an error message if the spellbook is missing.
   *
   * @param serialNumber the spellbook serial number to check
   * @return {@code true} if the spellbook is missing, otherwise {@code false}
   */
  public boolean isMissingSpellbook(int serialNumber) {
    boolean errorsPresent = false;
    if (!spellbookMap.containsKey(serialNumber)) {
      System.out.println("No such spellbook in system.");
      errorsPresent = true;
    }
    return errorsPresent;
  }

  /**
   * Checks if the specified spellbook is currently rented by the given student.
   * Prints an error message if the rental does not match.
   *
   * @param serialNumber  the spellbook serial number
   * @param studentNumber the student ID
   * @return {@code true} if the rental is invalid, otherwise {@code false}
   */
  public boolean spellbookNotRentedByStudent(int serialNumber, int studentNumber){
    boolean errorsPresent = false;
    if (spellbookMap.get(serialNumber).getStudentRenting() != studentNumber) {
      System.out.println("Unable to return spellbook.");
      errorsPresent = true;
    }
    return errorsPresent;
  }
}
