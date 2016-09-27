package com.jcs.gfx;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Created by Jcs on 25/9/2016.
 */
public class GameItem {

    private Mesh[] meshes;

    private Matrix4f model;

    private Vector3f position;
    private Quaternionf rotation;
    private float scale;

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

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        calculateModelMatrix();
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
        calculateModelMatrix();
    }

    public void setScale(float scale) {
        this.scale = scale;
        calculateModelMatrix();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    private void calculateModelMatrix() {
        model.identity().translate(position).rotate(rotation).scale(scale);
    }

    public Matrix4f getModelMatrix() {
        return model;
    }

    public void draw(int sampler) {
        for (Mesh mesh : meshes) {
            mesh.draw(sampler);
        }
    }
}
