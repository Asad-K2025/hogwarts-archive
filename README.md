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

This was my first structured project in Java and assisted in learning Object-Oriented Programming principles. This meant encountering getters and setters for the first time. The 500 words document `Report.pdf` states how the main principles were implemented in detail.

### Testing

Comprehensive test cases are included in the `/tests` folder for multiple scenarios using input (`.in`) and output (`.out`) files.

To run all testcases, execute: 
```bash
./run_tests.sh
```

### Documentation

- A detailed UML (Unified Modeling Language) diagram which lists interaction between classes for this system was also created by me to understand software documentation.

- Javadoc was also written for this program, and is hosted through github pages: https://asad-k2025.github.io/hogwarts-archive/package-summary.html

