package com.jcs.gfx.camera;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Created by Jcs on 9/9/2016.
 */
public class FreeCamera {

    public enum Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        UP,
        DOWN,
    }

    private final float MAX_ZOOM = 120f;
    private final float MIN_ZOOM = 5f;
    public final float PIHalf = (float) Math.PI * 0.5f;

    public Matrix4f viewMatrix = new Matrix4f();

    public Vector3f pos = new Vector3f();
    public Quaternionf q = new Quaternionf();

    public Vector3f dir = new Vector3f();
    public Vector3f right = new Vector3f();
    public Vector3f up = new Vector3f();


    public float movementSpeed = 3.0f;
    public float mouseSensitivity = 0.5f;

    public float zoom = 60f;
    public int width;
    public int height;

    public FreeCamera() {

    }

    public FreeCamera(Vector3f pos) {
        this();
        this.pos = pos;
    }

    public FreeCamera(Vector3f pos, int width, int height) {
        this(pos);
        this.width = width;
        this.height = height;
    }

    public void update(float move) {
        move *= movementSpeed;
        viewMatrix.positiveZ(dir).negate().mul(move);
        viewMatrix.positiveX(right).mul(move);
        viewMatrix.positiveY(up).mul(move);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().rotate(q)
                .translate(-pos.x, -pos.y, -pos.z);

    }

    public void processKeyboard(Movement direction) {
        if (direction == Movement.FORWARD)
            pos.add(dir);
        if (direction == Movement.BACKWARD)
            pos.sub(dir);
        if (direction == Movement.LEFT)
            pos.sub(right);
        if (direction == Movement.RIGHT)
            pos.add(right);
        if (direction == Movement.UP)
            pos.add(up);
        if (direction == Movement.DOWN)
            pos.sub(up);
    }

    public void processMouseMovement(long window, float xPos, float yPos) {
        float xRot = (xPos / width) * mouseSensitivity;
        float yRot = (yPos / height) * mouseSensitivity;

        q.identity().rotateX(yRot).rotateY(xRot);
    }

    public void processMouseScroll(float yOffSet) {
        if (yOffSet < 0)
            zoom *= 1.05;
        else
            zoom /= 1.05f;

        if (zoom < MIN_ZOOM)
            zoom = MIN_ZOOM;
        if (zoom > MAX_ZOOM)
            zoom = MAX_ZOOM;
    }

    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
