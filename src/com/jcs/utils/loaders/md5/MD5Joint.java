package com.jcs.utils.loaders.md5;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MD5Joint {

    private static final String FLOAT_REGEXP = "[+-]?\\d*\\.?\\d*";
    private static final String VECTOR3_REGEXP = "\\(\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*\\)";
    private static final String PARENT_INDEX_REGEXP = "([-]?\\d+)";
    private static final String NAME_REGEXP = "\\\"([^\\\"]+)\\\"";
    private static final String JOINT_REGEXP = "\\s*" + NAME_REGEXP + "\\s*" + PARENT_INDEX_REGEXP + "\\s*"
            + VECTOR3_REGEXP + "\\s*" + VECTOR3_REGEXP + ".*";
    private static final Pattern PATTERN_JOINT = Pattern.compile(JOINT_REGEXP);

    public String name;
    public int parentIndex;
    public Vector3f position;
    public Quaternionf orientation;

    public static MD5Joint parseLine(String line) {
        MD5Joint result = null;
        Matcher matcher = PATTERN_JOINT.matcher(line);
        if (matcher.matches()) {
            result = new MD5Joint();
            result.name = (matcher.group(1));
            result.parentIndex = (Integer.parseInt(matcher.group(2)));
            float x = Float.parseFloat(matcher.group(3));
            float y = Float.parseFloat(matcher.group(4));
            float z = Float.parseFloat(matcher.group(5));
            result.position = new Vector3f(x, y, z);

            x = Float.parseFloat(matcher.group(6));
            y = Float.parseFloat(matcher.group(7));
            z = Float.parseFloat(matcher.group(8));
            result.orientation = calculateQuaternion(new Quaternionf(x, y, z, 0f));
        }
        return result;
    }

    public static Quaternionf calculateQuaternion(Quaternionf q) {
        float temp = 1.0f - q.lengthSquared();

        if (temp < 0.0f)
            q.w = 0.0f;
        else
            q.w = -(float) (Math.sqrt(temp));

        return q;
    }

    public static List<MD5Joint> parse(List<String> blockBody) {
        List<MD5Joint> joints = new ArrayList<>();
        for (String line : blockBody) {
            MD5Joint jointData = MD5Joint.parseLine(line);
            if (jointData != null) {
                joints.add(jointData);
            }
        }
        return joints;
    }
}
