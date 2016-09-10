package com.jcs;

import com.jcs.callback.KeyCallback;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class Main {

    private boolean running = false;

    // The window handle
    private long window;
    private String tittle = "Hello World!";
    private int width;
    private int height;

    private KeyCallback keyCallback;

    private GLFWWindowCloseCallback windowCloseCallback;
    private GLFWFramebufferSizeCallback framebufferSizeCallback;

    private void init() throws Exception {

    }

    private void initCallbacks() throws Exception {

        glfwSetWindowCloseCallback(window, windowCloseCallback = new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long window) {
                running = false;
            }
        });

        glfwSetKeyCallback(window, keyCallback = new KeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {

                if (key == GLFW_KEY_UNKNOWN)
                    return;

                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    running = false;

                super.invoke(window, key, scancode, action, mods);
            }
        });


        glfwSetFramebufferSizeCallback(window, framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Main.this.width = width;
                Main.this.height = height;
                glViewport(0, 0, width, height);
            }
        });

    }

    private void update(float delta) {

    }

    private void render() {

    }

    private void finish() {
        windowCloseCallback.free();
        framebufferSizeCallback.free();
        keyCallback.free();

    }

    private void oneSecond(int ups, int fps) {
        glfwSetWindowTitle(window, tittle + " || ups: " + ups + ", fps: " + fps);
    }

    private void loop() {

        int ups = 0, fps = 0;
        glfwSetTime(0);

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        double lastTime = glfwGetTime();
        double lastTimer = glfwGetTime();

        while (running) {

            double currentTime = glfwGetTime();
            float deltaTime = (float) (currentTime - lastTime);
            lastTime = currentTime;

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            ups++;
            update(deltaTime);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            fps++;
            render();
            glfwSwapBuffers(window); // swap the color buffers

            if (glfwGetTime() - lastTimer > 1) {
                oneSecond(ups, fps);
                ups = fps = 0;
                lastTimer = glfwGetTime();
            }
        }

        glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
        finish();
    }

    private void initGLFW() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Get the resolution of the primary monitor
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);

        int iWidth = 480;
        int iHeight = 360;

        // Create the window
        window = glfwCreateWindow(iWidth, iHeight, tittle, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        IntBuffer framebufferSize = BufferUtils.createIntBuffer(2);
        nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
        width = framebufferSize.get(0);
        height = framebufferSize.get(1);

        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - iWidth) / 2,
                (vidmode.height() - iHeight) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        try {
            running = true;
            init();
            initGLFW();
            initCallbacks();

            loop();

            // Free the window callbacks and destroy the window
            //glfwFreeCallbacks(window);
            glfwDestroyWindow(window);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Terminate GLFW and free the error callback
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello Game!!");
        new Main().run();
    }
}
