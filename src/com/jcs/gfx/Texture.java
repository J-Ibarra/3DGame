package com.jcs.gfx;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.jcs.utils.IOUtils.ioResourceToByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

/**
 * Created by Jcs on 9/9/2016.
 */
public class Texture {

    public int id = -1;
    public int width = -1;
    public int height = -1;

    //<editor-fold defaultstate="collapsed" desc="variables cache">
    private static List<Texture> textures = new ArrayList<>();
    //</editor-fold>

    private Texture(int[] data) {
        //<editor-fold desc="create class texture from data">
        this.id = data[0];
        this.width = data[1];
        this.height = data[2];
        textures.add(this);
        //</editor-fold>
    }

    private static int[] loadTexture(String resource) {
        //<editor-fold desc="load texture from resource">
        ByteBuffer imageBuffer = ioResourceToByteBuffer(resource);

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        // Use info to read image metadata without decoding the entire image.
        // We don't need this for this demo, just testing the API.
        if (!stbi_info_from_memory(imageBuffer, w, h, comp))
            throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());

        // Decode the image
        ByteBuffer image;
        image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
        if (image == null)
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

        int width = w.get(0);
        int height = h.get(0);
        int com = comp.get(0);

        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        if (com == 3) {
            if ((width & 3) != 0)
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (width & 1));
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        } else {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        // Set texture filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        stbi_image_free(image);

        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        return new int[]{texID, width, height};
        //</editor-fold>
    }

    public static Texture createClassTexture(String resource) {
        //<editor-fold desc="createClassTexture from resource">
        return new Texture(loadTexture(resource));
        //</editor-fold>
    }

    public void bind(int sampler) {
        //<editor-fold defaultstate="collapsed" desc="bind sampler Texture">
        if (sampler >= 0 && sampler <= 31) {
            glEnable(GL_TEXTURE_2D);
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        } else
            throw new RuntimeException("Class: Texture;\n\t\t Could not activate sampler: [" + sampler + "]");
        //</editor-fold>
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static void cleanUp() {
        //<editor-fold defaultstate="collapsed" desc="Delete all Texture">
        while (!textures.isEmpty()) {
            deleteTexture(textures.get(0));
        }
        //</editor-fold>
    }

    public static Texture deleteTexture(Texture texture) {
        //<editor-fold defaultstate="collapsed" desc="delete Texture">
        glDeleteTextures(texture.id);
        textures.remove(texture);
        texture.id = -1;
        texture.width = -1;
        texture.height = -1;
        texture = null;
        return texture;
        //</editor-fold>
    }
}
