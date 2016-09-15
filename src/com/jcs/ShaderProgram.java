package com.jcs;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //<editor-fold defaultstate="collapsed" desc="variables cache">
    private static List<ShaderProgram> shades = new ArrayList<>();
    private Map<String, Integer> uniform = new HashMap<>();
    //</editor-fold>

    public ShaderProgram() {
        //<editor-fold desc="create ShaderProgram empty">
        id = glCreateProgram();
        if (id < 1)
            throw new RuntimeException("Could not create Shader");
        shades.add(this);
        //</editor-fold>
    }

    public ShaderProgram(String vs, String fs) {
        //<editor-fold defaultstate="collapsed" desc="create ShaderProgram from resource">
        this();
        createVertexShader(vs);
        createFragmentShader(fs);
        link();
        //</editor-fold>
    }

    public ShaderProgram(CharSequence vs, CharSequence fs) {
        //<editor-fold desc="create ShaderProgram from source">
        this();
        createVertexShader(vs);
        createFragmentShader(fs);
        link();
        //</editor-fold>
    }

    public void createUniform(String name) {
        //<editor-fold  defaultstate="collapsed" desc="create variable in cache from uniform variable location">
        getLocation(name);
        //</editor-fold>
    }

    public int getLocation(String name) {
        //<editor-fold desc="getLocation from uniform variable">
        if (uniform.containsKey(name))
            return uniform.get(name);

        int result = glGetUniformLocation(id, name);
        if (result < 0)
            throw new RuntimeException("Could not find uniform variable [" + name + "]");

        uniform.put(name, result);
        return result;
        //</editor-fold>
    }


    public void createVertexShader
        //<editor-fold defaultstate="collapsed" desc="createVertexShader from source or resource">
    (String resource) {
        vsId = createShader(resource, GL_VERTEX_SHADER);
        glAttachShader(id, vsId);
    }

    public void createVertexShader(CharSequence source) {
        vsId = createShader(source, GL_VERTEX_SHADER);
        glAttachShader(id, vsId);
    }
    //</editor-fold>


    public void createFragmentShader
        //<editor-fold desc="createFragmentShader from source or resource">
    (String resource) {
        fsId = createShader(resource, GL_FRAGMENT_SHADER);
        glAttachShader(id, fsId);
    }

    public void createFragmentShader(CharSequence source) {
        fsId = createShader(source, GL_FRAGMENT_SHADER);
        glAttachShader(id, fsId);
    }
    //</editor-fold>

    public void link() {
        //<editor-fold defaultstate="collapsed" desc="link ShaderProgram">
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
        unbind();
        //</editor-fold>
    }

    public void bind() {
        //<editor-fold defaultstate="collapsed" desc="bind Shader">
        glUseProgram(id);
        //</editor-fold>
    }

    public void unbind() {
        //<editor-fold defaultstate="collapsed" desc="unbind Shader">
        glUseProgram(0);
        //</editor-fold>
    }

    public static void cleanUp() {
        //<editor-fold defaultstate="collapsed" desc="cleanUp all Shades">
        while (!shades.isEmpty()) {
            deleteShader(shades.get(0));
        }
        //</editor-fold>
    }

    public static ShaderProgram deleteShader(ShaderProgram shader) {
        //<editor-fold defaultstate="collapsed" desc="deleteShader">
        shader.unbind();
        if (shader.id > 0) {
            if (shader.vsId > 0) {
                glDetachShader(shader.id, shader.vsId);
                glDeleteShader(shader.vsId);
            }
            shader.vsId = -1;

            if (shader.fsId > 0) {
                glDetachShader(shader.id, shader.fsId);
                glDeleteShader(shader.fsId);
                shader.fsId = -1;
            }
            glDeleteProgram(shader.id);
            shader.id = -1;
            shader.uniform.clear();
            shades.remove(shader);
            shader = null;
        }
        return shader;
        //</editor-fold>
    }

    public int createShader(String resource, int shaderType) {
        //<editor-fold desc="int createShader from resource">
        int shaderId = glCreateShader(shaderType);
        if (shaderId < 1) {
            throw new RuntimeException("Error creating shader, log: " + glGetShaderInfoLog(shaderId));
        }
        ByteBuffer source = ioResourceToByteBuffer(resource);

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
        //</editor-fold>
    }

    protected int createShader(CharSequence source, int shaderType) {
        // <editor-fold defaultstate="collapsed" desc="int CreateShader from source">
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
        //</editor-fold>
    }
}
