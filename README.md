# Hogwarts Archive

A command line Java application for managing student accounts and spellbook records at Hogwarts Archive.

To run the program:

```bash
javac *.java  # compiles all Java source files into .class files
java Archive  # Archive is the entry point file
```

**Please note:** A Java Development Kit (JDK) is required to compile and run this program: https://www.oracle.com/java/technologies/downloads/

## Overview

The Hogwarts Archive system allows a user to:

- Manage student accounts
- Catalogue spellbooks individually or via CSV collections
- Rent and return spellbooks
- Track detailed rental histories for both students and spellbooks
- Query spellbooks by type, inventor, availability, and more
- Persist archive data by saving collections to CSV files

All interaction is performed via **standard input/output** in a command-line environment.

## Technologies Used

- **Language:** Java  
- **Paradigm:** Object-Oriented Programming (OOP)  
- **Input/Output:** Standard I/O (Command Line)  
- **Data Storage:** CSV files  

## Object-Oriented Design

This project was designed with a strong emphasis on Object-Oriented Programming principles. The 500 words document `Report.pdf` states how this was implemented in detail.

### Encapsulation
- Core entities such as `Student` and `Spellbook` encapsulate their own data and behaviour  
- Internal state (e.g. rental history, availability) is accessed and modified only through methods  

### Abstraction
- High-level archive operations are separated from low-level data handling  
- Clear abstractions for archive actions such as renting, returning, and listing spellbooks  

### Inheritance & Polymorphism
- Shared behaviour is structured to reduce duplication  
- Design supports future extensibility (e.g. new item types or archive commands)  

### Separation of Concerns
- Command parsing, data management, and output formatting are logically separated  
- Improves readability, maintainability, and testing  

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

## Testing

- Comprehensive test cases included  
- Each command is tested with multiple scenarios  
- Input (`.in`) and output (`.out`) files verify correctness and edge cases

To run all testcases, execute: 
```bash
./run_tests.sh
```

## Example Commands

The instruction `COMMANDS` can be used for a comprehensive list of all commands.

```bash
ADD STUDENT Hermione Granger
ADD COLLECTION spellbooks.csv
RENT 100000 111111
LIST ALL LONG
STUDENT HISTORY 100000
EXIT
```