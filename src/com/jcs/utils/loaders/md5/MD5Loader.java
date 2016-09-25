package com.jcs.utils.loaders.md5;

import com.jcs.gfx.Mesh;
import com.jcs.gfx.Texture;
import com.jcs.gfx.camera.GameItem;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class MD5Loader {

    private static final String NORMAL_FILE_SUFFIX = "_normal";

    public static GameItem process(MD5Model md5Model) {
        List<MD5Mesh> md5MeshList = md5Model.getMeshes();

        List<Mesh> list = new ArrayList<>();
        for (MD5Mesh md5Mesh : md5Model.getMeshes()) {
            Mesh mesh = generateMesh(md5Model, md5Mesh);
            handleTexture(mesh, md5Mesh);
            list.add(mesh);
        }
        Mesh[] meshes = new Mesh[list.size()];
        meshes = list.toArray(meshes);
        GameItem gameItem = new GameItem(meshes);

        return gameItem;
    }

    private static Mesh generateMesh(MD5Model md5Model, MD5Mesh md5Mesh) {
        List<VertexInfo> vertexInfoList = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        List<MD5Mesh.MD5Vertex> vertices = md5Mesh.getVertices();
        List<MD5Mesh.MD5Weight> weights = md5Mesh.getWeights();
        List<MD5JointInfo.MD5JointData> joints = md5Model.getJointInfo().getJoints();

        for (MD5Mesh.MD5Vertex vertex : vertices) {
            Vector3f vertexPos = new Vector3f();
            Vector2f vertexTextCoords = vertex.getTextCoords();
            int startWeight = vertex.getStartWeight();
            int numWeights = vertex.getWeightCount();

            for (int i = startWeight; i < startWeight + numWeights; i++) {
                MD5Mesh.MD5Weight weight = weights.get(i);
                MD5JointInfo.MD5JointData joint = joints.get(weight.getJointIndex());
                Vector3f rotatedPos = new Vector3f(weight.getPosition()).rotate(joint.getOrientation());
                Vector3f acumPos = new Vector3f(joint.getPosition()).add(rotatedPos);
                acumPos.mul(weight.getBias());
                vertexPos.add(acumPos);
            }

            vertexInfoList.add(new VertexInfo(vertexPos, vertexTextCoords));
        }

        for (MD5Mesh.MD5Triangle tri : md5Mesh.getTriangles()) {
            indices.add(tri.getVertex0());
            indices.add(tri.getVertex1());
            indices.add(tri.getVertex2());

            // Normals
            VertexInfo v0 = vertexInfoList.get(tri.getVertex0());
            VertexInfo v1 = vertexInfoList.get(tri.getVertex1());
            VertexInfo v2 = vertexInfoList.get(tri.getVertex2());
            Vector3f pos0 = v0.position;
            Vector3f pos1 = v1.position;
            Vector3f pos2 = v2.position;

            Vector3f normal = (new Vector3f(pos2).sub(pos0)).cross(new Vector3f(pos1).sub(pos0));
            normal.normalize();

            v0.normal.add(normal).normalize();
            v1.normal.add(normal).normalize();
            v2.normal.add(normal).normalize();
        }

        FloatBuffer fbPos = BufferUtils.createFloatBuffer(vertexInfoList.size() * 3);
        FloatBuffer fbNor = BufferUtils.createFloatBuffer(vertexInfoList.size() * 3);
        FloatBuffer fbTex = BufferUtils.createFloatBuffer(vertexInfoList.size() * 2);
        for (VertexInfo info : vertexInfoList) {
            fbPos.put(info.position.x);
            fbPos.put(info.position.y);
            fbPos.put(info.position.z);
            fbNor.put(info.normal.x);
            fbNor.put(info.normal.y);
            fbNor.put(info.normal.z);
            fbTex.put(info.textCoords.x);
            fbTex.put(info.textCoords.y);
        }
        fbPos.flip();
        fbNor.flip();
        fbTex.flip();

        Mesh.Data data0 = new Mesh.Data(0, 3, fbPos);
        Mesh.Data data1 = new Mesh.Data(1, 2, fbTex);
        Mesh.Data data2 = new Mesh.Data(2, 3, fbNor);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        Mesh mesh = new Mesh(new Mesh.Data[]{data0, data1, data2}, indicesArr);

        return mesh;
    }

    private static void handleTexture(Mesh mesh, MD5Mesh md5Mesh) {
        String texturePath = md5Mesh.getTexture();
        if (texturePath != null && texturePath.length() > 0) {
            String t = texturePath.substring(texturePath.lastIndexOf("/"));
            Texture texture = Texture.createClassTexture("test/animation" + t);
            mesh.setTexture(texture);
        }
    }

    private static class VertexInfo {

        public Vector3f position;
        public Vector2f textCoords;
        public Vector3f normal;

        public VertexInfo(Vector3f position) {
            this.position = position;
            normal = new Vector3f(0f);
            textCoords = new Vector2f(0f);
        }

        public VertexInfo(Vector3f vertexPos, Vector2f vertexTextCoords) {
            this(vertexPos);
            textCoords.set(vertexTextCoords);
        }
    }
}
