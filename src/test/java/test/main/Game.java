package test.main;

import fox.main.FoxEngine;

public class Game extends FoxEngine{
    public void init() {
        updateSetting("UPS", 1.0f);
        updateSetting("FPS", 1.0f);
        updateSetting("visible", false);
        updateSetting("title", "My First Game");
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}
