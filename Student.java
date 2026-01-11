import java.util.List;
import java.util.ArrayList;

/**
 * Represents a {@code student} who can rent spell books.
 * Each student is assigned a unique ID and maintains a record of current and past rentals.
 */
public class Student {
  private static int nextStudentId = 100000;
  private int number;
  private String name;
  private List<Integer> currentSpellbooks;
  private List<Integer> history;

  /**
   * Constructs a new {@code Student} with a unique student ID and the given name.
   *
   * @param name the name of the student
   */
  public Student(String name){
    this.number = nextStudentId++;
    this.name = name;
    this.currentSpellbooks = new ArrayList<>();
    this.history = new ArrayList<>();
  }

  public int getNumber() {
    return number;
  }

  public String getName() {
    return name;
  }

  public List<Integer> getCurrentSpellbooks() {
    return currentSpellbooks;
  }

  public List<Integer> getHistory() {
    return history;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Adds a spellbook to the list of currently rented books.
   * @param serialNumber the serial number of the spell book
   */
  public void addToCurrentSpellbooks(int serialNumber){
    currentSpellbooks.add(serialNumber);
  }

  /**
   * Adds a spell book to the rental history.
   *
   * @param serialNumber the serial number of the spell book
   */
  public void addToHistory(int serialNumber){
    history.add(serialNumber);
  }

  public void clearCurrentSpellbooks(){
    currentSpellbooks.clear();
  }

  /**
   * Removes a spell book from the list of currently rented books.
   *
   * @param serialNumber the serial number of the spell book to remove
   */
  public void removeSpellbook(int serialNumber){
    currentSpellbooks.remove(Integer.valueOf(serialNumber));
  }

  /**
   * Resets the student ID counter to its initial value between testcases.
   */
  public static void resetStudentId() {
    nextStudentId = 100000;
  }

}

