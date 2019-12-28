package fox.node;

import fox.main.Logger;

import java.util.*;

public abstract class Node {
    protected String mName;
    protected List<Node> mChildren = new ArrayList<>();
    protected Set<String> mChildrenNames = new HashSet<>();

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
     * <b>Children (direct descendants) should have distinct names</b>.
     * It is recommended that descendants do not have the same names
     * @param child The node to add as a child
     */
    public void addChild(Node child){
        if (!mChildrenNames.contains(child.getName())){
            mChildren.add(child);
            mChildrenNames.add(child.mName);
        }
        else {
            Logger logger = Logger.getLogger();
            logger.warn("This Node already has a child with name: " + child.getName());
        }
    }

    /**
     * Add multiple children to this Node.
     * @param children The list of children to add
     */
    public void addChildren(Node... children){
        for (Node child : children)
            addChild(child);
    }

    /**
     * Get the shallowest descendant of this node that has the given name.
     * @param name The name of the descendant
     * @return The descendant
     */
    public Node getDescendant(String name){
        Queue<Node> descendants = new LinkedList<>();
        descendants.add(this);

        while (!descendants.isEmpty()){
            Node descendant = descendants.poll();
            if (descendant.getName().equals(name))
                return  descendant;

            for (Node child : descendant.getChildren())
                descendants.add(child);
        }

        Logger logger = Logger.getLogger();
        logger.warn("No descendant was found with name: " + name);

        return null;
    }

    /**
     * Remove a descendant of this Node only if it exists.
     * This method cannot remove the node itself.
     * @param name The name of the descendant to remove
     * @return Whether or not the descendant was removed
     */
    public boolean removeDescendant(String name){
        Node nullNode = null;
        Queue<List<Node>> descendantChain = new LinkedList<>();
        descendantChain.add(Arrays.asList(this, nullNode));

        Logger logger = Logger.getLogger();

        while (!descendantChain.isEmpty()){
            List<Node> chain = descendantChain.poll();

            Node descendant = chain.get(0);
            Node parent = chain.get(1);

            if (descendant.getName().equals(name)){
                if (parent!= null){
                    parent.getChildren().remove(descendant);
                    parent.mChildrenNames.remove(descendant.getName());

                    return true;
                }
                else {
                    logger.warn("The node itself is not a descendant, so it cannot be removed");
                    return false;
                }
            }

            for (Node child : descendant.getChildren())
                descendantChain.add(Arrays.asList(child, descendant));
        }

        logger.warn("The descendant with name: " + name + " does not exists");
        return false;
    }

    public String getName(){
        return mName;
    }
}
