package com.jcs;

import com.jcs.gfx.*;
import com.jcs.gfx.camera.FirstPerson;
import com.jcs.gfx.camera.FreeCamera;
import com.jcs.gfx.camera.GameItem;
import com.jcs.utils.loaders.md5.MD5Loader;
import com.jcs.utils.loaders.obj.OBJLoader;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.Random;

import static com.jcs.gfx.Mesh.Data;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
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
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;
    private GLFWWindowCloseCallback windowCloseCallback;
    private GLFWFramebufferSizeCallback framebufferSizeCallback;

    private ShaderProgram shader;

    private FirstPerson camera;

    private GameItem gameItem0;
    private GameItem gameItem1;
    private GameItem gameItem2;
    private GameItem gameItem3;
    private GameItem gameItem4;
    private GameItem gameItem5;
    private GameItem gameItem6;

    private GameItem monster;

    Texture texture0;
    Texture texture1;

    Animation animation;
    Texture texture5;

    Matrix4f model;
    Matrix4f view;
    Matrix4f projection;
    Matrix4f ortho;

    private void init() {

        MD5Loader.MD5Model md5MeshModel = MD5Loader.loadMD5("test/animation/boblamp.md5mesh");
        monster = MD5Loader.processModel(md5MeshModel);
        monster.scale = 0.25f;
        monster.position.set(0f, 0f, 3f);
        monster.rotation.set(new Quaternionf().rotate((float) -Math.toRadians(90), 0, 0));

        //<editor-fold defaultstate="collapsed" desc="void init">
        camera = new FirstPerson(new Vector3f(0f, 3f, 0f), width, height);

        shader = new ShaderProgram("test/shader.vs", "test/shader.fs");
        shader.createUniform("model");
        shader.createUniform("view");
        shader.createUniform("projection");
        shader.createUniform("ourTexture");

        texture0 = Texture.createClassTexture("test/texture.jpg");
        texture1 = Texture.createClassTexture("test/grassblock.png");

        gameItem0 = new GameItem(new Mesh(texture0));
        gameItem0.position.set(-4.0f, 0.5f, -3.0f);

        Mesh mesh1 = initMesh1();
        mesh1.setTexture(texture0);
        gameItem1 = new GameItem(mesh1);
        gameItem1.position.set(-2.0f, 0.5f, -3.0f);


        Mesh mesh2 = initMesh2();
        mesh2.setTexture(texture1);
        gameItem2 = new GameItem(mesh2);
        gameItem2.position.set(2.0f, 0.5f, -3.0f);

        Mesh mesh3 = new Mesh(OBJLoader.loadOBJ("test/grassCube.obj"), texture1);
        gameItem3 = new GameItem(mesh3);
        gameItem3.position.set(4.0f, 0.5f, -3.0f);

        Mesh mesh4 = new Mesh(OBJLoader.loadOBJ("test/planet.obj"), "test/planet.png");
        gameItem4 = new GameItem(mesh4);
        gameItem4.position.set(0f, 1f, -3f);
        gameItem4.scale = 0.25f;

        Mesh mesh5 = new Mesh(OBJLoader.loadOBJ("test/stall.obj"), "test/stallTexture.png");
        gameItem5 = new GameItem(mesh5);
        gameItem5.position.set(0.0f, 0.0f, -8.0f);
        gameItem5.rotation.set(new Quaternionf().rotate(0, (float) Math.toRadians(180), 0));

        Mesh mesh6 = new Mesh(OBJLoader.loadOBJ("test/Date_Palm.obj"), "test/Bottom_Trunk.bmp");
        gameItem6 = new GameItem(mesh6);
        gameItem6.position.set(0.0f, 0.0f, 8.0f);
        gameItem6.scale = 0.4f;

        animation = new Animation(OBJLoader.loadAnimationOBJ("test/animation/animation.zip"));
        texture5 = Texture.createClassTexture("test/animation/char.png");

        model = new Matrix4f();
        view = new Matrix4f();
        projection = new Matrix4f().setPerspective((float) Math.toRadians(camera.zoom), width / height, 0.01f, 100.0f);
        ortho = new Matrix4f().setOrtho2D(0, width, height, 0);

        config();
        //</editor-fold>
    }

    private void config() {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glEnable(GL_DEPTH_TEST);
    }

    private Mesh initMesh1() {
        // <editor-fold defaultstate="collapsed" desc="void initMesh1">
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
        return new Mesh(new Data[]{v, t});
        // </editor-fold>
    }

    private Mesh initMesh2() {
        // <editor-fold defaultstate="collapsed" desc="void initMesh2">

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
                // Top OBJFace
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

        return new Mesh(data, indices);
        // </editor-fold>
    }

    private void initCallbacks() {
        // <editor-fold defaultstate="collapsed" desc="void initCallbacks">

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
                camera.setWindowSize(width, height);
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

        glfwSetCursorPos(window, 0d, 0d);
        glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                //xpos = 0;
                //ypos = 0;
                camera.processMouseMovement(window, (float) xpos, (float) ypos);
            }
        });

        glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button == GLFW_MOUSE_BUTTON_3 && action == GLFW_RELEASE) {
                    camera.zoom = 60f;
                    projection.setPerspective((float) Math.toRadians(camera.zoom), width / height, 0.01f, 100.0f);
                }
            }
        });

        // </editor-fold>
    }

    private void update(float delta) {
        //<editor-fold defaultstate="collapsed" desc="Description">
        float move = delta;
        if (keys[GLFW_KEY_LEFT_SHIFT])
            move *= 2.0f;
        if (keys[GLFW_KEY_LEFT_CONTROL])
            move *= 0.5f;

        camera.update(move);

        if (keys[GLFW_KEY_W])
            camera.processKeyboard(FreeCamera.Movement.FORWARD);
        if (keys[GLFW_KEY_S])
            camera.processKeyboard(FreeCamera.Movement.BACKWARD);
        if (keys[GLFW_KEY_A])
            camera.processKeyboard(FreeCamera.Movement.LEFT);
        if (keys[GLFW_KEY_D])
            camera.processKeyboard(FreeCamera.Movement.RIGHT);
        if (keys[GLFW_KEY_SPACE])
            camera.processKeyboard(FreeCamera.Movement.UP);
        if (keys[GLFW_KEY_LEFT_ALT])
            camera.processKeyboard(FreeCamera.Movement.DOWN);

        q.rotateAxis(delta, vec);

        shader.bind();
        glUniformMatrix4fv(shader.getLocation("projection"), false, projection.get(fb));
        glUniformMatrix4fv(shader.getLocation("view"), false, camera.getViewMatrix().get(fb));
        glUniform1i(shader.getLocation("ourTexture"), 0);
        ShaderProgram.unbind();
        //</editor-fold>
    }

    Random r = new Random();
    Vector3f vec = new Vector3f(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1);
    Quaternionf q = new Quaternionf();
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    private void render() {
        //<editor-fold defaultstate="collapsed" desc="Description">
        glClearColor(0.6f, 0.6f, 0.6f, 1.0f);
        shader.bind();

        gameItem0.rotation.set(q);
        glUniformMatrix4fv(shader.getLocation("model"), false, gameItem0.getModelMatrix().get(fb));
        gameItem0.draw(0);

        gameItem1.rotation.set(q);
        glUniformMatrix4fv(shader.getLocation("model"), false, gameItem1.getModelMatrix().get(fb));
        gameItem1.draw(0);

        gameItem2.rotation.set(q);
        glUniformMatrix4fv(shader.getLocation("model"), false, gameItem2.getModelMatrix().get(fb));
        gameItem2.draw(0);

        gameItem3.rotation.set(q);
        glUniformMatrix4fv(shader.getLocation("model"), false, gameItem3.getModelMatrix().get(fb));
        gameItem3.draw(0);

        gameItem4.rotation.set(new Quaternionf().rotateAxis((float) glfwGetTime(), new Vector3f(0, 1, 0)));
        glUniformMatrix4fv(shader.getLocation("model"), false, gameItem4.getModelMatrix().get(fb));
        gameItem4.draw(0);

        glUniformMatrix4fv(shader.getLocation("model"), false, gameItem5.getModelMatrix().get(fb));
        gameItem5.draw(0);

        gameItem6.rotation.set(new Quaternionf().rotateAxis((float) glfwGetTime(), new Vector3f(0, 1f, 0)));
        glUniformMatrix4fv(shader.getLocation("model"), false, gameItem6.getModelMatrix().get(fb));
        gameItem6.draw(0);

        glUniformMatrix4fv(shader.getLocation("model"), false, monster.getModelMatrix().get(fb));
        monster.draw(0);

        texture5.bind(0);
        glUniformMatrix4fv(shader.getLocation("model"), false, model.identity().translate(0.0f, 0.0f, 0.0f).get(fb));
        animation.draw();

        Texture.unbind();
        ShaderProgram.unbind();

        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(projection.get(fb));
        glMatrixMode(GL_MODELVIEW);
        glLoadMatrixf(camera.getViewMatrix().get(fb));
        renderGrid();

        Font.render(text, 10, 10, ortho);
        Font.render("favy: " + camera.zoom, 10, 20, ortho);
        Font.render("Pos: " + camera.pos.toString(new DecimalFormat()), 10, 30, ortho);
        //</editor-fold>
    }

    private int dl = -1;

    private void renderGrid() {
        // <editor-fold defaultstate="collapsed" desc="void renderGrid">

        if (dl == -1) {
            dl = glGenLists(1);
            glNewList(dl, GL_COMPILE);
            glBegin(GL_LINES);
            glColor3f(0f, 0f, 1f);

            int gridSize = 40;
            float ceiling = 6.0f;

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
        // </editor-fold>
    }

    private void finish() {
        //<editor-fold desc="void finish">
        ShaderProgram.cleanUp();
        Mesh.cleanUp();
        Texture.cleanUp();

        /* <-- --> */
        keyCallback.free();
        cursorPosCallback.free();
        mouseButtonCallback.free();
        scrollCallback.free();
        windowCloseCallback.free();
        framebufferSizeCallback.free();
        //</editor-fold>
    }

    private int i = 0;
    String text = "";

    private void oneSecond(int ups, int fps) {
        // <editor-fold defaultstate="collapsed" desc="Body of class Data">
        text = "ups: " + ups + ", fps: " + fps;

        glfwSetWindowTitle(window, tittle + " || ups: " + ups + ", fps: " + fps);
        i++;
        if (i % 5 == 0)
            vec = new Vector3f(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1);
        // </editor-fold>
    }

    private void loop() {
        // <editor-fold defaultstate="collapsed" desc="void Game Loop">
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
        // </editor-fold>
    }

    private void initGLFW() {
        // <editor-fold defaultstate="collapsed" desc="void init GLFW">
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
        // </editor-fold>
    }

    public void run() {
        // <editor-fold defaultstate="collapsed" desc="void run">
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
        // </editor-fold>
    }

    public static void main(String[] args) {
        // <editor-fold defaultstate="collapsed" desc="public static void main">
        System.out.println("Hello Game!!");
        new Main().run();
        // </editor-fold>
    }
}
