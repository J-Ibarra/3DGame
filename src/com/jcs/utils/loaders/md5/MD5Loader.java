package com.jcs.utils.loaders.md5;

import com.jcs.gfx.Mesh;
import com.jcs.gfx.Texture;
import com.jcs.gfx.GameItem;
import com.jcs.utils.IOUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jcs on 25/9/2016.
 */
public class MD5Loader {

    public static class MD5Model {
        public final MD5Header header;
        public final List<MD5Joint> joints;
        public final List<MD5Mesh> meshes;

        public MD5Model(MD5Header header, List<MD5Joint> joints, List<MD5Mesh> meshes) {
            this.header = header;
            this.joints = joints;
            this.meshes = meshes;
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

    public static MD5Model loadMD5(String resource) {
        List<String> strings = IOUtils.ioResourceToListString(resource);
        MD5Header header = null;
        List<MD5Joint> joints = null;
        List<MD5Mesh> meshes = new ArrayList<>();

        int numLines = strings != null ? strings.size() : -1;
        if (numLines == -1)
            throw new RuntimeException("could not processModel the resource");

        int start = -1;
        for (int i = 0; i < strings.size(); i++) {
            String line = strings.get(i).trim();
            if (line.endsWith("{")) {
                start = i;
                break;
            }
        }

        if (start == -1)
            throw new RuntimeException("could not read the header");

        header = MD5Header.process(strings.subList(0, start));
        MD5Mesh mesh = null;

        int type = -1;
        for (int i = start; i < strings.size(); i++) {
            String line = strings.get(i).trim();
            if (line.endsWith("{")) {
                if (line.contains("joints")) {
                    joints = new ArrayList<>();
                    type = 0;
                    continue;
                } else if (line.contains("mesh")) {
                    mesh = new MD5Mesh();
                    type = 1;
                }
            } else if (line.endsWith("}")) {
                if (type == 1)
                    meshes.add(mesh);
                type = -1;
            } else {
                if (type == 0) {
                    joints.add(MD5Joint.processLine(line));
                } else if (type == 1) {
                    mesh.processLine(line);
                }
            }
        }
        if (header == null || joints == null || meshes.size() == 0 || mesh == null)
            throw new RuntimeException("");
        return new MD5Model(header, joints, meshes);
    }

    public static GameItem processModel(MD5Model md5Model) {
        List<Mesh> list = new ArrayList<>();
        for (MD5Mesh md5Mesh : md5Model.meshes) {
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
        List<MD5Mesh.MD5Vertex> vertices = md5Mesh.vertices;
        List<MD5Mesh.MD5Weight> weights = md5Mesh.weights;
        List<MD5Joint> joints = md5Model.joints;

        for (MD5Mesh.MD5Vertex vertex : vertices) {
            Vector3f vertexPos = new Vector3f();
            Vector2f vertexTextCoords = vertex.textCoords;
            int startWeight = vertex.startWeight;
            int numWeights = vertex.weightCount;

            for (int i = startWeight; i < startWeight + numWeights; i++) {
                MD5Mesh.MD5Weight weight = weights.get(i);
                MD5Joint joint = joints.get(weight.jointIndex);
                Vector3f rotatedPos = new Vector3f(weight.position).rotate(joint.orientation);
                Vector3f acumPos = new Vector3f(joint.position).add(rotatedPos);
                acumPos.mul(weight.bias);
                vertexPos.add(acumPos);
            }

            vertexInfoList.add(new VertexInfo(vertexPos, vertexTextCoords));
        }

        for (MD5Mesh.MD5Triangle tri : md5Mesh.triangles) {
            indices.add(tri.vertex0);
            indices.add(tri.vertex1);
            indices.add(tri.vertex2);

            // Normals
            VertexInfo v0 = vertexInfoList.get(tri.vertex0);
            VertexInfo v1 = vertexInfoList.get(tri.vertex1);
            VertexInfo v2 = vertexInfoList.get(tri.vertex2);
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
        return new Mesh(new Mesh.Data[]{data0, data1, data2}, indicesArr);
    }

    private static void handleTexture(Mesh mesh, MD5Mesh md5Mesh) {
        String texturePath = md5Mesh.texture;
        if (texturePath != null && texturePath.length() > 0) {
            String t = texturePath.substring(texturePath.lastIndexOf("/"));
            Texture texture = Texture.createClassTexture("test/animation" + t);
            mesh.setTexture(texture);
        }
    }


}
