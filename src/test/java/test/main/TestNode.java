package test.main;

import fox.main.Keyboard;
import fox.main.Logger;
import fox.node.Node;

public class TestNode extends Node {
    public TestNode(String name){
        super(name);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(double dt) {
        int up = Keyboard.getReleased("up");

        Logger logger = Logger.getLogger();
        logger.log((up > 0) ? "ON" : "OFF");
    }
}
