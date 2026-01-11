import java.util.List;
import java.util.ArrayList;

/**
 * The {@code SpellBook} class represents a magical book in a library system.
 * Each spell book has a unique serial number, a title, an inventor, a type,
 * and tracks rental history and current renting status.
 */
public class SpellBook {
  private int serialNumber;
  private String title;
  private String inventor;
  private String type;
  private int studentRenting;
  private List<Integer> history;

  /**
   * Constructs a new {@code SpellBook} with the specified details.
   *
   * @param serialNumber the unique identifier for the spellbook
   * @param title        the title of the spellbook
   * @param inventor     the name of the inventor of the spellbook
   * @param type         the type of spell the book contains
   */

  public SpellBook(int serialNumber, String title, String inventor, String type){
    this.serialNumber = serialNumber;
    this.title = title;
    this.inventor = inventor;
    this.type = type;
    this.studentRenting = -1;  // -1 represents not renting in this system
    this.history = new ArrayList<>();
  }

  public int getSerialNumber() {
    return serialNumber;
  }

  public String getTitle() {
    return title;
  }

  public String getInventor() {
    return inventor;
  }

  public String getType() {
    return type;
  }

  /**
   * Returns the student ID currently renting the spell book.
   *
   * @return the student ID or -1 if not rented
   */
  public int getStudentRenting() {
    return studentRenting;
  }

  /**
   * Returns the rental history of the spell book.
   *
   * @return a list of student IDs who have rented the book
   */
  public List<Integer> getHistory() {
    return history;
  }

  /**
   * Returns a printable string representation of the spell book.
   * Includes title, inventor, and optionally the type.
   *
   * @param longPresent whether to include the type in the output. Optional parameter set to false if not provided.
   * @return a formatted string of the spell book details
   */
  public String getPrintableForm(boolean longPresent) {
    return longPresent ? title + " (" + inventor + ", " + type + ")": title + " (" + inventor + ")";
  }

  public String getPrintableForm(){
    return title + " (" + inventor + ")";
  }

  public String getRentingStatus(){
    if (studentRenting == -1){
      return "Currently available.";
    } else {
      return "Rented by: " + studentRenting + ".";
    }
  }

  public void setSerialNumber(int serialNumber) {
    this.serialNumber = serialNumber;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setInventor(String inventor) {
    this.inventor = inventor;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setStudentRenting(int studentRenting) {
    this.studentRenting = studentRenting;
  }

  /**
   * Adds a student ID to the rental history of the spell book.
   *
   * @param studentNumber the student ID to add
   */
  public void addToHistory(int studentNumber){
    history.add(studentNumber);
  }
}


