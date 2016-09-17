package com.jcs.gfx.camera;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

/**
 * Created by Jcs on 17/9/2016.
 */
public class FirstPerson extends FreeCamera {

    public float yPos = 0f;
    public Vector3f vAux = new Vector3f();

    public FirstPerson() {
        super();
    }

    public FirstPerson(Vector3f pos) {
        super(pos);
    }

    public FirstPerson(Vector3f pos, int width, int height) {
        super(pos, width, height);
        yPos = this.pos.y;
    }


    Quaternionf qAux = new Quaternionf().rotateY(-PIHalf);

    @Override
    public void update(float move) {
        move *= movementSpeed;
        viewMatrix.positiveX(dir).negate().mul(move).rotate(qAux);
        viewMatrix.positiveX(right).mul(move);
    }

    @Override
    public void processKeyboard(Movement direction) {
        super.processKeyboard(direction);
    }

    @Override
    public void processMouseMovement(long window, float xPos, float yPos) {
        float xRot = (xPos / width) * mouseSensitivity;
        float yRot = (yPos / height) * mouseSensitivity;

        if (yRot < -PIHalf) {
            yRot = -PIHalf;
            glfwSetCursorPos(window, xPos, yRot * height / mouseSensitivity);
        }

        if (yRot > PIHalf) {
            yRot = PIHalf;
            glfwSetCursorPos(window, xPos, yRot * height / mouseSensitivity);
        }

        q.identity().rotateX(yRot).rotateY(xRot);
    }
}
