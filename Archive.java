import java.util.Scanner;

/**
 * Entry point for the spellbook archive system.
 * Continuously reads user input and delegates command handling to {@code CommandHandler}.
 */
public class Archive {

  /**
   * Starts the command-line interface for the Hogwarts Archive system.
   * Accepts user input until a termination command is issued.
   */
  public static void main(String[] args){
    Scanner userScanner = new Scanner(System.in);
    CommandHandler cmdHandler = new CommandHandler();

    while (true){
      System.out.print("user: ");
      String userInput = userScanner.nextLine();
      boolean shouldExit = cmdHandler.handleCommand(userInput);
      if (shouldExit) break;

      System.out.println();  // 1 line space between each command
    }

  }
}

