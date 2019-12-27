package fox.node;

import fox.main.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Node {
    private String mName;
    private List<Node> mChildren = new ArrayList<>();
    private Set<String> mChildrenNames = new HashSet<>();

    /**
     * Create a new Node.
     * @param name The name of this Node
     */
    public Node(String name){
        mName = name;
    }

    public abstract void init();
    public abstract void update(double dt);

    /**
     * Get all the children of this Node.
     * @return The List of this Node's children
     */
    public List<Node> getChildren(){
        return mChildren;
    }

    /**
     * Add a child to this node.
     * - Children (direct descendants) should have distinct names
     * - It is recommended that descendants do not have the same names
     * @param child The node to add as a child
     */
    public void addChild(Node child){
        if (!mChildrenNames.contains(child.getName())){
            mChildren.add(child);
            mChildrenNames.add(child.mName);
        }
        else {
            Logger logger = Logger.getLogger();
            logger.error("This Node already has a child with name: " + child.getName());
        }
    }

    // TODO Implement getDescendant : recursively find first descendant with given name
    // TODO Implement removeDescendent : recursively find first descendant with given name and remove

    public String getName(){
        return mName;
    }
}
