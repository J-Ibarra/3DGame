package com.jcs.gfx;

import com.jcs.utils.loaders.obj.OBJLoader;

/**
 * Created by Jcs on 18/9/2016.
 */
public class Animation {

    private Mesh[] meshes;

    public Animation(OBJLoader.OBJModel[] OBJModels) {
        meshes = new Mesh[OBJModels.length];
        for (int i = 0; i < OBJModels.length; i++) {
            meshes[i] = new Mesh(OBJModels[i]);
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
