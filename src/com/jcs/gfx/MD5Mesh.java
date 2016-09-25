package com.jcs.gfx;

import com.jcs.utils.loaders.md5.MD5JointData;
import com.jcs.utils.loaders.md5.MD5Loader;
import com.jcs.utils.loaders.md5.MD5MeshData;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jcs on 25/9/2016.
 */
public class MD5Mesh {

    public Mesh[] meshes;

    public MD5Mesh(MD5Loader.MD5Model model) {
        meshes = new Mesh[model.meshes.size()];

        for (int i = 0; i < model.meshes.size(); i++) {
            MD5MeshData meshData = model.meshes.get(i);
            Mesh mesh = generateMesh(model.joints, meshData);
            mesh.setTexture(meshData.getTexture());
            meshes[i] = mesh;
        }

    }

    private static Mesh generateMesh(List<MD5JointData> joints, MD5MeshData meshData) {
        List<MD5Loader.VertexInfo> vertexInfoList = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for (MD5MeshData.MD5Vertex vertice : meshData.vertices) {
            Vector3f vertexPos = new Vector3f();
            int startWeight = vertice.startWeight;
            int numWeights = vertice.weightCount;

            for (int i = startWeight; i < startWeight + numWeights; i++) {
                MD5MeshData.MD5Weight weight = meshData.weights.get(i);
                MD5JointData joint = joints.get(weight.jointIndex);
                Vector3f rotatedPos = new Vector3f(weight.position).rotate(joint.orientation);
                Vector3f acumPos = new Vector3f(joint.position).add(rotatedPos);
                acumPos.mul(weight.bias);
                vertexPos.add(acumPos);
            }
            vertexInfoList.add(new MD5Loader.VertexInfo(vertexPos, vertice.textCoords));
        }

        for (MD5MeshData.MD5Triangle triangle : meshData.triangles) {
            indices.add(triangle.vertex0);
            indices.add(triangle.vertex1);
            indices.add(triangle.vertex2);

            // Normals
            /*MD5Loader.VertexInfo v0 = vertexInfoList.get(triangle.vertex0);
            MD5Loader.VertexInfo v1 = vertexInfoList.get(triangle.vertex1);
            MD5Loader.VertexInfo v2 = vertexInfoList.get(triangle.vertex2);
            Vector3f pos0 = v0.position;
            Vector3f pos1 = v1.position;
            Vector3f pos2 = v2.position;

            Vector3f normal = (new Vector3f(pos2).sub(pos0)).cross(new Vector3f(pos1).sub(pos0));
            normal.normalize();

            v0.normal.add(normal).normalize();
            v1.normal.add(normal).normalize();
            v2.normal.add(normal).normalize();*/
        }
        FloatBuffer fbPos = BufferUtils.createFloatBuffer(vertexInfoList.size() * 3);
        FloatBuffer fbTex = BufferUtils.createFloatBuffer(vertexInfoList.size() * 2);
        FloatBuffer fbNor = BufferUtils.createFloatBuffer(vertexInfoList.size() * 3);
        for (int i = 0; i < vertexInfoList.size(); i++) {

            MD5Loader.VertexInfo info = vertexInfoList.get(i);
            fbPos.put(info.position.x);
            fbPos.put(info.position.y);
            fbPos.put(info.position.z);
            fbTex.put(info.textCoords.x);
            fbTex.put(info.textCoords.y);
            /*fbNor.put(info.normal.x);
            fbNor.put(info.normal.y);
            fbNor.put(info.normal.z);*/
        }
        fbPos.flip();
        fbTex.flip();
        fbNor.flip();

        Mesh.Data data0 = new Mesh.Data(0, 3, fbPos);
        Mesh.Data data1 = new Mesh.Data(1, 2, fbTex);
        Mesh.Data data2 = new Mesh.Data(2, 3, fbNor);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();

        return new Mesh(new Mesh.Data[]{data0, data1, data2}, indicesArr);
    }

    public void draw() {
        for (int i = 0; i < meshes.length; i++) {
            meshes[i].draw(0);
        }
    }
}
