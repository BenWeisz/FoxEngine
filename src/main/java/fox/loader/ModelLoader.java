package fox.loader;

import fox.render.Model3D;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

    private List<Integer> mVaos = new ArrayList<>();
    private List<Integer> mVbos = new ArrayList<>();

    /**
     * Load a 3D Model.
     * @param positions The 3D coordinates of the Model's vertices
     * @return A new Model3D
     */
    public Model3D loadModel3D(float[] positions){
        int vaoID = GL40.glGenVertexArrays();

        mVaos.add(vaoID);

        GL40.glBindVertexArray(vaoID);
        storeVaoData(0, positions, 3);
        GL40.glBindVertexArray(0);

        return new Model3D(vaoID, positions.length / 3);
    }

    /**
     * Store some float data in the VAO as a VBO.
     * @param index The index of the VAO into which the data should be inserted
     * @param data The data to be inserted
     * @param dataLength The length of data array to be inserted
     */
    private void storeVaoData(int index, float[] data, int dataLength) {
        int vboID = GL40.glGenBuffers();
        mVbos.add(vboID);

        GL40.glBindBuffer(GL40.GL_ARRAY_BUFFER, vboID);

        FloatBuffer buffer = convertToFloatBuffer(data);
        GL40.glBufferData(GL40.GL_ARRAY_BUFFER, vboID, GL40.GL_STATIC_DRAW);

        GL40.glVertexAttribPointer(index, dataLength, GL40.GL_FLOAT, false, 0, 0);
        GL40.glBindBuffer(GL40.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Convert a float array into a float buffer.
     * @param data The data that is to be converted
     * @return The float buffer version of the data
     */
    private FloatBuffer convertToFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);

        buffer.flip();
        return buffer;
    }

    /**
     * Garbage collect all the vaos and vbos.
     */
    public void garbageCollect(){
        for (int vao : mVaos)
            GL40.glDeleteVertexArrays(vao);

        for (int vbo : mVbos)
            GL40.glDeleteBuffers(vbo);
    }
}
