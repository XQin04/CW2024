## **README.md**

### **1. GitHub Repository**
[GitHub Repository](https://github.com/your-repository-link)

---

### **2. Compilation Instructions**
#### **Compilation Instructions**
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repository-link.git
   ```
2. Navigate to the project directory:
   ```bash
   cd CW2024
   ```
3. Use Maven to clean, compile, and run the project:
   ```bash
   mvn clean javafx:run
   ```
4. Ensure you have JDK 19 or later and JavaFX dependencies installed.

---

### **3. Implemented and Working Properly**
#### **Implemented and Working Properly**
- **Main Menu**:
    - Features background music and settings (e.g., muting sound effects).
- **Pause/Resume Functionality**:
    - Allows players to pause and resume gameplay.
- **Power-Up Integration**:
    - Collect power-ups to gain temporary advantages like enhanced firepower.
- **Collision Management**:
    - Accurate collision detection between projectiles, enemies, and the player.
- **Spreadshot Mechanic**:
    - Fires three projectiles simultaneously upon activation.
- **Level Transition**:
    - Smooth transitions between levels, including the appearance of a boss.

---

### **4. Implemented but Not Working Properly**
#### **Implemented but Not Working Properly**
- **Boss Projectile Spread**:
    - Explosion and fragments spread correctly but sometimes collide with other actors unexpectedly.
- **SoundManager**:
    - Background music may not stop during rapid transitions between menus.

---

### **5. Features Not Implemented**
#### **Features Not Implemented**
- **Online Multiplayer**:
    - Due to time constraints, multiplayer mode was excluded.
- **AI Pathfinding for Enemies**:
    - Implementing advanced pathfinding algorithms proved to be complex within the timeframe.

---

### **6. New Java Classes**
#### **New Java Classes**
1. **LevelThree**: Represents the third level of the game, managing its unique enemies, challenges, and transitions.  
   *Location: `com.example.demo.levels`*

2. **CollisionManager**: Handles collision detection and responses for actors such as projectiles, enemies, and the player.  
   *Location: `com.example.demo.managers`*

3. **EnemyManager**: Manages the spawning and behavior of enemies in the game.  
   *Location: `com.example.demo.managers`*

4. **GameStateManager**: Controls the current state of the game, such as playing, paused, game over, and win states, and notifies observers about state changes.  
   *Location: `com.example.demo.managers`*

5. **InputHandler**: Manages and processes user input for controlling the player’s actions in the game.  
   *Location: `com.example.demo.managers`*

6. **PowerUpManager**: Handles the spawning and activation of power-ups to provide temporary boosts to the player.  
   *Location: `com.example.demo.managers`*

7. **ProjectileManager**: Manages the projectiles fired by the player and enemies, including tracking their positions and behaviors.  
   *Location: `com.example.demo.managers`*

8. **SoundManager**: Handles sound effects and background music for the game, including muting options and specific event sounds.  
   *Location: `com.example.demo.managers`*

9. **Observer**: An interface for implementing the Observer design pattern, allowing objects to be notified of changes in observed subjects.  
   *Location: `com.example.demo.observer`*

10. **PowerUp**: Represents a generic power-up object that players can collect to gain temporary advantages.  
    *Location: `com.example.demo.powerups`*

11. **SpreadshotPowerUp**: A specialized power-up that activates a spread-shot mechanic, enabling the player to fire multiple projectiles in one shot.  
    *Location: `com.example.demo.powerups`*

12. **EndGameMenu**: Manages the UI for the end-game menu, allowing players to restart or exit after the game ends.  
    *Location: `com.example.demo.ui.menus`*

13. **MainMenu**: Represents the main menu interface, including options to start the game, view settings, or exit.  
    *Location: `com.example.demo.ui.menus`*

14. **PauseMenu**: Provides the pause menu interface, allowing players to resume, navigate to the main menu, or exit the game.  
    *Location: `com.example.demo.ui.menus`*

15. **UIManager**: Handles the overall UI elements, such as menus and buttons, and responds to game state changes using the Observer pattern.  
    *Location: `com.example.demo.ui`*

---

### **7. Modified Java Classes**
#### **Modified Java Classes**
1. **LevelParent**:
    - **Changes Made**: Refactored into SRP classes. Added pause functionality, collision management, and enhanced power-up integration.
    - **Reason**: Simplified codebase for modularity and maintainability.

2. **UserPlane**:
    - **Changes Made**: Added horizontal movement capabilities and enhanced firepower mechanics, including the spreadshot feature.
    - **Reason**: Improved gameplay mechanics and added diversity to player abilities.

3. **Boss**:
    - **Changes Made**: Introduced custom hitbox logic for accurate collision detection and implemented glowing shield effects using DropShadow.
    - **Reason**: Enhanced boss interactions and improved visual aesthetics.

4. **Main**:
    - **Changes Made**: Added `startGame` method for launching the game and modularized the initialization of menus.
    - **Reason**: Improved code structure and enabled separation of menu and game logic.

5. **GameOverImage**:
    - **Changes Made**: Adjusted image size and hard-coded position.
    - **Reason**: Improved UI clarity and player feedback during the game over sequence.

---

### **8. Unexpected Problems**
#### **Unexpected Problems**
1. **Mockito Mocking Errors**:
    - **Issue**: Encountered issues mocking JavaFX classes with Mockito due to module restrictions.
    - **Resolution**: Switched to using dummy classes for testing and avoided direct mocking of restricted classes.

2. **JavaFX Scene Graph**:
    - **Issue**: Difficulty updating the scene graph dynamically during level transitions.
    - **Resolution**: Resolved by clearing and rebuilding the scene graph for smoother transitions.

---

