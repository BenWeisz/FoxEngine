package test.main;

import fox.main.Keyboard;
import fox.main.FoxEngine;

public class Game extends FoxEngine{
    public void init() {
        updateSetting("UPS", 60.0f);
        updateSetting("FPS", 60.0f);
        updateSetting("visible", false);

        // Setup the key bindings
        Keyboard.setBindingPath("./src/test/resources/bindings.json");

        TestNode test = new TestNode("root");
        switchScene(test);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}
