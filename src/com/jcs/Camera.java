package com.jcs;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Created by Jcs on 9/9/2016.
 */
public class Camera {


    enum Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    public Matrix4f viewMatrix;

    Vector3f pos = new Vector3f();
    Quaternionf q = new Quaternionf();

    Vector3f dir = new Vector3f();
    Vector3f right = new Vector3f();
    Vector3f up = new Vector3f();


    float movementSpeed = 3.0f;
    public float mouseSensitivity = 0.5f;

    public float zoom;

    public Camera() {
        zoom = 60f;
        viewMatrix = new Matrix4f();
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

    void processMouseMovement(float xpos, float ypos) {
        q.identity().rotateX(ypos * mouseSensitivity).rotateY(xpos * mouseSensitivity);
    }

    public void processMouseScroll(float yoffset) {
        if (yoffset < 0)
            zoom *= 1.05;
        else
            zoom /= 1.05f;

        if (zoom < 10.0f)
            zoom = 10.0f;
        if (zoom > 60.0f)
            zoom = 60.0f;
    }

}
