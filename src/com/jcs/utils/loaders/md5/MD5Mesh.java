package com.jcs.utils.loaders.md5;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jcs on 26/9/2016.
 */
public class MD5Mesh {

    private static final String FLOAT_REGEXP = "[+-]?\\d*\\.?\\d*";
    private static final String VECTOR3_REGEXP = "\\(\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*\\)";
    private static final Pattern PATTERN_SHADER = Pattern.compile("\\s*shader\\s*\\\"([^\\\"]+)\\\"");
    private static final Pattern PATTERN_VERTEX = Pattern.compile("\\s*vert\\s*(\\d+)\\s*\\(\\s*("
            + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*\\)\\s*(\\d+)\\s*(\\d+)");
    private static final Pattern PATTERN_TRI = Pattern.compile("\\s*tri\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)");
    private static final Pattern PATTERN_WEIGHT = Pattern.compile("\\s*weight\\s*(\\d+)\\s*(\\d+)\\s*" +
            "(" + FLOAT_REGEXP + ")\\s*" + VECTOR3_REGEXP);

    public String texture = null;
    public List<MD5Vertex> vertices;
    public List<MD5Triangle> triangles;
    public List<MD5Weight> weights;

    public MD5Mesh() {
        this.vertices = new ArrayList<>();
        this.triangles = new ArrayList<>();
        this.weights = new ArrayList<>();
    }

    public void processLine(String line) {
        if (line.contains("shader")) {
            Matcher matcher = PATTERN_SHADER.matcher(line);
            if (matcher.matches())
                this.texture = matcher.group(1);
        } else if (line.contains("vert")) {
            Matcher matcher = PATTERN_VERTEX.matcher(line);
            if (matcher.matches()) {
                int index = Integer.parseInt(matcher.group(1));
                float x = Float.parseFloat(matcher.group(2));
                float y = Float.parseFloat(matcher.group(3));
                Vector2f textCoords = new Vector2f(x, y);
                int startWeight = (Integer.parseInt(matcher.group(4)));
                int weightCount = (Integer.parseInt(matcher.group(5)));
                vertices.add(new MD5Vertex(index, textCoords, startWeight, weightCount));
            }
        } else if (line.contains("tri")) {
            Matcher matcher = PATTERN_TRI.matcher(line);
            if (matcher.matches()) {
                int index = Integer.parseInt(matcher.group(1));
                int vertex0 = Integer.parseInt(matcher.group(2));
                int vertex1 = Integer.parseInt(matcher.group(3));
                int vertex2 = Integer.parseInt(matcher.group(4));
                triangles.add(new MD5Triangle(index, vertex0, vertex1, vertex2));
            }
        } else if (line.contains("weight")) {
            Matcher matcher = PATTERN_WEIGHT.matcher(line);
            if (matcher.matches()) {
                int index = (Integer.parseInt(matcher.group(1)));
                int jointIndex = (Integer.parseInt(matcher.group(2)));
                float bias = (Float.parseFloat(matcher.group(3)));
                float x = Float.parseFloat(matcher.group(4));
                float y = Float.parseFloat(matcher.group(5));
                float z = Float.parseFloat(matcher.group(6));
                Vector3f position = new Vector3f(x, y, z);
                weights.add(new MD5Weight(index,jointIndex,bias,position));
            }
        }
    }

    public static class MD5Vertex {
        public final int index;
        public final Vector2f textCoords;
        public final int startWeight;
        public final int weightCount;

        public MD5Vertex(int index, Vector2f textCoords, int startWeight, int weightCount) {
            this.index = index;
            this.textCoords = textCoords;
            this.startWeight = startWeight;
            this.weightCount = weightCount;
        }
    }

    public static class MD5Triangle {
        public final int index;
        public final int vertex0;
        public final int vertex1;
        public final int vertex2;

        public MD5Triangle(int index, int vertex0, int vertex1, int vertex2) {
            this.index = index;
            this.vertex0 = vertex0;
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
        }
    }

    public static class MD5Weight {
        public final int index;
        public final int jointIndex;
        public final float bias;
        public final Vector3f position;

        public MD5Weight(int index, int jointIndex, float bias, Vector3f position) {
            this.index = index;
            this.jointIndex = jointIndex;
            this.bias = bias;
            this.position = position;
        }
    }
}
