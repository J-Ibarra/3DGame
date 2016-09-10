package com.jcs;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static com.jcs.utils.IOUtils.ioResourceToByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

/**
 * Created by Jcs on 9/9/2016.
 */
public class Texture {

    public static int createTexture(String path) throws Exception {
        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);

        // Set texture filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        ByteBuffer imageBuffer;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        ByteBuffer image;

        imageBuffer = ioResourceToByteBuffer(path);

        if (!stbi_info_from_memory(imageBuffer, w, h, comp))
            throw new IOException("Failed to read image information: " + stbi_failure_reason());
        image = stbi_load_from_memory(imageBuffer, w, h, comp, 3);
        if (image == null)
            throw new IOException("Failed to load image: " + stbi_failure_reason());

        int width = w.get(0);
        int height = h.get(0);
        int com = comp.get(0);

        if (com == 3) {
            if ((width & 3) != 0)
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (width & 1));
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        } else {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        stbi_image_free(image);

        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        return tex;
    }
}
