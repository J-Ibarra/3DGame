package com.jcs;

import com.jcs.utils.OBJLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Jcs on 14/9/2016.
 */
public class Mesh {
    private int vao = -1;
    private int nDraw = -1;
    private int[] vbs;
    private boolean drawMode = false;

    //<editor-fold defaultstate="collapsed" desc="variables cache">
    private static List<Mesh> meshes = new ArrayList<>();
    //</editor-fold>

    public Mesh() {
        // <editor-fold defaultstate="collapsed" desc="init Mesh of default CubeTexture">

        float[] vertices = new float[]{
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
        };

        int floatByteSize = 4;
        int positionFloatCount = 3;
        int textureFloatCount = 2;
        int floatsPerVertex = positionFloatCount + textureFloatCount;
        int vertexFloatSizeInBytes = floatByteSize * floatsPerVertex;

        vbs = new int[1];
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        vbs[0] = vbo;

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length);
        fb.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, vertexFloatSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        int byteOffset = floatByteSize * positionFloatCount;
        glVertexAttribPointer(1, 2, GL_FLOAT, false, vertexFloatSizeInBytes, byteOffset);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        nDraw = 36;
        drawMode = true;

        meshes.add(this);
        // </editor-fold>
    }

    public Mesh(Data[] data) {
        //<editor-fold defaultstate="collapsed" desc="create Mesh from Array Data">
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbs = new int[data.length + 1];

        for (int i = 0; i < data.length; i++) {
            Data d = data[i];

            int vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            vbs[i] = vbo;

            glBufferData(GL_ARRAY_BUFFER, d.data, GL_STATIC_DRAW);
            glVertexAttribPointer(d.index, d.size, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(d.index);
        }
        nDraw = data[0].data.remaining() / 3;
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        drawMode = true;
        meshes.add(this);
        //</editor-fold>
    }

    public Mesh(Data[] data, int[] indices) {
        //<editor-fold defaultstate="collapsed" desc="create Mesh from Array Data and put indices">
        this(data);
        glBindVertexArray(vao);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length);
        ib.put(indices).flip();
        int vbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        vbs[data.length] = vbo;
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);
        nDraw = indices.length;
        glBindVertexArray(0);
        drawMode = false;
        //</editor-fold>
    }

    public Mesh(OBJLoader.Model model) {
        //<editor-fold desc="create Mesh from OBJLoader">
        this(processModel(model));
        //</editor-fold>
    }

    private static Data[] processModel(OBJLoader.Model model) {
        // <editor-fold defaultstate="collapsed" desc="Data[] processModel from  OBJLoader">
        float[] vData = new float[model.f.size() * 3 * 3];
        float[] nData = new float[model.f.size() * 3 * 3];
        float[] tData = new float[model.f.size() * 2 * 3];

        for (int i = 0; i < model.f.size(); i++) {
            OBJLoader.Face face = model.f.get(i);
            Vector3f v1 = model.v.get(face.v.x);
            Vector3f v2 = model.v.get(face.v.y);
            Vector3f v3 = model.v.get(face.v.z);

            vData[i * 9 + 0] = v1.x;
            vData[i * 9 + 1] = v1.y;
            vData[i * 9 + 2] = v1.z;
            vData[i * 9 + 3] = v2.x;
            vData[i * 9 + 4] = v2.y;
            vData[i * 9 + 5] = v2.z;
            vData[i * 9 + 6] = v3.x;
            vData[i * 9 + 7] = v3.y;
            vData[i * 9 + 8] = v3.z;

            Vector2f t1 = model.t.get(face.t.x);
            Vector2f t2 = model.t.get(face.t.y);
            Vector2f t3 = model.t.get(face.t.z);

            tData[i * 6 + 0] = t1.x;
            tData[i * 6 + 1] = t1.y;
            tData[i * 6 + 2] = t2.x;
            tData[i * 6 + 3] = t2.y;
            tData[i * 6 + 4] = t3.x;
            tData[i * 6 + 5] = t3.y;

            Vector3f n1 = model.v.get(face.n.x);
            Vector3f n2 = model.v.get(face.n.y);
            Vector3f n3 = model.v.get(face.n.z);

            nData[i * 9 + 0] = n1.x;
            nData[i * 9 + 1] = n1.y;
            nData[i * 9 + 2] = n1.z;
            nData[i * 9 + 3] = n2.x;
            nData[i * 9 + 4] = n2.y;
            nData[i * 9 + 5] = n2.z;
            nData[i * 9 + 6] = n3.x;
            nData[i * 9 + 7] = n3.y;
            nData[i * 9 + 8] = n3.z;
        }

        Data v = new Data(0, 3, vData);
        Data t = new Data(1, 2, tData);
        Data n = new Data(2, 3, nData);
        return new Data[]{v, t, n};
        // </editor-fold>
    }

    public void draw() {
        // <editor-fold defaultstate="collapsed" desc="draw the Mesh">
        glBindVertexArray(vao);
        if (drawMode)
            glDrawArrays(GL_TRIANGLES, 0, nDraw);
        else
            glDrawElements(GL_TRIANGLES, nDraw, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        // </editor-fold>
    }

    public static void cleanUp() {
        // <editor-fold defaultstate="collapsed" desc="cleanUp all Meshes">
        while (!meshes.isEmpty()) {
            deleteMesh(meshes.get(0));
        }
        // </editor-fold>
    }

    public static Mesh deleteMesh(Mesh mesh) {
        // <editor-fold defaultstate="collapsed" desc="deleteMesh">

        for (int i = 0; i < mesh.vbs.length; i++) {
            glDeleteBuffers(mesh.vbs[i]);
        }
        glDeleteVertexArrays(mesh.vao);
        mesh.nDraw = -1;
        mesh.vbs = null;
        mesh.vao = -1;
        meshes.remove(mesh);
        mesh = null;
        return mesh;
        // </editor-fold>
    }

    public static class Data {
        // <editor-fold defaultstate="collapsed" desc="Body of Data class">
        public FloatBuffer data;
        public int index;
        public int size;

        public Data(int index, int size, FloatBuffer data) {
            this.data = data;
            this.index = index;
            this.size = size;
        }

        public Data(int index, int size, float[] data) {
            this.index = index;
            this.size = size;
            this.data = BufferUtils.createFloatBuffer(data.length);
            this.data.put(data).flip();
        }
        // </editor-fold>
    }
}

