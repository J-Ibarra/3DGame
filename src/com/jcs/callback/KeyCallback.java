package com.jcs.callback;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Created by Jcs on 9/9/2016.
 */
public class KeyCallback extends GLFWKeyCallback {

    public boolean[] keys = new boolean[GLFW_KEY_LAST];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
    }
}
