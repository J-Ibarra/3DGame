package com.jcs.utils.loaders.md5;

import com.jcs.utils.IOUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jcs on 25/9/2016.
 */
public class MD5Loader {
    public static class MD5Model {
        public final MD5HeaderData header;
        public final List<MD5JointData> joints;
        public final List<MD5MeshData> meshes;

        public MD5Model(MD5HeaderData header, List<MD5JointData> joints, List<MD5MeshData> meshes) {
            this.header = header;
            this.joints = joints;
            this.meshes = meshes;
        }
    }

    public static MD5Model loadMD5(String resource) {
        List<String> strings = IOUtils.ioResourceToListString(resource);

        int start = -1;
        for (int i = 0; i < strings.size(); i++) {
            String line = strings.get(i);
            if (line.endsWith("{")) {
                start = i;
                break;
            }
        }

        if (start == -1)
            throw new RuntimeException("could not find header");

        MD5HeaderData header = MD5HeaderData.process(strings.subList(0, start));
        List<MD5JointData> joints = new ArrayList<>();
        List<MD5MeshData> meshes = new ArrayList<>();
        MD5MeshData mesh = null;
        int type = -1;
        for (int i = start; i < strings.size(); i++) {
            String line = strings.get(i);
            if (line.endsWith("{")) {
                if (line.startsWith("joints")) {
                    type = 0;
                    continue;
                } else if (line.startsWith("mesh")) {
                    type = 1;
                    mesh = new MD5MeshData();
                    continue;
                }
            } else if (line.endsWith("}")) {
                if (type == 1)
                    meshes.add(mesh);
                type = -1;
            } else {
                if (type == 0)
                    joints.add(MD5JointData.processLine(line));
                else if (type == 1) {
                    mesh.processLine(line);
                }
            }
        }

        return new MD5Model(header, joints, meshes);
    }

    public static class VertexInfo {

        public final Vector3f position;
        public Vector3f normal;
        public Vector2f textCoords;

        public VertexInfo(Vector3f position) {
            this.position = position;
            this.normal = new Vector3f(0f);
            this.textCoords = new Vector2f(0f);
        }

        public VertexInfo(Vector3f vertexPos, Vector2f textCoords) {
            this(vertexPos);
            this.textCoords = textCoords;
        }
    }

}
