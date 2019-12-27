package fox.main;

import fox.input.Keyboard;
import fox.node.Node;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class FoxEngine {
    private Map<String, Object> mSettings;

    private GLFWErrorCallback mErrorCallback = GLFWErrorCallback.createPrint(System.err);

    // TODO move this to the input class.
    private GLFWKeyCallback mKeyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
                mSettings.put("running", false);
            }
        }
    };

    private long mWindow;
    private Keyboard mKeyboard;
    private Node mCurrentScene;

    /**
     * Initialize the default settings for the FoxEngine.
     */
    public FoxEngine(){
        mSettings = new HashMap<>();
        mSettings.put("running", true);
        mSettings.put("title", "FoxEngine");
        mSettings.put("resizeable", false);
        mSettings.put("visible", true);
        mSettings.put("width", 640);
        mSettings.put("height", 480);
        mSettings.put("UPS", 60.0f);
        mSettings.put("FPS", 60.0f);
    }

    /**
     * Update setting key with value value.
     * @param key The setting to update
     * @param value The updated value
     */
    public void updateSetting(String key, Object value){
        mSettings.put(key, value);
    }

    /**
     * Initialize all the scenes and their resources.
     */
    public abstract void init();

    private void internalInit(){
        init();

        glfwSetErrorCallback(mErrorCallback);

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        int width = (Integer) mSettings.get("width");
        int height = (Integer) mSettings.get("height");
        String title = (String)mSettings.get("title");

        mWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (mWindow == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        mKeyboard = Keyboard.getKeyboard();
        glfwSetKeyCallback(mWindow, mKeyboard.getKeyCallback());

        glfwMakeContextCurrent(mWindow);
        GL.createCapabilities();
    }

    /**
     * Run the engine.
     */
    public final void run() {
        Logger logger = Logger.getLogger();
        logger.debug("Starting Initialization");

        internalInit();

        logger.debug("Engine Start");

        double prevUpdateTime = glfwGetTime();
        double prevFrameTime = glfwGetTime();

        double targetUpdateDelta = 1.0f / (Float) mSettings.get("UPS");
        double targetFrameDelta = 1.0f / (Float) mSettings.get("FPS");

        while ((Boolean) mSettings.get("running") && !glfwWindowShouldClose(mWindow)){
            double currentTime = glfwGetTime();

            if (currentTime - prevUpdateTime > targetUpdateDelta){
                update(currentTime - prevUpdateTime);
                prevUpdateTime = currentTime;
            }

            if (currentTime - prevFrameTime > targetFrameDelta) {
                prevFrameTime = currentTime;
                render();
            }

            glfwSwapBuffers(mWindow);
            glfwPollEvents();
        }

        glfwDestroyWindow(mWindow);
        mKeyboard.getKeyCallback().free();

        glfwTerminate();
        mErrorCallback.free();
    }

    /**
     * Perform a physics update in the engine.
     * @param dt The elapsed time since the last physics update
     */
    private void update(double dt){

    }

    /**
     * Perform a graphics update.
     */
    private void render(){

    }
}
