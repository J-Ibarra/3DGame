package com.jcs;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static com.jcs.utils.IOUtils.ioResourceToByteBuffer;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Jcs on 18/8/2016.
 */
public class ShaderProgram {
    public int id = -1;
    public int vsId;
    public int fsId;

    public ShaderProgram() {
        id = glCreateProgram();
        if (id < 1)
            throw new RuntimeException("Could not create Shader");
    }

    public ShaderProgram(String vs, String fs) {
        this();
        createVertexShader(vs);
        createFragmentShader(fs);
        link();
    }

    public ShaderProgram(CharSequence vs, CharSequence fs) {
        this();
        createVertexShader(vs);
        createFragmentShader(fs);
        link();
    }

    public void createVertexShader(String resource) {
        vsId = createShader(resource, GL_VERTEX_SHADER);
        glAttachShader(id, vsId);
    }

    public void createVertexShader(CharSequence source) {
        vsId = createShader(source, GL_VERTEX_SHADER);
        glAttachShader(id, vsId);
    }

    public void createFragmentShader(String resource) {
        fsId = createShader(resource, GL_FRAGMENT_SHADER);
        glAttachShader(id, fsId);
    }

    public void createFragmentShader(CharSequence source) {
        fsId = createShader(source, GL_FRAGMENT_SHADER);
        glAttachShader(id, fsId);
    }

    public void link() {
        bind();
        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error linking shader, log: vs: " + glGetShaderInfoLog(vsId) + ", fs: " +
                    glGetShaderInfoLog(fsId) + ", shader: " + glGetShaderInfoLog(id));
        }

        glValidateProgram(id);
        if (glGetProgrami(id, GL_VALIDATE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Warning validating shader, log: vs:" + glGetShaderInfoLog(vsId) + ", fs:  " +
                    glGetShaderInfoLog(fsId) + ", shader: " + glGetShaderInfoLog(id));
        }

        glDeleteShader(vsId);
        vsId = -1;
        glDeleteShader(fsId);
        fsId = -1;
        unbind();
    }

    public void bind() {
        glUseProgram(id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanUp() {
        unbind();
        if (id > 0) {
            if (vsId > 0) {
                glDetachShader(id, vsId);
                glDeleteShader(vsId);
            }
            vsId = -1;

            if (fsId > 0) {
                glDetachShader(id, fsId);
                glDeleteShader(fsId);
                fsId = -1;
            }
            glDeleteProgram(id);
            id = -1;
        }
    }

    public int createShader(String resource, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId < 1) {
            throw new RuntimeException("Error creating shader, log: " + glGetShaderInfoLog(shaderId));
        }
        ByteBuffer source;

        source = ioResourceToByteBuffer(resource);


        PointerBuffer strings = BufferUtils.createPointerBuffer(1);
        IntBuffer lengths = BufferUtils.createIntBuffer(1);

        strings.put(0, source);
        lengths.put(0, source.remaining());

        glShaderSource(shaderId, strings, lengths);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId));
        }

        return shaderId;
    }

    protected int createShader(CharSequence source, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId < 1) {
            throw new RuntimeException("Error creating shader, log: " + glGetShaderInfoLog(shaderId));
        }

        glShaderSource(shaderId, source);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Error compiling Shader code: " + glGetShaderInfoLog(shaderId));
        }

        return shaderId;
    }
}
