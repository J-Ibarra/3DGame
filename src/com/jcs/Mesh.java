package com.jcs;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Jcs on 14/9/2016.
 */
public class Mesh {
    private int vao = -1;
    private int nDraw = -1;
    private int[] vbos;

    public Mesh() {

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

        vbos = new int[1];
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        vbos[0] = vbo;

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length);
        fb.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, vertexFloatSizeInBytes, 0);
        glEnableVertexAttribArray(0);

        int byteOffset = floatByteSize * positionFloatCount;
        glVertexAttribPointer(2, 2, GL_FLOAT, false, vertexFloatSizeInBytes, byteOffset);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public Mesh(Data[] data) {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbos = new int[data.length + 1];

        for (int i = 0; i < data.length; i++) {
            Data d = data[i];
            FloatBuffer fb = BufferUtils.createFloatBuffer(d.data.length);
            fb.put(d.data).flip();
            int vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            vbos[i] = vbo;

            glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);
            glVertexAttribPointer(d.index, d.size, d.type, false, 0, 0);
            glEnableVertexAttribArray(d.index);
        }
        nDraw = data[0].data.length / 3;
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public Mesh(Data[] data, int[] indices) {
        this(data);
        glBindVertexArray(vao);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length);
        ib.put(indices).flip();
        int vbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        vbos[data.length] = vbo;
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, ib, GL_STATIC_DRAW);
        nDraw = indices.length;
        glBindVertexArray(0);
    }

    public void draw() {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, nDraw, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        for (int i = 0; i < vbos.length; i++) {
            glDeleteBuffers(vbos[i]);
        }
        glDeleteVertexArrays(vao);
        nDraw = -1;
        vbos = null;
        vao = -1;
    }

    public static class Data {
        public float[] data;
        public int index;
        public int size;
        public int type;

        public Data(int index, int size, int type, float[] data) {
            this.data = data;
            this.index = index;
            this.size = size;
            this.type = type;
        }
    }
}

