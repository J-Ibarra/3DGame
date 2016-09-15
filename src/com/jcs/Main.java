package com.jcs;

import com.jcs.test.OBJLoader;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import static com.jcs.Mesh.Data;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;


public class Main {

    private boolean running = false;

    // The window handle
    private long window;
    private String tittle = "Hello World!";
    private int width;
    private int height;

    public boolean[] keys = new boolean[GLFW_KEY_LAST + 1];

    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWScrollCallback scrollCallback;
    private GLFWWindowCloseCallback windowCloseCallback;
    private GLFWFramebufferSizeCallback framebufferSizeCallback;

    private ShaderProgram shader;

    private Camera camera;

    private Mesh mesh0;
    private Mesh mesh1;
    private Mesh mesh2;
    private Mesh mesh3;

    private Mesh mesh4;

    Texture texture0;
    Texture texture1;
    Texture texture2;

    Matrix4f model;
    Matrix4f view;
    Matrix4f projection;
    Matrix4f ortho;

    private void init() {
        camera = new Camera();

        shader = new ShaderProgram("test/shader.vs", "test/shader.fs");
        shader.createUniform("model");
        shader.createUniform("view");
        shader.createUniform("projection");
        shader.createUniform("ourTexture");

        initMesh0();
        initMesh1();
        initMesh2();
        initMesh3();
        mesh4 = new Mesh(OBJLoader.loadOBJ("test/stall.obj"));

        texture0 = Texture.createClassTexture("test/texture.jpg");
        texture1 = Texture.createClassTexture("test/grassblock.png");
        texture2 = Texture.createClassTexture("test/stallTexture.png");

        model = new Matrix4f().translate(0.0f, 0.0f, -3.0f);
        view = new Matrix4f();
        projection = new Matrix4f().setPerspective((float) Math.toRadians(camera.zoom), width / height, 0.01f, 100.0f);
        ortho = new Matrix4f().setOrtho2D(0, width, height, 0);

        config();
    }

    private void config() {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glEnable(GL_DEPTH_TEST);
    }

    private void initMesh0() {
        mesh0 = new Mesh();
    }

    private void initMesh1() {
        float[] vertices = new float[]{
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,

                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,

                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
        };

        float[] texture = new float[]{
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,

                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,

                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,

                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 1.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 1.0f
        };

        Data v = new Data(0, 3, vertices);
        Data t = new Data(1, 2, texture);
        mesh1 = new Mesh(new Data[]{v, t});

    }

    private void initMesh2() {
        float[] positions = new float[]{
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,
                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,
                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,};
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,
                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,
                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,};
        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};

        Data vert = new Data(0, 3, positions);
        Data text = new Data(1, 2, textCoords);
        Mesh.Data[] data = new Mesh.Data[]{vert, text};

        mesh2 = new Mesh(data, indices);
    }

    private void initMesh3() {
        mesh3 = new Mesh(OBJLoader.loadOBJ("test/untitled.obj"));
    }

    private void initCallbacks() {

        glfwSetWindowCloseCallback(window, windowCloseCallback = new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long window) {
                running = false;
            }
        });

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {

                if (key == GLFW_KEY_UNKNOWN)
                    return;

                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    running = false;

                keys[key] = action != GLFW_RELEASE;
            }
        });


        glfwSetFramebufferSizeCallback(window, framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Main.this.width = width;
                Main.this.height = height;
                projection.setPerspective((float) Math.toRadians(camera.zoom), width / height, 0.01f, 100.0f);
                ortho = new Matrix4f().setOrtho2D(0, width, height, 0);
                glViewport(0, 0, width, height);
            }
        });

        glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {

                camera.processMouseScroll((float) yoffset);

                projection.setPerspective((float) Math.toRadians(camera.zoom), width / height, 0.01f, 100.0f);
            }
        });

        glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                camera.processMouseMovement((float) xpos / width, (float) ypos / height);
            }
        });

    }

    private void update(float delta) {

        float move = delta;
        if (keys[GLFW_KEY_LEFT_SHIFT])
            move *= 2.0f;
        if (keys[GLFW_KEY_LEFT_CONTROL])
            move *= 0.5f;

        camera.update(move);

        if (keys[GLFW_KEY_W])
            camera.processKeyboard(Camera.Movement.FORWARD);
        if (keys[GLFW_KEY_S])
            camera.processKeyboard(Camera.Movement.BACKWARD);
        if (keys[GLFW_KEY_A])
            camera.processKeyboard(Camera.Movement.LEFT);
        if (keys[GLFW_KEY_D])
            camera.processKeyboard(Camera.Movement.RIGHT);
        if (keys[GLFW_KEY_SPACE])
            camera.processKeyboard(Camera.Movement.UP);
        if (keys[GLFW_KEY_LEFT_ALT])
            camera.processKeyboard(Camera.Movement.DOWN);

        q.rotateAxis(delta, vec);

        shader.bind();
        glUniformMatrix4fv(shader.getLocation("projection"), false, projection.get(fb));
        glUniformMatrix4fv(shader.getLocation("view"), false, camera.getViewMatrix().get(fb));
        glUniform1i(shader.getLocation("ourTexture"), 0);
        shader.unbind();
    }

    Random r = new Random();
    Vector3f vec = new Vector3f(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1);
    Quaternionf q = new Quaternionf();
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    private void render() {
        glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        shader.bind();

        texture0.bind(0);
        glUniformMatrix4fv(shader.getLocation("model"), false, model.identity().translate(-4.0f, 0.0f, -3.0f).rotate(q).get(fb));
        mesh0.draw();

        glUniformMatrix4fv(shader.getLocation("model"), false, model.identity().translate(-2.0f, 0.0f, -3.0f).rotate(q).get(fb));
        mesh1.draw();

        texture1.bind(0);
        glUniformMatrix4fv(shader.getLocation("model"), false, model.identity().translate(0.0f, 0.0f, -3.0f).rotate(q).get(fb));
        mesh2.draw();

        glUniformMatrix4fv(shader.getLocation("model"), false, model.identity().translate(2.0f, 0.0f, -3.0f).rotate(q).get(fb));
        mesh3.draw();

        texture2.bind(0);
        Quaternionf qq = new Quaternionf().rotate(0,(float)Math.toRadians(180),0);
        glUniformMatrix4fv(shader.getLocation("model"), false, model.identity().translate(0.0f, 0.0f, -8.0f).rotate(qq).get(fb));
        mesh4.draw();

        shader.unbind();

        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(projection.get(fb));
        glMatrixMode(GL_MODELVIEW);
        glLoadMatrixf(camera.getViewMatrix().get(fb));
        renderGrid();

        Font.render(text, new Vector2f(1f), ortho);
        Font.render("xPos: " + camera.pos.x, new Vector2f(1, 10), ortho);
        Font.render("yPos: " + camera.pos.y, new Vector2f(1, 20), ortho);
        Font.render("zPos: " + camera.pos.z, new Vector2f(1, 30), ortho);
        Font.render("xRot: " + camera.q.x, new Vector2f(1, 40), ortho);
        Font.render("yRot: " + camera.q.y, new Vector2f(1, 50), ortho);
    }

    private int dl = -1;

    private void renderGrid() {
        if (dl == -1) {
            dl = glGenLists(1);
            glNewList(dl, GL_COMPILE);
            glBegin(GL_LINES);
            glColor3f(0f, 0f, 1f);

            int gridSize = 40;
            float ceiling = 3.0f;

            for (int i = -gridSize; i <= gridSize; i++) {
                glVertex3f(-gridSize, 0.0f, i);
                glVertex3f(gridSize, 0.0f, i);
                glVertex3f(i, 0.0f, -gridSize);
                glVertex3f(i, 0.0f, gridSize);
            }
            glColor3f(1f, 1f, 0f);
            for (int i = -gridSize; i <= gridSize; i++) {
                glVertex3f(-gridSize, ceiling, i);
                glVertex3f(gridSize, ceiling, i);
                glVertex3f(i, ceiling, -gridSize);
                glVertex3f(i, ceiling, gridSize);
            }
            glEnd();
            glEndList();
        }
        glCallList(dl);
    }

    private void finish() {
        ShaderProgram.cleanUp();
        Mesh.cleanUp();

        /* <-- --> */
        keyCallback.free();
        cursorPosCallback.free();
        scrollCallback.free();
        windowCloseCallback.free();
        framebufferSizeCallback.free();
    }

    private int i = 0;
    String text = "";

    private void oneSecond(int ups, int fps) {
        text = "ups: " + ups + ", fps: " + fps;

        glfwSetWindowTitle(window, tittle + " || ups: " + ups + ", fps: " + fps);
        i++;
        if (i % 5 == 0)
            vec = new Vector3f(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1);
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
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE); // the window will be decorate

        // Get the resolution of the primary monitor
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidmode = glfwGetVideoMode(monitor);

        //int iWidth = vidmode.width();
        //int iHeight = vidmode.height();
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

            initGLFW();
            init();
            initCallbacks();

            loop();

            // Free the window callbacks and destroy the window
            //glfwFreeCallbacks(window);
            glfwDestroyWindow(window);

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
