package com.jcs.gfx.camera;

import com.jcs.gfx.Mesh;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Created by Jcs on 25/9/2016.
 */
public class GameItem {

    private Mesh[] meshes;

    private Matrix4f model;
    public Vector3f position;
    public Quaternionf rotation;
    public float scale;


    private GameItem() {
        model = new Matrix4f();
        position = new Vector3f(0f);
        rotation = new Quaternionf();
        scale = 1f;
    }

    public GameItem(Mesh[] meshes) {
        this();
        this.meshes = meshes;
    }

    public GameItem(Mesh mesh) {
        this(new Mesh[]{mesh});
    }

    public Matrix4f getModelMatrix() {
        return new Matrix4f().translate(position).rotate(rotation).scale(scale);
    }

    public void draw(int sampler) {
        for (Mesh mesh : meshes) {
            mesh.draw(sampler);
        }
    }
}
