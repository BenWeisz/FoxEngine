package fox.main;

import fox.node.Node;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class FoxEngine {
    private Map<String, Object> mSettings;

    private GLFWErrorCallback mErrorCallback = GLFWErrorCallback.createPrint(System.err);

    private long mWindow;
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
        Keyboard.init();

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

        glfwSetKeyCallback(mWindow, Keyboard.getKeyCallback());

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(mWindow, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

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

        double prevLogTime = glfwGetTime();
        int upsCounter = 0;

        while ((Boolean) mSettings.get("running") && !glfwWindowShouldClose(mWindow)){
            double currentTime = glfwGetTime();

            if (currentTime - prevLogTime > 1.0f){
                prevLogTime = currentTime;
                logger.debug("UPS -> " + upsCounter);
                upsCounter = 0;
            }

            if (currentTime - prevUpdateTime > targetUpdateDelta){
                update(currentTime - prevUpdateTime);
                prevUpdateTime = currentTime;
                upsCounter += 1;
            }

            if (currentTime - prevFrameTime > targetFrameDelta) {
                prevFrameTime = currentTime;
                render();
            }

            glfwSwapBuffers(mWindow);
            glfwPollEvents();
        }

        glfwDestroyWindow(mWindow);
        Keyboard.getKeyCallback().free();

        glfwTerminate();
        mErrorCallback.free();
    }

    /**
     * Perform a physics update in the engine.
     * @param dt The elapsed time since the last physics update
     */
    private void update(double dt){
        Stack<Node> visited = new Stack<>();
        visited.add(mCurrentScene);

        Stack<Node> updates = new Stack<>();

        while (!visited.empty()){
            Node currNode = visited.pop();
            updates.push(currNode);

            List<Node> children = currNode.getChildren();
            for (Node child : children)
                visited.push(child);
        }

        while (!updates.empty()){
            Node currNode = updates.pop();
            currNode.update(dt);
        }

        Keyboard.update();
    }

    /**
     * Perform a graphics update.
     */
    private void render(){

    }

    /**
     * Swap the current scene for a new one.
     * @param scene The scene that is to be displayed
     */
    public void switchScene(Node scene){
        mCurrentScene = scene;
    }
}
