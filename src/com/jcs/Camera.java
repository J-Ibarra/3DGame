package com.jcs;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Created by Jcs on 9/9/2016.
 */
public class Camera {


    public enum Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private float MAX_ZOOM = 120f;
    private float MIN_ZOOM = 5f;

    public Matrix4f viewMatrix = new Matrix4f();

    Vector3f pos = new Vector3f();
    Quaternionf q = new Quaternionf();

    Vector3f dir = new Vector3f();
    Vector3f right = new Vector3f();
    Vector3f up = new Vector3f();


    float movementSpeed = 3.0f;
    public float mouseSensitivity = 0.5f;

    public float zoom = 60f;

    public Camera() {

    }

    public Camera(Vector3f pos) {
        this();
        this.pos = pos;
    }

    public void update(float move) {
        move *= movementSpeed;
        viewMatrix.positiveZ(dir).negate().mul(move);
        viewMatrix.positiveX(right).mul(move);
        viewMatrix.positiveY(up).mul(move);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().rotate(q).translate(pos.negate(new Vector3f()));
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

    public void processMouseMovement(float xPos, float yPos) {
        q.identity().rotateX(yPos * mouseSensitivity).rotateY(xPos * mouseSensitivity);
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

}
