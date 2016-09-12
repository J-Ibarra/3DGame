package com.jcs.test;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;

/**
 * Created by Jcs on 11/9/2016.
 */
public class Font {
    public static String text = "Font Hud Demo, Hello Word!!";
    private static ByteBuffer charBuffer;
    private static int quads;

    public static void render(float scale, Vector3f color, Vector3f pos, FloatBuffer fb) {
        charBuffer = BufferUtils.createByteBuffer(text.length() * 270);
        quads = stb_easy_font_print(0, 0, text, null, charBuffer);

        //glDisable(GL_CULL_FACE);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glLoadMatrixf(fb);
        //glOrtho(0.0, window.getWidth(), window.getHeight(), 0.0, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);

        glColor3f(color.x, color.y, color.z);
        glScalef(scale, scale, scale);
        glPushMatrix();
        glTranslated(pos.x, pos.y, 0.0f);
        glDrawArrays(GL_QUADS, 0, quads * 4);
        glPopMatrix();

        charBuffer.clear();
        glDisableClientState(GL_VERTEX_ARRAY);
        //glEnable(GL_CULL_FACE);
    }
}
