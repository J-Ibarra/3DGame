package com.jcs;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Jcs on 9/9/2016.
 */
public class Camera {

    enum Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }


    // Default camera values
    public static final float YAW = -90.0f;
    public static final float PITCH = 0.0f;
    public static final float SPEED = 3.0f;
    public static final float SENSITIVITY = 0.25f;
    public static final float ZOOM = 60f;

    public Matrix4f viewMatrix;

    // Camera Attributes
    public Vector3f position;
    public Vector3f front;
    public Vector3f up;
    public Vector3f right;
    public Vector3f worldUp;
    // Eular Angles
    public float yaw;
    public float pitch;
    // Camera options
    public float movementSpeed;
    public float mouseSensitivity;
    public float zoom;

    public Camera() {
        position = new Vector3f(0f);
        worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
        yaw = YAW;
        pitch = PITCH;
        front = new Vector3f(0.0f, 0.0f, -1.0f);
        movementSpeed = SPEED;
        mouseSensitivity = SENSITIVITY;
        zoom = ZOOM;
        up = new Vector3f(0f);
        right = new Vector3f(0f);

        viewMatrix = new Matrix4f();

        updateCameraVectors();
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix.setLookAt(position, position.add(front, new Vector3f()), up);
    }

    public void processKeyboard(Movement direction, float deltaTime) {
        float velocity = movementSpeed * deltaTime;
        if (direction == Movement.FORWARD)
            position.add(front.mul(velocity, new Vector3f()));
        if (direction == Movement.BACKWARD)
            position.sub(front.mul(velocity, new Vector3f()));
        if (direction == Movement.LEFT)
            position.sub(right.mul(velocity, new Vector3f()));
        if (direction == Movement.RIGHT)
            position.add(right.mul(velocity, new Vector3f()));
    }

    private boolean firstMouse = true;
    double lastX, lastY;

    void processMouseMovement(double xpos, double ypos) {
        if (firstMouse) {
            lastX = xpos;
            lastY = ypos;
            firstMouse = false;
        }

        float xoffset = (float) (xpos - lastX);
        float yoffset = (float) (lastY - ypos);
        lastX = xpos;
        lastY = ypos;

        xoffset *= mouseSensitivity;
        yoffset *= mouseSensitivity;

        yaw += xoffset;
        pitch += yoffset;

        if (pitch > 89.0f)
            pitch = 89.0f;
        if (pitch < -89.0f)
            pitch = -89.0f;

        // Update Front, Right and Up Vectors using the updated Eular angles
        updateCameraVectors();
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

    /**
     * Calculates the front vector from the Camera's (updated) Eular Angles
     */
    private void updateCameraVectors() {
        // Calculate the new Front vector
        Vector3f frontAux = new Vector3f();
        frontAux.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        frontAux.y = (float) Math.sin(Math.toRadians(pitch));
        frontAux.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front = frontAux.normalize();

        // Also re-calculate the Right and Up vector
        right = front.cross(worldUp, right).normalize(); //glm::normalize(glm::cross(this->Front, this->WorldUp));  // Normalize the vectors, because their length gets closer to 0 the more you look up or down which results in slower movement.
        up = right.cross(front, up).normalize(); //glm::normalize(glm::cross(this->Right, this->Front));

    }
}
