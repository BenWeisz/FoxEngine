package fox.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
    private GLFWKeyCallback mKeyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
        }
    };

    private static Keyboard mKeyboard = null;

    /**
     * The keyboard handler.
     */
    private Keyboard(){

    }

    /**
     * Get the GLFW KeyCallback Function.
     * @return The GLFW KeyCallback function.
     */
    public GLFWKeyCallback getKeyCallback(){
        return mKeyCallback;
    }

    /**
     * Get the Keyboard singleton.
     * @return The Keyboard singleton.
     */
    public static Keyboard getKeyboard(){
        if (mKeyboard != null)
            return mKeyboard;
        else return new Keyboard();
    }
}
