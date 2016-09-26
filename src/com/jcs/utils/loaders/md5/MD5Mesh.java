package com.jcs.utils.loaders.md5;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MD5Mesh {

    private static final String FLOAT_REGEXP = "[+-]?\\d*\\.?\\d*";
    private static final String VECTOR3_REGEXP = "\\(\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*\\)";
    private static final Pattern PATTERN_SHADER = Pattern.compile("\\s*shader\\s*\\\"([^\\\"]+)\\\"");
    private static final Pattern PATTERN_VERTEX = Pattern.compile("\\s*vert\\s*(\\d+)\\s*\\(\\s*("
            + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*\\)\\s*(\\d+)\\s*(\\d+)");
    private static final Pattern PATTERN_TRI = Pattern.compile("\\s*tri\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)");
    private static final Pattern PATTERN_WEIGHT = Pattern.compile("\\s*weight\\s*(\\d+)\\s*(\\d+)\\s*" +
            "(" + FLOAT_REGEXP + ")\\s*" + VECTOR3_REGEXP);

    public String texture;
    public List<MD5Vertex> vertices;
    public List<MD5Triangle> triangles;
    public List<MD5Weight> weights;

    public MD5Mesh() {
        this.vertices = new ArrayList<>();
        this.triangles = new ArrayList<>();
        this.weights = new ArrayList<>();
    }

    public static MD5Mesh process(List<String> meshBlock) {
        MD5Mesh mesh = new MD5Mesh();
        List<MD5Vertex> vertices = mesh.vertices;
        List<MD5Triangle> triangles = mesh.triangles;
        List<MD5Weight> weights = mesh.weights;

        for (String line : meshBlock) {
            if (line.contains("shader")) {
                Matcher textureMatcher = PATTERN_SHADER.matcher(line);
                if (textureMatcher.matches()) {
                    mesh.texture = textureMatcher.group(1);

                }
            } else if (line.contains("vert")) {
                Matcher vertexMatcher = PATTERN_VERTEX.matcher(line);
                if (vertexMatcher.matches()) {
                    MD5Vertex vertex = new MD5Vertex();
                    vertex.index = (Integer.parseInt(vertexMatcher.group(1)));
                    float x = Float.parseFloat(vertexMatcher.group(2));
                    float y = Float.parseFloat(vertexMatcher.group(3));
                    vertex.textCoords = (new Vector2f(x, y));
                    vertex.startWeight = (Integer.parseInt(vertexMatcher.group(4)));
                    vertex.weightCount = (Integer.parseInt(vertexMatcher.group(5)));
                    vertices.add(vertex);
                }
            } else if (line.contains("tri")) {
                Matcher triMatcher = PATTERN_TRI.matcher(line);
                if (triMatcher.matches()) {
                    MD5Triangle triangle = new MD5Triangle();
                    triangle.index = (Integer.parseInt(triMatcher.group(1)));
                    triangle.vertex0 = (Integer.parseInt(triMatcher.group(2)));
                    triangle.vertex1 = (Integer.parseInt(triMatcher.group(3)));
                    triangle.vertex2 = (Integer.parseInt(triMatcher.group(4)));
                    triangles.add(triangle);
                }
            } else if (line.contains("weight")) {
                Matcher weightMatcher = PATTERN_WEIGHT.matcher(line);
                if (weightMatcher.matches()) {
                    MD5Weight weight = new MD5Weight();
                    weight.index = (Integer.parseInt(weightMatcher.group(1)));
                    weight.jointIndex = (Integer.parseInt(weightMatcher.group(2)));
                    weight.bias = (Float.parseFloat(weightMatcher.group(3)));
                    float x = Float.parseFloat(weightMatcher.group(4));
                    float y = Float.parseFloat(weightMatcher.group(5));
                    float z = Float.parseFloat(weightMatcher.group(6));
                    weight.position = (new Vector3f(x, y, z));
                    weights.add(weight);
                }
            }
        }
        return mesh;
    }

    public static class MD5Vertex {
        public int index;
        public Vector2f textCoords;
        public int startWeight;
        public int weightCount;
    }

    public static class MD5Triangle {
        public int index;
        public int vertex0;
        public int vertex1;
        public int vertex2;
    }

    public static class MD5Weight {
        public int index;
        public int jointIndex;
        public float bias;
        public Vector3f position;
    }
}
