package fox.render;

public class Model3D {
    private int mVaoID, mVertexCount;

    /**
     * A 3d Model.
     * @param vaoID The Model's vaoID
     * @param vertexCount The number of vertices the Model has
     */
    public Model3D(int vaoID, int vertexCount){
        mVaoID = vaoID;
        mVertexCount = vertexCount;
    }

    /**
     * Get the vaoID of this Model.
     * @return The vaoID
     */
    public int getVaoID(){
        return mVaoID;
    }

    /**
     * Get the number of vertices of this model.
     * @return The number of vertices
     */
    public int getVertexCount(){
        return mVertexCount;
    }
}
