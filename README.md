# COMP2042 Coursework

## Table of Contents

1. [GitHub Repository](#github-repository)
2. [Compilation Instructions](#compilation-instructions)
3. [Implemented and Working Properly](#implemented-and-working-properly)
4. [Implemented but Not Working Properly](#implemented-but-not-working-properly)
5. [Features Not Implemented](#features-not-implemented)
6. [New Java Classes](#new-java-classes)
7. [Modified Java Classes](#modified-java-classes)
8. [Unexpected Problems](#unexpected-problems)

## GitHub Repository

Written by: Eng Xiang Qin (20508943)

https://github.com/XQin04/CW2024.git

---

## Compilation Instructions

Follow these steps to compile and run the application:

1. **Clone the repository:**

   ```bash
   git clone https://github.com/XQin04/CW2024.git
   ```

2. **Navigate to the project directory:**

   ```bash
   cd sky-battle
   ```

3. **Ensure JavaFX is configured:**

    - Download and install JavaFX SDK if not already installed.
    - Set the `--module-path` and `--add-modules` flags in your IDE or command-line execution.


4. **Run the application from the command line:**

   ```bash
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.media -jar SkyBattle.jar
   ```

5. **Dependencies:**

   **Check and Set Java SDK Version:**
   - Go to `File > Project Structure > Project`.
   - Ensure the **SDK** is set to Java 19 (Amazon Corretto 19.0.2, as shown in your screenshot).
   - Verify the **Language Level** is compatible with JavaFX (11 or higher).
   - Ensure all required libraries are included in your classpath.

---

## Implemented and Working Properly

### Core Features:

1. **Main Menu:**

    - The `MainMenu` class provides a comprehensive starting interface for the game.
    - Includes methods such as `start()`, `createMainMenuLayout()`, and `createSettingsLayout()` for options like "Start Game," "Settings," "How to Play," and "Exit."
    - The "Settings" option allows players to toggle background music and sound effects using checkboxes, leveraging the `SoundManager` class for state management.
    - "How to Play" presents gameplay instructions using `createHowToPlayLayout()` for objectives and controls.

2. **Pause Menu:**

    - The `PauseMenu` class provides in-game pause functionality through methods like `createStyledButton()` and `configureLayout()`.
    - Features options to "Resume" gameplay, return to the main menu, or exit the application.

3. **End Game Menu:**

    - The `EndGameMenu` class appears at the conclusion of the game (win or loss).
    - Dynamically updates the title to "You Win" or "Game Over" using the `show()` method.
    - Provides options to return to the main menu or exit the game with methods such as `createStyledButton()`.

4. **Player Movement:**

    - The `InputHandler` class enables smooth player control using methods like `handleInput()` for movement in all four cardinal directions (up, down, left, right).
    - Integrates seamlessly with the `UserSuperman` character's movement logic.

5. **Boss Spider Behavior in Level Two:**

    - The `BossSpider` class in LevelTwo uses the `shootProjectile()` method to fire a large projectile.
    - The explosion and fragment behavior are managed using the `BossProjectile` class with methods like `scheduleExplosion()` and `spawnExplodingFragments()`.

6. **Level Three Waves:**

    - The `LevelThree` class introduces waves of `EnemySpider` units using `generateEnemyWaves()`.
    - The final wave features the `BossSpider` as the level's climax, providing a challenging boss fight.

7. **Level Three Power-Ups:**

    - LevelThree incorporates collectible `SpreadshotPowerUp` items.
    - The `activate()` method in the `SpreadshotPowerUp` class grants the player the ability to shoot multiple projectiles simultaneously, enhancing attack capabilities.

---

## Implemented but Not Working Properly

1. **Background Music Mute Issue:**

    - Initially, when background music was muted via the `MainMenu` settings, it would still play when the game was paused and resumed.
    - Added a checker in the `PauseMenu` class using `setMusicMuted()` and `isMusicMuted()` in the `SoundManager` to ensure mute state consistency during transitions.

2. **Spreadshot Power-Up Count:**

    - Collecting multiple `SpreadshotPowerUp` items simultaneously only allowed the user to shoot one spread shot.
    - Modified the `PowerUpManager` and `UserSuperman` classes to include methods like `incrementSpreadshotCount()` and `decrementSpreadshotCount()` to track and apply multiple power-ups.

---

## Features Not Implemented

1. **Online Leaderboard:**

    - Planned to integrate an online leaderboard for players to compare scores.
    - Not implemented due to the time constraints.

2. **Dynamic Weather Effects:**

    - Intended to include dynamic weather effects (e.g., rain, thunder) to enhance game immersion.
    - Omitted due to the complexity of integrating animations and ensuring performance across systems.

---

## New Java Classes

| Class Name             | Description                                                                                  | Location                          |
|------------------------|----------------------------------------------------------------------------------------------|-----------------------------------|
| **LevelThree**         | Represents the third level of the game, managing its unique enemies, challenges, and transitions. | `com.example.demo.levels`        |
| **CollisionManager**   | Handles collision detection and responses for actors such as projectiles, enemies, and the player. | `com.example.demo.managers`      |
| **EnemyManager**       | Manages the spawning and behavior of enemies in the game.                                    | `com.example.demo.managers`      |
| **GameStateManager**   | Controls the current state of the game, such as playing, paused, game over, and win states, and notifies observers about state changes. | `com.example.demo.managers`      |
| **InputHandler**       | Manages and processes user input for controlling the player’s actions in the game.           | `com.example.demo.managers`      |
| **PowerUpManager**     | Handles the spawning and activation of power-ups to provide temporary boosts to the player.  | `com.example.demo.managers`      |
| **ProjectileManager**  | Manages the projectiles fired by the player and enemies, including tracking their positions and behaviors. | `com.example.demo.managers`      |
| **SoundManager**       | Handles sound effects and background music for the game, including muting options and specific event sounds. | `com.example.demo.managers`      |
| **Observer**           | An interface for implementing the Observer design pattern, allowing objects to be notified of changes in observed subjects. | `com.example.demo.observer`      |
| **PowerUp**            | Represents a generic power-up object that players can collect to gain temporary advantages.  | `com.example.demo.powerups`      |
| **SpreadshotPowerUp**  | A specialized power-up that activates a spread-shot mechanic, enabling the player to fire multiple projectiles in one shot. | `com.example.demo.powerups`      |
| **EndGameMenu**        | Manages the UI for the end-game menu, allowing players to restart or exit after the game ends. | `com.example.demo.ui.menus`      |
| **MainMenu**           | Represents the main menu interface, including options to start the game, view settings, or exit. | `com.example.demo.ui.menus`      |
| **PauseMenu**          | Provides the pause menu interface, allowing players to resume, navigate to the main menu, or exit the game. | `com.example.demo.ui.menus`      |
| **UIManager**          | Handles the overall UI elements, such as menus and buttons, and responds to game state changes using the Observer pattern. | `com.example.demo.ui`            |

---

## Modified Java Classes

| Class Name            | Description                                                                                                           | Key Changes                                                                                                                                                                                                 |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **ShieldImage**       | Manages the shield's visual representation.                                                                          | Updated the image path from `"/com/example/demo/images/shield.jpg"` to `"/com/example/demo/images/shield.png"`.                                                                                         |
| **LevelParent**       | Core class for managing level gameplay and transitions.                                                             | Added cleanup logic to `goToNextLevel` for smooth level transitions. Improved collision handling for enemy projectiles and added a pause button with `initializePauseButton()` and `togglePause()`.          |
| **Boss**              | Represents the boss character in the game.                                                                          | Added `getCustomHitbox()` for precise collision detection and implemented a glowing effect using `DropShadow` for shield activation.                                                                        |
| **Main**              | Entry point for the game.                                                                                            | Added `MainMenu` initialization, extracted game launch logic into `startGame()`, and modularized menu handling.                                                                                            |
| **UserSuperman**      | Represents the player's character.                                                                                   | Added methods for horizontal movement (`moveLeft()`, `moveRight()`, `stopHorizontal()`), firepower management, and spread-shot capabilities with `activateOneTimeSpreadshot()`.                             |
| **LevelTwo**          | Manages Level Two gameplay, including transitions.                                                                  | Introduced the `NEXT_LEVEL` constant for transitioning to Level Three.                                                                                                                                     |
| **SpreadshotPowerUp** | Power-up granting the player a one-time spread-shot ability.                                                         | Created a new class for this functionality and integrated with `UserPlane`.                                                                                                                                |
| **SoundManager**      | Handles audio for the game, including sound effects and background music.                                            | Added methods to check and manage sound muting (`isMusicMuted()`, `setSoundEffectsMuted()`) and play specific sound effects for events like power-up collection and game state changes.                     |
| **GameOverImage**     | Displays the game-over image.                                                                                        | Adjusted image size and position for better visibility.                                                                                                                                                    |
| **ProjectileManager** | Handles projectile behavior for both player and enemy projectiles.                                                  | Enhanced logic to manage boss projectiles and added collision detection for spread-shot fragments.                                                                                                         |

---

## Unexpected Problems

1. **Game Crash in Level One:**

    - Encountered a `java.lang.reflect.InvocationTargetException` error when playing Level One due to an incorrect file type for the shield image.
    - Fixed by updating the image file extension from `.jpg` to `.png` in the `ShieldImage` class.

2. **Game Crash on Transition to Level Two:**

    - The game crashed when transitioning to Level Two due to resources not being cleared from Level One.
    - Fixed by adding cleanup code in the `goToNextLevel` method of the `LevelParent` class:
      ```java
      timeline.stop(); // Stop the current game loop.
      root.getChildren().clear(); // Clear all nodes from the scene to release resources.
      ```

3. **Shield Image Not Displaying in Level Two:**

    - The shield image failed to render during Level Two when the boss was shielded.
    - Replaced the shield image with a glowing effect using `DropShadow` to enhance visual clarity and provide a dynamic indicator for the shield's state.
    - The glow effect allows better visibility during gameplay and avoids image overlap issues with other UI elements.


---
