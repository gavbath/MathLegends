# Math Legends – Educational GUI Game

A multiplayer arithmetic game built with Java and Swing. This project focuses on Object-Oriented Programming (OOP) principles and provides an interactive platform for practicing mathematical skills.

## Key Features
- **Four Game Modes:** Includes "Make a Wish" (standard), "No Mistakes" (sudden death), "Take Chances" (three lives), and "Time Trial" (countdown timer).
- **Multiplayer Support:** Features a recursive player initialization system to handle dynamic naming for multiple users in a single session.
- **Dynamic Leaderboard:** Implements a real-time performance ranking system utilizing a Bubble Sort algorithm to display winners and statistics.
- **Math Engine:** Randomized problem generation with precision logic for two-decimal floating-point division.

## Technical Concepts Demonstrated
- **Encapsulation:** Utilized private instance variables and public getters/setters within the Player class to ensure data integrity and security.
- **Polymorphism:** Overrode the paintComponent method to render custom mathematical symbols and UI graphics on the main menu.
- **Recursive Logic:** Implemented recursive methods to manage the flow of user input and player creation during the setup phase.
- **State Management:** Managed application states, game timers, and score tracking across multiple panels using CardLayout for seamless transitions.

## Project Structure
The project follows the standard Maven directory structure:
- src/main/java/com/gavbath/mathlegends/ : Contains the Java source files (MathLegends, Player, and Game).
- pom.xml : Maven configuration file for project management and builds.
- nbactions.xml : NetBeans IDE specific configurations.

## How to Run
1. Ensure you have Java Development Kit (JDK) 8 or higher installed.
2. Clone the repository to your local machine:
   git clone https://github.com/gavbath/MathLegends.git
3. Open the project folder in an IDE such as NetBeans, IntelliJ IDEA, or Eclipse.
4. Locate and run the MathLegends.java file to start the application.
