package com.jcs.gfx;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.BufferUtils.createFloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;

/**
 * Created by Jcs on 11/9/2016.
 */
public class Font {


    public static void render(String text, Vector3f color, Vector2i pos, Matrix4f ortho) {

        ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 380);
        int quads = stb_easy_font_print(0, 0, text, null, charBuffer);

        FloatBuffer fb = new Matrix4f(ortho)
                .translate(pos.x, pos.y, 0.0f)
                .get(createFloatBuffer(16));

        shader.bind();
        GL20.glUniformMatrix4fv(shader.getLocation("viewProjMatrix"), false, fb);
        GL20.glUniform3fv(shader.getLocation("color"), color.get(createFloatBuffer(3)));
        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);
        glDrawArrays(GL_QUADS, 0, quads * 4);
        glDisableClientState(GL_VERTEX_ARRAY);
        shader.unbind();
    }

    public static void render(String text, int x, int y, Matrix4f ortho) {
        render(text, new Vector2i(x, y), ortho);
    }

    public static void render(String text, Vector2i pos, Matrix4f ortho) {
        render(text, new Vector3f(1f), pos, ortho);
    }

    private static final CharSequence vs =
            "uniform mat4 viewProjMatrix;" +
                    "void main() {" +
                    "  gl_Position = viewProjMatrix * gl_Vertex;" +
                    "}";

    private static final CharSequence fs =
            "uniform vec3 color;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(color, 1.0);" +
                    "}";

    private static ShaderProgram shader = new ShaderProgram(vs, fs);

    static {
        shader.createUniform("viewProjMatrix");
        shader.createUniform("color");
    }


}
