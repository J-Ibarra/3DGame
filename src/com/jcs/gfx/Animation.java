package com.jcs.gfx;

import com.jcs.utils.Loaders.OBJLoader;

/**
 * Created by Jcs on 18/9/2016.
 */
public class Animation {

    private Mesh[] meshes;

    public Animation(OBJLoader.Model[] models) {
        meshes = new Mesh[models.length];
        for (int i = 0; i < models.length; i++) {
            meshes[i] = new Mesh(models[i]);
        }
    }

    public int fps = 0;
    public int i = 0;

    public void draw() {
        meshes[fps].draw();
        i++;

        if (i % 3 == 0) {
            fps++;
            if (fps == meshes.length)
                fps = 0;
        }
    }
}
