package com.jcs.gfx.camera;

import com.jcs.gfx.Mesh;
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
    public boolean r = false;
    public int i = 0;

    public void draw() {
        meshes[fps].draw();
        i++;

        if (i % 3 == 0) {
            fps++;
            if (fps == meshes.length)
                fps = 0;
        }

        /*if (i % 3 == 0) {
            if (!r)
                fps++;
            else
                fps--;
            if (fps >= meshes.length - 1)
                r = true;
            if (fps <= 1)
                r = false;
        }*/
    }
}
