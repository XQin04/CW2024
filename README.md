### ShieldImage.java Changes

#### Changes Made
- **Corrected Image Path:**
    - Updated the path from `"/com/example/demo/images/shield.jpg"` to `"/com/example/demo/images/shield.png"`.


### LevelParent.java Changes

#### Changes Made
Added the following code in `goToNextLevel` method to allow the boss to appear smoothly and immediately after entering Level Two:
  ```java
  timeline.stop(); // Stop the current game loop.
  root.getChildren().clear(); // Clear all nodes from the scene to release resources.
  ```
#### Collision Handling Enhancements

#### Changes Made

1. **Refined `handleEnemyProjectileCollisions` Method:**
    - Specifically handles collisions between enemy projectiles and the user.
        - decrease user's health only when there is actual collision with the enemy projectiles
    - When an enemy projectile collides with the user:
        - The projectile is destroyed (`projectile.takeDamage()`).
        - The user's health is decreased (`user.takeDamage()`).

2. **Updated `handleCollisions` Method:**
    - Introduced logic to detect collisions between projectiles and a `Boss` using the `Boss` class's custom hitbox (`boss.getCustomHitbox()`):
        - If a projectile intersects the boss's custom hitbox:
            - The projectile is destroyed.
            - The boss takes damage.
    - Ensured default collision detection for other enemies by checking:
      ```java
      enemy.getBoundsInParent().intersects(projectile.getBoundsInParent())
      ```

### Boss.java Changes

#### Added `getCustomHitbox` Method
- Introduced a new method `getCustomHitbox` to provide a more precise collision detection for the boss.
- Adjusts the default bounds by adding padding:
    - Horizontal padding: `80` units.
    - Vertical padding: `80` units.

#### Code Example:
```java
public javafx.geometry.Bounds getCustomHitbox() {
    // Get the default bounds of the BossSpider
    javafx.geometry.Bounds originalBounds = super.getBoundsInParent();

    // Adjust the bounds to make the hitbox more precise
    double paddingX = 80; // Horizontal padding
    double paddingY = 80; // Vertical padding
    return new javafx.geometry.BoundingBox(
            originalBounds.getMinX() + paddingX, // Adjust left boundary
            originalBounds.getMinY() + paddingY, // Adjust top boundary
            originalBounds.getWidth() - 2 * paddingX, // Adjust width
            originalBounds.getHeight() - 2 * paddingY // Adjust height
    );
}
```

### Glow Effect Changes for Boss Plane

#### Changes Made

1. **Replaced Shield Image with Glow Effect:**
    - Added a `DropShadow` effect to simulate a **bright yellow glow**.
    - Glow parameters include:
        - `Color.YELLOW` for the glow color.
        - `radius` set to `30` for a large glowing radius.
        - `spread` set to `0.5` for high brightness and intensity.

2. **Updated Shield Activation and Deactivation:**
    - When the shield is activated, the glow effect is applied using `setEffect(shieldGlowEffect)`.
    - When the shield is deactivated, the glow effect is removed using `setEffect(null)`.

3. **Constructor Adjustments:**
    - Initialized the `DropShadow` effect in the constructor of the `Boss` class.

#### Key Code Snippets:
- **Constructor Changes:**
```java
		shieldGlowEffect = new DropShadow();
		shieldGlowEffect.setColor(Color.YELLOW); // Blue glow for the shield
		shieldGlowEffect.setRadius(30);       // Adjust the radius for the glow size
		shieldGlowEffect.setSpread(0.5);      // Spread makes the glow more intense

```

- **Shield Activation and Deactivation:**
    - **Activate Shield:**
    ```java
    private void activateShield() {
        isShielded = true;
        setEffect(shieldGlowEffect); // Apply the bright yellow glow
    }
    ```

    - **Deactivate Shield:**
    ```java
    private void deactivateShield() {
        isShielded = false;
        framesWithShieldActivated = 0;
        setEffect(null); // Remove the glow effect
    }
    ```

### LevelParent.java Changes

#### Added Pause/Resume Button
- **New Feature: Pause/Resume Game**
    - Added a **Pause** button to allow the player to pause and resume the game.
    - When paused:
        - The game loop (`Timeline`) stops.
        - Player controls are disabled.
    - When resumed:
        - The game loop restarts.
        - Player controls are re-enabled.

- **Code Changes**:
  ```java
  private void initializePauseButton() {
      pauseButton = new Button("Pause");
      pauseButton.setFont(Font.font("Arial", 14));
      pauseButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-background-radius: 10;");
      pauseButton.setOnAction(e -> togglePause());

      // Position the button at the top-right of the screen
      pauseButton.setLayoutX(screenWidth - 100);
      pauseButton.setLayoutY(20);

      root.getChildren().add(pauseButton);
  }

  private void togglePause() {
      if (isPaused) {
          timeline.play();
          isPaused = false;
          pauseButton.setText("Pause");
      } else {
          timeline.pause();
          isPaused = true;
          pauseButton.setText("Resume");
      }
  }
  ```

#### Gameplay Usage
- During gameplay, click the **Pause** button at the top-right corner to pause the game.
- Click **Resume** to continue playing.


### Main.java Changes

### **1. Added Main Menu Initialization**
- **What Changed:**
    - Introduced a `MainMenu` object to initialize the main menu before launching the game.
    - Passed the `Stage` and the main class instance to the `MainMenu` for better modularity.
- **Why:**
    - To separate the main menu logic from the game logic, making it easier to manage and extend.

#### **Added Code:**
```java
// Initialize the main menu and pass this instance
MainMenu menu = new MainMenu();
menu.start(stage, this); // Pass stage and main class to menu
```

---

### **2. Introduced a `startGame` Method**
- **What Changed:**
    - Extracted game launch logic into a separate method.
- **Why:**
    - To improve code readability and separate responsibilities.

#### **New Method:**
```java
public void startGame(Stage stage) {
    try {
        myController = new Controller(stage);
        myController.launchGame();
    } catch (Exception e) {
        e.printStackTrace(); // Log exceptions if any issues occur during game startup
    }
}
```

---


### **3. Created `MainMenu` Class**
- **What Changed:**
    - Added a new `MainMenu` class to handle the main menu of the game.
    - The `MainMenu` class is responsible for displaying the main menu interface and transitioning to the game.
- **Why:**
    - To modularize the code, making it easier to manage the main menu separately from the game logic.






### **UserPlane Horizontal Movement Update Modified Classes**

1. **UserPlane.java**

    - **Added horizontal movement capabilities**:
        - Implemented `moveLeft()`, `moveRight()`, and `stopHorizontal()` methods.
        - Updated `updatePosition()` to include horizontal velocity adjustments, enabling the plane to move in both directions concurrently.

2. **LevelParent.java**

    - **Updated Key Handling**:
        - Modified the `initializeBackground()` method to handle `KeyCode.LEFT` and `KeyCode.RIGHT` events for moving the `UserPlane` horizontally.

### **Code Highlights**

#### **UserPlane.java**

- Added methods for handling horizontal movement:
  ```java
  public void moveLeft() {
      horizontalVelocityMultiplier = -1;
  }

  public void moveRight() {
      horizontalVelocityMultiplier = 1;
  }

  public void stopHorizontal() {
      horizontalVelocityMultiplier = 0;
  }
  ```
- Updated the `updatePosition()` method to factor in horizontal movement.

#### **LevelParent.java**

- Modified key press and release handling:
  ```java
  background.setOnKeyPressed(e -> {
      if (!isPaused) {
          KeyCode kc = e.getCode();
          if (kc == KeyCode.UP) getUser().moveUp();
          if (kc == KeyCode.DOWN) getUser().moveDown();
          if (kc == KeyCode.LEFT) getUser().moveLeft();
          if (kc == KeyCode.RIGHT) getUser().moveRight();
          if (kc == KeyCode.SPACE) fireProjectile();
      }
  });

  background.setOnKeyReleased(e -> {
      if (!isPaused) {
          KeyCode kc = e.getCode();
          if (kc == KeyCode.UP || kc == KeyCode.DOWN) getUser().stop();
          if (kc == KeyCode.LEFT || kc == KeyCode.RIGHT) getUser().stopHorizontal();
      }
  });
  ```

### Level Two: Transition to Level Three

**NEXT_LEVEL Constant**:
- A constant `NEXT_LEVEL` was defined at the top of the `LevelTwo` class to specify the path to Level Three.
```java
private static final String NEXT_LEVEL = "com.example.demo.LevelThree";
```






### UserPlane: Added Firepower Mechanic

In the `UserPlane` class, we introduced a new firepower mechanic that allows the player's plane to have different levels of firepower, impacting the number of projectiles fired.

## Key Changes Made:

1. **Firepower Level**:

    - A new attribute `firePowerLevel` was added to represent the current firepower level of the user plane:
      ```java
      private int firePowerLevel = 1; // Default firepower level is 1
      ```

2. **Modified `fireProjectile` Method**:

    - The `fireProjectile` method was modified to fire multiple projectiles based on the current `firePowerLevel`. If `firePowerLevel` is greater than 1, multiple projectiles are fired in a spread pattern:
      ```java
      @Override
      public ActiveActorDestructible fireProjectile() {
          double currentX = getLayoutX() + getTranslateX();
          double currentY = getLayoutY() + getTranslateY();
 
          List<ActiveActorDestructible> projectiles = new ArrayList<>();
          for (int i = 0; i < firePowerLevel; i++) {
              projectiles.add(new UserProjectile(currentX + PROJECTILE_X_POSITION_OFFSET, currentY + PROJECTILE_Y_POSITION_OFFSET + (i * 10)));
          }
 
          // Return the first projectile for backward compatibility, while other projectiles are added manually.
          return projectiles.get(0);
      }
      ```

3. **Managing Firepower Level**:

    - Added methods to manage the firepower level:
      ```java
      public int getFirePowerLevel() {
          return firePowerLevel;
      }
 
      public void setFirePowerLevel(int level) {
          this.firePowerLevel = level;
      }
 
      public void resetFirePower() {
          this.firePowerLevel = 1; // Reset to default level
      }
      ```

These changes enhance the gameplay by allowing the player to collect power-ups that increase their firepower, enabling them to fire multiple projectiles at once for a limited time.

### Added new class
- LevelThree.java
- LevelViewLevelThree.java
- PowerUp.java


### LevelParent: Power-Up Integration

In the LevelParent class, we integrated the functionality to spawn and handle power-ups, allowing the player to collect items that enhance their capabilities.

## Key Changes Made:

1. **Power-Up Collection Handling**:

    - A new method `handlePowerUpCollisions` was added to handle collisions between the user plane and power-ups:
      ```java
      private void handlePowerUpCollisions() {
          for (ActiveActorDestructible powerUp : powerUps) {
              if (powerUp.getBoundsInParent().intersects(user.getBoundsInParent())) {
                  System.out.println("PowerUp collected!");
                  activatePowerUp();
                  powerUp.destroy();
              }
          }
      }
      ```

2. **Activating Power-Ups**:

    - The `activatePowerUp` method was added to temporarily increase the user's firepower level when a power-up is collected:
      ```java
      private void activatePowerUp() {
          user.setFirePowerLevel(user.getFirePowerLevel() + 1);
          Timeline powerUpTimer = new Timeline(new KeyFrame(Duration.seconds(5), e -> user.resetFirePower()));
          powerUpTimer.setCycleCount(1);
          powerUpTimer.play();
      }
      ```

These additions provide an exciting new mechanic to the game, where players can collect power-ups to gain temporary advantages, adding depth and strategy to gameplay.



### UserPlane Spreadshot Feature**

### **Changes Made**

1. **Added Spreadshot Capability**:
    - **Method Added**: `activateOneTimeSpreadshot()`
        - Activates a spreadshot that allows the user to fire three projectiles at once for the next shot.
    - **Spreadshot Implementation**:
        - Updated the `fireProjectile()` method to support firing three projectiles (left, center, right) when the spreadshot is active.
        - After firing the spreadshot, the plane automatically reverts to firing a single projectile.

   ```java
   public void activateOneTimeSpreadshot() {
       oneTimeSpreadshotActive = true; // Activate spreadshot for the next shot
   }

   @Override
   public ActiveActorDestructible fireProjectile() {
       double currentX = getLayoutX() + getTranslateX();
       double currentY = getLayoutY() + getTranslateY();

       if (oneTimeSpreadshotActive) {
           // Create spreadshot projectiles
           ActiveActorDestructible leftProjectile = new UserProjectile(currentX + 100, currentY - 10);
           ActiveActorDestructible centerProjectile = new UserProjectile(currentX + 100, currentY);
           ActiveActorDestructible rightProjectile = new UserProjectile(currentX + 100, currentY + 10);

           // Add all projectiles to the scene via LevelParent
           levelParent.addProjectile(leftProjectile);
           levelParent.addProjectile(centerProjectile);
           levelParent.addProjectile(rightProjectile);

           oneTimeSpreadshotActive = false; // Reset spreadshot flag
           return centerProjectile; // Return the center projectile for compatibility
       } else {
           // Single shot
           ActiveActorDestructible projectile = new UserProjectile(currentX + 100, currentY);
           levelParent.addProjectile(projectile); // Add to LevelParent
           return projectile;
       }
   }
   ```

---

### **Usage Summary**
- **Activate Spreadshot**: Call `activateOneTimeSpreadshot()` to enable a spreadshot for the next shot.
- **Fire Projectile**: Use `fireProjectile()` to fire either a single shot or a spreadshot depending on whether spreadshot is activated.

---

### **Key Features**
- **Single Shot**: By default, fires one projectile.
- **Spreadshot**: After activation, fires three projectiles (left, center, right).

---

### **Notes**
- The spreadshot fires **only once** after activation, then reverts to the default single shot.
---
### created
SpreadshotPowerUp.java class

---
. **LevelParent Method Added**:
- **Method Added**: `addProjectile(ActiveActorDestructible projectile)`
- Adds a projectile to the game scene if it is not already present.
- Ensures that projectiles are tracked and added to the scene graph only once.

   ```java
   public void addProjectile(ActiveActorDestructible projectile) {
       if (!userProjectiles.contains(projectile)) {
           getRoot().getChildren().add(projectile); // Add to the scene graph
           userProjectiles.add(projectile);         // Track the projectile
       }
   }
   ```

---

### Changes in gameoverimage.java
- set the size of image smaller
- hard code the image position **might change**
---

# Created SoundManager.java
## Sound Management

The `SoundManager` class is used in `LevelParent` to handle all sound effects, including:

- **Power-Up Collection**: When the user collects a power-up, a sound effect is played using `SoundManager`.
- **Game Win/Loss**: The `SoundManager` is used to play sounds when the user wins or loses the game.

The `SoundManager` is initialized in `LevelParent` and provides sound effects throughout the gameplay experience, enhancing the player's immersion.

## Key Features of `LevelParent`

- Manages background, enemies, and projectiles for each level.
- Includes a pause button for stopping and resuming the game.
- Handles user input for controlling the player's plane.
- Updates actors, checks for collisions, and manages power-ups.

---

## Boss Projectile Modification

The boss projectile has been modified from a normal projectile to a spread web projectile (`BossProjectile`). Key changes include:

- **Explosion with Spread Fragments**: When the boss projectile reaches a certain point, it explodes after a delay, releasing multiple smaller web fragments that spread out in random directions.
- **Fragment Creation**: The fragments are created with random velocities to ensure a more natural and wider spread, adding challenge to the gameplay.
- **Delayed Explosion**: The explosion of the boss projectile is delayed by 1 second after it reaches a specific position, providing a more dynamic effect.
- **Collision Detection**: The fragments check for collisions with the user's plane, reducing health upon impact.

---
## SoundManager.java
- soundEffectsMuted method, to check whether soound effect is muted
---
## MainMenu.java
- added background music
- settings (mute sound effect & mute background music)

---
## Pausemenu.java
- pausemenu.java created
- initialised pause menu & create pause button in levelparrent
- ---
## EndGameMenu.java
- EndGameMenu.java created
- initialised end game menu in levelparrent