package com.jcs.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import static com.jcs.utils.IOUtils.ioResourceToBufferedReader;

/**
 * Created by Jcs on 14/9/2016.
 */
public class OBJLoader {

    public static class Model {
        public List<Vector3f> v = new ArrayList<>();
        public List<Vector2f> t = new ArrayList<>();
        public List<Vector3f> n = new ArrayList<>();
        public List<Face> f = new ArrayList<>();
    }

    public static class Face {
        public final Vector3i v; //indices of Vertices
        public final Vector3i t; //indices of Textures
        public final Vector3i n; //indices of Normals

        public Face(Vector3i v, Vector3i t, Vector3i n) {
            this.v = v;
            this.t = t;
            this.n = n;
        }
    }

    public static Model loadOBJ(String resource) {
        BufferedReader reader = ioResourceToBufferedReader(resource);
        Model m = new Model();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] vs = line.split(" +");
                    float x = Float.parseFloat(vs[1]);
                    float y = Float.parseFloat(vs[2]);
                    float z = Float.parseFloat(vs[3]);
                    m.v.add(new Vector3f(x, y, z));
                } else if (line.startsWith("vt ")) {
                    String[] ts = line.split(" +");
                    float x = Float.parseFloat(ts[1]);
                    float y = 1f-Float.parseFloat(ts[2]);
                    m.t.add(new Vector2f(x, y));
                } else if (line.startsWith("vn ")) {
                    String[] ns = line.split(" +");
                    float x = Float.parseFloat(ns[1]);
                    float y = Float.parseFloat(ns[2]);
                    float z = Float.parseFloat(ns[3]);
                    m.n.add(new Vector3f(x, y, z));
                } else if (line.startsWith("f ")) {
                    String[] fs = line.split(" +");
                    String[] f1 = fs[1].split("/");
                    String[] f2 = fs[2].split("/");
                    String[] f3 = fs[3].split("/");

                    int v1 = Integer.parseInt(f1[0]) - 1;
                    int v2 = Integer.parseInt(f2[0]) - 1;
                    int v3 = Integer.parseInt(f3[0]) - 1;
                    Vector3i vi = new Vector3i(v1, v2, v3);

                    int t1 = Integer.parseInt(f1[1]) - 1;
                    int t2 = Integer.parseInt(f2[1]) - 1;
                    int t3 = Integer.parseInt(f3[1]) - 1;
                    Vector3i ti = new Vector3i(t1, t2, t3);

                    int n1 = Integer.parseInt(f1[2]) - 1;
                    int n2 = Integer.parseInt(f2[2]) - 1;
                    int n3 = Integer.parseInt(f3[2]) - 1;
                    Vector3i ni = new Vector3i(n1, n2, n3);

                    m.f.add(new Face(vi, ti, ni));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read file: " + resource);
        }

        return m;
    }
}
