# Hogwarts Archive

A command line CRUD (Create, Read, Update, Delete) application for managing student accounts and spellbook records at Hogwarts Archive.

## Core Features

### Student Management
- Automatically assigned unique student numbers  
- Tracks currently rented spellbooks  
- Maintains a complete rental history  

### Spellbook Management
- Supports multiple copies of spellbooks with unique serial numbers  
- Tracks availability and rental history  
- Allows querying by type and inventor  

### CSV Integration
- Load spellbooks individually or as collections from CSV files  
- Save the current archive state back to a CSV file  

### Robust Command Handling
- Gracefully handles edge cases (empty systems, duplicates, invalid queries)  
- Output strictly matches the required specification  

## Technologies Used

- **Language:** Java  
- **Paradigm:** Object-Oriented Programming (OOP)  
- **Input/Output:** Standard I/O (Command Line)  
- **Data Storage:** CSV files  

## How to run

To run the program:

```bash
javac *.java  # compiles all Java source files into .class files
java Archive  # Archive is the entry point file
```

**Please note:** A Java Development Kit (JDK) is required to compile and run this program: https://www.oracle.com/java/technologies/downloads/

### Example Commands

The instruction `COMMANDS` can be used for a comprehensive list of all commands.

```bash
ADD STUDENT Hermione Granger
ADD COLLECTION spellbooks.csv
RENT 100000 111111
LIST ALL LONG
STUDENT HISTORY 100000
EXIT
```

## Additional Learning Outcomes

### Object-Oriented Design

This project was designed with a strong emphasis on learning Object-Oriented Programming principles. The 500 words document `Report.pdf` states how this was implemented in detail.

### Testing

- Comprehensive test cases included  
- Each command is tested with multiple scenarios  
- Input (`.in`) and output (`.out`) files verify correctness and edge cases

To run all testcases, execute: 
```bash
./run_tests.sh
```

### Documentation

A detailed UML diagram which lists interaction between classes for the operation of this system.