package fox.main;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
    private static String mBindingPath;

    private static GLFWKeyCallback mKeyCallback = null;
    private static Map<Integer, String> mKeyToAction = new HashMap<>();
    private static Map<String, Boolean> mKeyPressed = new HashMap<>();
    private static Map<String, Boolean> mKeyHeld = new HashMap<>();
    private static Map<String, Boolean> mKeyReleased = new HashMap<>();

    /**
     * The keyboard handler.
     */
    static void init(){
        bind();

        mKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window, true);
                }

                for (Integer keyCode : mKeyToAction.keySet()){
                    if (key == keyCode) {
                        if (action == GLFW_PRESS)
                            mKeyPressed.put(mKeyToAction.get(keyCode), true);
                        if (action == GLFW_RELEASE) {
                            mKeyReleased.put(mKeyToAction.get(keyCode), true);
                            mKeyHeld.put(mKeyToAction.get(keyCode), false);
                        }
                        if (action == GLFW_REPEAT)
                            mKeyHeld.put(mKeyToAction.get(keyCode), true);
                    }
                }
            }
        };
    }

    /**
     * Update the keys.
     */
    static void update(){
        Set<String> keyGroups = mKeyPressed.keySet();
        for (String keyGroup : keyGroups){
            mKeyPressed.put(keyGroup, false);
            mKeyReleased.put(keyGroup, false);
        }
    }

    /**
     * Set the path of the file which contains the key bindings. (JSON)
     * @param path The path to the key binding file.
     */
    public static void setBindingPath(String path){
        mBindingPath = path;
    }

    /**
     * Check if a key group is pressed.
     * @param keyGroup The key group in question
     * @return 1 if the key group is pressed, 0 otherwise
     */
    public static int getPressed(String keyGroup){
        Set<String> keyGroups = mKeyPressed.keySet();
        if (keyGroups.contains(keyGroup))
            return (mKeyPressed.get(keyGroup)) ? 1 : 0;
        else {
            Logger logger = Logger.getLogger();
            logger.warn("The key group: " + keyGroup + " does not exist");
            return 0;
        }
    }

    /**
     * Check if a key group is released.
     * @param keyGroup The key group in question
     * @return 1 if the key group is released, 0 otherwise
     */
    public static int getReleased(String keyGroup){
        Set<String> keyGroups = mKeyReleased.keySet();
        if (keyGroups.contains(keyGroup))
            return (mKeyReleased.get(keyGroup)) ? 1 : 0;
        else {
            Logger logger = Logger.getLogger();
            logger.warn("The key group: " + keyGroup + " does not exist");
            return 0;
        }
    }

    /**
     * Check if a key group is held.
     * @param keyGroup The key group in question
     * @return 1 if the key group is held, 0 otherwise
     */
    public static int getHeld(String keyGroup){
        Set<String> keyGroups = mKeyHeld.keySet();
        if (keyGroups.contains(keyGroup))
            return (mKeyHeld.get(keyGroup)) ? 1 : 0;
        else {
            Logger logger = Logger.getLogger();
            logger.warn("The key group: " + keyGroup + " does not exist");
            return 0;
        }
    }

    /**
     * Read the specified JSON file.
     * @param path The path to the json file
     * @return The text of the JSON file
     */
    private static String readJSON(String path){
        BufferedReader mappingReader = null;
        String jsonText = null;
        try {
            mappingReader = new BufferedReader(new FileReader(path));

            jsonText = "";
            String line = "";
            while ((line = mappingReader.readLine()) != null)
                jsonText += line + "\n";

        } catch (IOException e){
            Logger logger = Logger.getLogger();
            logger.error("Could not load JSON file: " + path);
            System.exit(1);
        }

        return jsonText;
    }

    /**
     * Bind the desired keys
     */
    private static void bind(){
        Logger logger = Logger.getLogger();
        logger.debug("Binding keys");

        String mappingText = readJSON("./src/main/resources/keymapping.json");
        JSONObject mappingJSON = new JSONObject(mappingText);

        String bindingText = readJSON(mBindingPath);
        JSONObject bindingJSON = new JSONObject(bindingText);
        String[] bindingKeys = JSONObject.getNames(bindingJSON);

        for (String bindingKey : bindingKeys){
            JSONArray relatedKeys = bindingJSON.getJSONArray(bindingKey);
            Iterator<Object> keyIterator = relatedKeys.iterator();

            while (keyIterator.hasNext()){
                String key = (String)keyIterator.next();
                int glfwKeyValue = mappingJSON.getInt(key);

                mKeyToAction.put(glfwKeyValue, bindingKey);
            }

            mKeyPressed.put(bindingKey, false);
            mKeyHeld.put(bindingKey, false);
            mKeyReleased.put(bindingKey, false);
        }
    }

    /**
     * Get the GLFW KeyCallback Function.
     * @return The GLFW KeyCallback function.
     */
    public static GLFWKeyCallback getKeyCallback(){
        return mKeyCallback;
    }
}
