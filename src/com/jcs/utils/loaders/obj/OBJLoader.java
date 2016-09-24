package com.jcs.utils.loaders.obj;

import com.jcs.utils.IOUtils;
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

    public static class OBJModel {
        //<editor-fold defaultstate="collapsed" desc="Body of OBJModel class">
        public final List<Vector3f> v; //data of vertices
        public final List<Vector2f> t; //data of textures
        public final List<Vector3f> n; //data of normals
        public final List<OBJFace> f;     //data of faces

        public OBJModel(List<Vector3f> v, List<Vector2f> t, List<Vector3f> n, List<OBJFace> f) {
            this.v = v;
            this.t = t;
            this.n = n;
            this.f = f;
        }
        //</editor-fold>
    }

    public static class OBJFace {
        //<editor-fold defaultstate="collapsed" desc="Body of OBJFace class">
        public final Vector3i v; //indices of Vertices
        public final Vector3i t; //indices of Textures
        public final Vector3i n; //indices of Normals

        public OBJFace(Vector3i v, Vector3i t, Vector3i n) {
            this.v = v;
            this.t = t;
            this.n = n;
        }
        //</editor-fold>
    }

    private static OBJModel loadModel(BufferedReader bufferedReader) {
        List<Vector3f> v = new ArrayList<>(); //data of vertices
        List<Vector2f> t = new ArrayList<>(); //data of textures
        List<Vector3f> n = new ArrayList<>(); //data of normals
        List<OBJFace> f = new ArrayList<>();     //data of faces

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] vs = line.split(" ");
                    float x = Float.parseFloat(vs[1]);
                    float y = Float.parseFloat(vs[2]);
                    float z = Float.parseFloat(vs[3]);
                    v.add(new Vector3f(x, y, z));
                } else if (line.startsWith("vt ")) {
                    String[] ts = line.split(" ");
                    float x = Float.parseFloat(ts[1]);
                    float y = 1f - Float.parseFloat(ts[2]);
                    t.add(new Vector2f(x, y));
                } else if (line.startsWith("vn ")) {
                    String[] ns = line.split(" ");
                    float x = Float.parseFloat(ns[1]);
                    float y = Float.parseFloat(ns[2]);
                    float z = Float.parseFloat(ns[3]);
                    n.add(new Vector3f(x, y, z));
                } else if (line.startsWith("f ")) {
                    String[] fs = line.split(" ");
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

                    f.add(new OBJFace(vi, ti, ni));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read bufferedReader");
        }

        return new OBJModel(v, t, n, f);
    }

    public static OBJModel loadOBJ(String resource) {
        //<editor-fold defaultstate="collapsed" desc="loadOBJ from resource">
        BufferedReader reader = ioResourceToBufferedReader(resource);
        return loadModel(reader);
        //</editor-fold>
    }

    public static OBJModel[] loadAnimationOBJ(String resource) {
        BufferedReader[] bufferedReaders = IOUtils.getBufferedReaderOfZipEntries(resource);
        OBJModel[] OBJModels = new OBJModel[bufferedReaders.length];

        for (int i = 0; i < OBJModels.length; i++) {
            OBJModels[i] = loadModel(bufferedReaders[i]);
        }

        return OBJModels;
    }
}
