module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.logging;


    // Open packages required by JavaFX for reflection (e.g., FXML loaders)
    opens com.example.demo.controller to javafx.fxml;
    opens com.example.demo.ui to javafx.fxml;

    // Export packages for use by other modules or for JavaFX runtime
    exports com.example.demo.controller;
    exports com.example.demo.actors;
    exports com.example.demo.projectiles;
    exports com.example.demo.powerups;
    exports com.example.demo.ui;
    exports com.example.demo.managers;
    exports com.example.demo.gameplay;
    exports com.example.demo.observer;
    exports com.example.demo.actors.enemies;
    exports com.example.demo.actors.player;
    exports com.example.demo.ui.menus;
    opens com.example.demo.ui.menus to javafx.fxml;
    exports com.example.demo.ui.gameplayUI;
    opens com.example.demo.ui.gameplayUI to javafx.fxml;

}
