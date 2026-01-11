import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;

// Please note: wildcard imports avoided to follow Google Java Guide: https://google.github.io/styleguide/javaguide.html

/**
 * Handles user commands for managing students and spellbooks in the Hogwarts Archive system.
 * Supports operations such as listing, searching, renting, relinquishing, and saving data.
 */
public class CommandHandler {
  private Map<Integer, Student> studentMap;
  private Map<Integer, SpellBook> spellbookMap;
  private CommonErrorChecker errCheck;
  private static final int NOT_RENTED = -1;

  public CommandHandler(){
    studentMap = new HashMap<>();
    spellbookMap = new HashMap<>();
    errCheck = new CommonErrorChecker(studentMap, spellbookMap);
    Student.resetStudentId();  // Resets studentID for each Ed test case
  }

  /**
   * Parses and executes a user command.
   *
   * @param userInput the raw command string entered by the user
   * @return {@code true} if the command signals to exit the program, otherwise {@code false}
   */
  public boolean handleCommand(String userInput) {
    String[] userInputParts = userInput.split(" ");
    String mainCommand = userInputParts[0].toUpperCase();

    switch (mainCommand) {
      case "EXIT" -> {
        System.out.println("Ending Archive process.");
        return true;
      }
      case "COMMANDS" -> printHelpString();
      case "LIST" -> manageListCommand(userInputParts);
      case "NUMBER" ->  manageNumberCopies();
      case "TYPE" -> searchSpellbooksByAttribute(SpellBook::getType, userInputParts);  // Passes in extractor
      case "INVENTOR" -> searchSpellbooksByAttribute(SpellBook::getInventor, userInputParts);
      case "SPELLBOOK" -> {
        if (userInputParts[1].equalsIgnoreCase("HISTORY")){
          displaySpellbookHistory(userInputParts);
        } else {
          displaySpellbook(userInputParts);
        }
      }
      case "STUDENT" -> manageStudent(userInputParts);
      case "RENT" -> rentSpellbook(userInputParts);
      case "RELINQUISH" -> {
        if(errCheck.checkEmptyStudents()) break;

        if (userInputParts[1].equalsIgnoreCase("ALL")){
          relinquishAll(userInputParts);
        } else {
          relinquish(userInputParts);
        }
      }
      case "ADD" -> manageAddCommands(userInputParts);
      case "SAVE" -> saveToFile(userInputParts);
      case "COMMON" -> printCommon(userInputParts);
    }
    return false;
  }

  //  -- Main functions processing command logic --

  private void manageListCommand(String[] userInputParts) {
    if (errCheck.checkEmptySpellbooks()) return;

    boolean longPresent = userInputParts.length >= 3 && userInputParts[2].equalsIgnoreCase("LONG");
    String listCommandType = userInputParts[1].toUpperCase();  // ALL, AVAILABLE, TYPES, INVENTORS

    // Handles all types of commands which start with LIST
    switch (listCommandType) {
      case "ALL" -> printSpellbooks(spellbookMap.values(), longPresent);
      case "AVAILABLE" -> {
        List<SpellBook> availableSpellbooks = new ArrayList<>();
        for (SpellBook sb : spellbookMap.values()) {
          if (sb.getStudentRenting() == NOT_RENTED) {  // Checks only for available spellbooks
            availableSpellbooks.add(sb);
          }
        }
        printSpellbooks(availableSpellbooks, longPresent);
      }
      case "TYPES" -> printSpellbookUniqueAttributes(SpellBook::getType);
      case "INVENTORS" -> printSpellbookUniqueAttributes(SpellBook::getInventor);
    }
  }

  private void manageNumberCopies(){
    if (errCheck.checkEmptySpellbooks()) return;

    Map<String, Integer> spellbookCountHashMap = new HashMap<>();

    // Count occurrences of each copy and store in hashmap
    for (SpellBook sb : spellbookMap.values()){
      if (spellbookCountHashMap.containsKey(sb.getPrintableForm())) {
        spellbookCountHashMap.put(sb.getPrintableForm(), spellbookCountHashMap.get(sb.getPrintableForm()) + 1);
      } else {
        spellbookCountHashMap.put(sb.getPrintableForm(), 1);
      }
    }

    List<Map.Entry<String, Integer>> sortedSpellbookEntries = new ArrayList<>(spellbookCountHashMap.entrySet());
    sortedSpellbookEntries.sort(Map.Entry.comparingByKey());  // Sort output by spellbook title in list of maps

    // Print entries in hashmap nicely
    for (Map.Entry<String, Integer> numCopiesEntry : sortedSpellbookEntries){
      System.out.println(numCopiesEntry.getKey() + ": " + numCopiesEntry.getValue());
    }
  }

  private void searchSpellbooksByAttribute(Function<SpellBook, String> extractor, String[] userInputParts){
    if (errCheck.checkEmptySpellbooks()) return;

    // Ensures that multi-word search items are accounted for
    String searchItem = String.join(" ", Arrays.copyOfRange(userInputParts, 1, userInputParts.length));

    List<SpellBook> matchingSpellbooks = new ArrayList<>();

    // If spellbook's specified attribute (type or inventor) matches the search item, add to matching list
    for (SpellBook sb : spellbookMap.values()){
      if (extractor.apply(sb).equalsIgnoreCase(searchItem)){
        matchingSpellbooks.add(sb);
      }
    }

    if (matchingSpellbooks.isEmpty()){
      if (userInputParts[0].equalsIgnoreCase("TYPE")){
        System.out.println("No spellbooks with type " + searchItem + ".");
      } else {
        System.out.println("No spellbooks by " + searchItem + ".");
      }
      return;
    }

    printSpellbooks(matchingSpellbooks, false);  // Print matching spellbooks in short form
  }

  private void displaySpellbookHistory(String[] userInputParts){
    int serialNumber = Integer.parseInt(userInputParts[2]);
    SpellBook sb = spellbookMap.get(serialNumber);

    if (errCheck.isMissingSpellbook(serialNumber)) return;

    List<Integer> spellbookHistory = new ArrayList<>(sb.getHistory());

    if (spellbookHistory.isEmpty()){
      System.out.println("No rental history.");
      return;
    }

    for (int studentNumber : spellbookHistory){
      System.out.println(studentNumber);
    }
  }

  private void displaySpellbook(String[] userInputParts){
    if(errCheck.checkEmptySpellbooks()) return;

    boolean longPresent = userInputParts.length >= 3 && userInputParts[2].equalsIgnoreCase("LONG");
    int serialNumber = Integer.parseInt(userInputParts[1]);
    SpellBook sb = spellbookMap.get(serialNumber);

    if (errCheck.isMissingSpellbook(serialNumber)) return;

    // Either print short or long string based on longPresent
    System.out.println(longPresent ? serialNumber + ": " + sb.getPrintableForm(longPresent) + "\n" +
            sb.getRentingStatus() : sb.getPrintableForm(longPresent));
  }

  private void manageStudent(String[] userInputParts){
    if (errCheck.checkEmptyStudents()) return;

    // Order of if-statement checks is STUDENT SPELLBOOKS, STUDENT HISTORY and lastly STUDENT commands
    if (userInputParts[1].equalsIgnoreCase("SPELLBOOKS")){
      int studentNumber = Integer.parseInt(userInputParts[2]);
      if (errCheck.isMissingStudent(studentNumber)) return;

      Student student = studentMap.get(studentNumber);

      List<Integer> sbSerialNumbersArray = student.getCurrentSpellbooks();
      if (sbSerialNumbersArray.isEmpty()){
        System.out.println("Student not currently renting.");
        return;
      }

      for (int spellBookSerialNumber : sbSerialNumbersArray){
        System.out.println(spellbookMap.get(spellBookSerialNumber).getPrintableForm());
      }

    } else if (userInputParts[1].equalsIgnoreCase("HISTORY")){
      int studentNumber = Integer.parseInt(userInputParts[2]);
      if (errCheck.isMissingStudent(studentNumber)) return;

      Student student = studentMap.get(studentNumber);
      List<Integer> studentHistory = new ArrayList<>(student.getHistory());

      if (studentHistory.isEmpty()) {
        System.out.println("No rental history for student.");
        return;
      }

      for (int spellBookSerialNumber : studentHistory){
        System.out.println(spellbookMap.get(spellBookSerialNumber).getPrintableForm());
      }

    } else {
      int studentNumber = Integer.parseInt(userInputParts[1]);
      if (errCheck.isMissingStudent(studentNumber)) return;

      System.out.println(studentNumber + ": " + studentMap.get(studentNumber).getName());
    }
  }

  private void rentSpellbook(String[] userInputParts){
    int studentNumber = Integer.parseInt(userInputParts[1]);
    int serialNumber = Integer.parseInt(userInputParts[2]);

    if (errCheck.checkEmptyStudents()) return;
    if (errCheck.checkEmptySpellbooks()) return;

    if (errCheck.isMissingStudent(studentNumber)) return;
    if (errCheck.isMissingSpellbook(serialNumber)) return;

    Student student = studentMap.get(studentNumber);
    SpellBook spellbook = spellbookMap.get(serialNumber);

    if (spellbook.getStudentRenting() != NOT_RENTED){
      System.out.println("Spellbook is currently unavailable.");
      return;
    }

    spellbook.setStudentRenting(studentNumber);
    student.addToCurrentSpellbooks(serialNumber);

    System.out.println("Success.");
  }

  private void relinquishAll(String[] userInputParts) {
    int studentNumber = Integer.parseInt(userInputParts[2]);

    if (errCheck.isMissingStudent(studentNumber)) return;

    Student student = studentMap.get(studentNumber);
    List<Integer> studentCurrentSpellbooksArray = new ArrayList<>(student.getCurrentSpellbooks());

    for (int serialNumber : studentCurrentSpellbooksArray) {
      relinquishHelperMethod(studentNumber, serialNumber);  // Relinquish every spellbook owned by student
    }

    student.clearCurrentSpellbooks();
    System.out.println("Success.");
  }


  private void relinquish(String[] userInputParts) {
    if (errCheck.checkEmptyStudents()) return;
    if (errCheck.checkEmptySpellbooks()) return;

    int studentNumber = Integer.parseInt(userInputParts[1]);
    int serialNumber = Integer.parseInt(userInputParts[2]);

    boolean success = relinquishHelperMethod(studentNumber, serialNumber);

    if (success) {
      System.out.println("Success.");
    }
  }


  private void manageAddCommands(String[] userInputParts){
    if (userInputParts[1].equalsIgnoreCase("STUDENT")) {

      // Accepts student name with spaces
      String fullName = String.join(" ", Arrays.copyOfRange(userInputParts, 2, userInputParts.length));

      Student student = new Student(fullName);
      studentMap.put(student.getNumber(), student); // Add studentNumber as key, and object as value
      System.out.println("Success.");
    }

    else {
      String addCommandType = userInputParts[1].toUpperCase();  // SPELLBOOK or COLLECTION

      String filePath = userInputParts[2];
      File fileObj = new File(filePath);

      boolean spellbookAdded = false; // Used by ADD SPELLBOOK command
      int sbCount = 0;  // Used by ADD COLLECTION command

      try (Scanner fileScanner = new Scanner(fileObj)){
        if (fileScanner.hasNextLine()){
          fileScanner.nextLine();  // skip header line in csv file
        }

        while (fileScanner.hasNextLine()) {
          if (addCommandType.equals("SPELLBOOK")){
            int specifiedSerialNumber = Integer.parseInt(userInputParts[3]);
            if (spellbookMap.containsKey(specifiedSerialNumber)) {  // Checking if spellbook has already been added
              System.out.println("Spellbook already exists in system.");
              return;
            }

            String fileNextLine = fileScanner.nextLine();
            SpellBook sb = parseSpellBookFromLine(fileNextLine);  // Extract line data into an object

            // Check if file entry matches provided spellbook serial number in command
            if (sb.getSerialNumber() == specifiedSerialNumber) {
              spellbookMap.put(specifiedSerialNumber, sb);
              System.out.println("Successfully added: " + sb.getPrintableForm() + ".");
              spellbookAdded = true;
              break;
            }
          } else { // ADD COLLECTION command
            String fileNextLine = fileScanner.nextLine();
            SpellBook sb = parseSpellBookFromLine(fileNextLine);
            int serialNumber = sb.getSerialNumber();

            if (spellbookMap.containsKey(serialNumber)) {
              continue; // Skip duplicate to avoid incrementing spellbook count
            }

            spellbookMap.put(serialNumber, sb);
            sbCount++;
          }


        }

        if (addCommandType.equals("SPELLBOOK")){
          if (!spellbookAdded){
            System.out.println("No such spellbook in file.");
          }
        } else {
          // ADD COLLECTION
          if (sbCount == 0){
            System.out.println("No spellbooks have been added to the system.");
          } else {
            System.out.println(sbCount + " spellbooks successfully added.");
          }
        }

      } catch (FileNotFoundException e) {  // Unique error for each command
        if (addCommandType.equals("SPELLBOOK")){
          System.out.println("No such file.");
        } else {
          System.out.println("No such collection.");
        }
      }
    }
  }

  private void saveToFile(String[] userInputParts){
    if (errCheck.checkEmptySpellbooks()) return;

    String fileName = userInputParts[2];
    try (FileWriter writer = new FileWriter(fileName)) {
      writer.append("serialNumber,title,inventor,type\n");  // header line

      List<SpellBook> sortedSpellbooks = new ArrayList<>(spellbookMap.values());
      sortedSpellbooks.sort(Comparator.comparingInt(SpellBook::getSerialNumber));  //Sort entries by serial number

      for (SpellBook sb : sortedSpellbooks){
        writer.append(String.valueOf(sb.getSerialNumber())).append(",")
                .append(sb.getTitle()).append(",")
                .append(sb.getInventor()).append(",")
                .append(sb.getType()).append("\n");
      }

      System.out.println("Success.");
    } catch (IOException _) {}  // No error messages necessary for this exception
  }

  private void printCommon(String[] userInputParts) {
    if (errCheck.checkEmptyStudents()) return;
    if (errCheck.checkEmptySpellbooks()) return;

    List<Set<String>> studentHistoriesList = new ArrayList<>();
    Set<Integer> alreadyAddedStudents = new HashSet<>();  // Used to check if a student is added multiple times

    if (userInputParts.length < 3){
      return;  // Not enough students provided
    }

    // Store each student's rental history into a list
    for (int i = 1; i < userInputParts.length; i++) {
      String stringStudentNumber = userInputParts[i];
      if (!stringStudentNumber.matches("-?\\d+")){  // Make sure provided studentNumber is an integer
        System.out.println("No such student in system.");
        return;
      }

      int studentNumber = Integer.parseInt(stringStudentNumber);
      if (!alreadyAddedStudents.add(studentNumber)) {
        System.out.println("Duplicate students provided.");
        return;
      }
      if (errCheck.isMissingStudent(studentNumber)) return;

      Student student = studentMap.get(studentNumber);
      Set<String> studentHistory = new HashSet<>();
      for (int serialNumber : student.getHistory()) {
        SpellBook sb = spellbookMap.get(serialNumber);
        studentHistory.add(sb.getPrintableForm());
      }

      studentHistoriesList.add(studentHistory);
    }

    // Find common spellbooks
    Set<String> commonSpellbooks = null;

    for (Set<String> studentHistory : studentHistoriesList) {
      if (commonSpellbooks == null) {  // For first student, take whole history
        commonSpellbooks = new TreeSet<>(studentHistory);  // Tree set used to maintain alphabetical order
      } else {
        commonSpellbooks.retainAll(studentHistory);  // Keep only shared items between next student's history
      }
    }

    if (commonSpellbooks.isEmpty()){
      System.out.println("No common spellbooks.");
      return;
    }

    for (String spellbook : commonSpellbooks){
      System.out.println(spellbook);
    }
  }

  private void printHelpString(){
    String helpString = """
            EXIT ends the archive process
            COMMANDS outputs this help string
            
            LIST ALL [LONG] outputs either the short or long string for all spellbooks
            LIST AVAILABLE [LONG] outputs either the short or long string for all available spellbooks
            NUMBER COPIES outputs the number of copies of each spellbook
            LIST TYPES outputs the name of every type in the system
            LIST INVENTORS outputs the name of every inventor in the system
            
            TYPE <type> outputs the short string of every spellbook with the specified type
            INVENTOR <inventor> outputs the short string of every spellbook by the specified inventor
            
            SPELLBOOK <serialNumber> [LONG] outputs either the short or long string for the specified spellbook
            SPELLBOOK HISTORY <serialNumber> outputs the rental history of the specified spellbook
            
            STUDENT <studentNumber> outputs the information of the specified student
            STUDENT SPELLBOOKS <studentNumber> outputs the spellbooks currently rented by the specified student
            STUDENT HISTORY <studentNumber> outputs the rental history of the specified student
            
            RENT <studentNumber> <serialNumber> loans out the specified spellbook to the given student
            RELINQUISH <studentNumber> <serialNumber> returns the specified spellbook from the student
            RELINQUISH ALL <studentNumber> returns all spellbooks rented by the specified student
            
            ADD STUDENT <name> adds a student to the system
            ADD SPELLBOOK <filename> <serialNumber> adds a spellbook to the system
            
            ADD COLLECTION <filename> adds a collection of spellbooks to the system
            SAVE COLLECTION <filename> saves the system to a csv file
            
            COMMON <studentNumber1> <studentNumber2> ... outputs the common spellbooks in students' history""";

    System.out.println(helpString);
  }

  // -- Helper methods --

  private void printSpellbooks(Collection<SpellBook> books, boolean longPresent) {
    List<SpellBook> booksList = new ArrayList<>(books);
    Collections.sort(booksList, Comparator.comparing(SpellBook::getSerialNumber));  // Sort by serialNumber

    if (booksList.isEmpty()){
      System.out.println("No spellbooks available.");
    }

    // Prints differently based on whether [LONG] has been provided by user
    for (int i = 0; i < books.size(); i++) {
      SpellBook sb = booksList.get(i);
      System.out.println(longPresent ? sb.getSerialNumber() + ": " + sb.getPrintableForm(longPresent) + "\n" +
              sb.getRentingStatus(): sb.getPrintableForm(longPresent));

      if (books.size() > 1 && longPresent && i != books.size() - 1){
        System.out.println();  // If there's more than one entry, then print empty lines between each spellbook
      }
    }
  }

  private void printSpellbookUniqueAttributes(Function<SpellBook, String> extractor){
    Set<String> uniqueAttrSet = new HashSet<>();  // Used to avoid repeated values

    for (SpellBook sb : spellbookMap.values()) {
      uniqueAttrSet.add(extractor.apply(sb));  // Applies provided function eg. sb.getType() and adds to hashset
    }

    List<String> sortedAttributesList = new ArrayList<>(uniqueAttrSet);
    Collections.sort(sortedAttributesList);  // Sort alphabetically

    for (String setItem : sortedAttributesList){
      System.out.println(setItem);
    }
  }

  private boolean relinquishHelperMethod(int studentNumber, int serialNumber) {
    if (errCheck.isMissingStudent(studentNumber)) return false;
    if (errCheck.isMissingSpellbook(serialNumber)) return false;

    Student student = studentMap.get(studentNumber);
    SpellBook sb = spellbookMap.get(serialNumber);

    if (errCheck.spellbookNotRentedByStudent(serialNumber, studentNumber)) return false;

    sb.addToHistory(studentNumber);
    sb.setStudentRenting(NOT_RENTED);
    student.addToHistory(sb.getSerialNumber());
    student.removeSpellbook(serialNumber);

    return true;
  }

  private SpellBook parseSpellBookFromLine(String line) {
    String[] lineParts = line.split(",");
    int serialNumber = Integer.parseInt(lineParts[0]);
    String title = lineParts[1];
    String inventor = lineParts[2];
    String type = lineParts[3];

    return new SpellBook(serialNumber, title, inventor, type);
  }
}
