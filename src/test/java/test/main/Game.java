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

        TestNode n1 = new TestNode("n1");
        TestNode n2 = new TestNode("n2");
        TestNode n3 = new TestNode("n3");
        TestNode n4 = new TestNode("n4");
        TestNode n5 = new TestNode("n5");
        TestNode n6 = new TestNode("n6");
        TestNode n7 = new TestNode("n7");

        n3.addChildren(n6, n7);
        n2.addChildren(n4, n5);
        n1.addChildren(n2, n3);

        n1.removeDescendant("n2");

        switchScene(n1);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}
