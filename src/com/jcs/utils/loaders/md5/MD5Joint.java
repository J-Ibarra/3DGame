package com.jcs.utils.loaders.md5;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jcs on 26/9/2016.
 */
public class MD5Joint {

    private static final String FLOAT_REGEXP = "[+-]?\\d*\\.?\\d*";
    private static final String VECTOR3_REGEXP = "\\(\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*\\)";
    private static final String PARENT_INDEX_REGEXP = "([-]?\\d+)";
    private static final String NAME_REGEXP = "\\\"([^\\\"]+)\\\"";
    private static final String JOINT_REGEXP = "\\s*" + NAME_REGEXP + "\\s*" + PARENT_INDEX_REGEXP + "\\s*"
            + VECTOR3_REGEXP + "\\s*" + VECTOR3_REGEXP + ".*";
    private static final Pattern PATTERN_JOINT = Pattern.compile(JOINT_REGEXP);

    public final String name;
    public final int parentIndex;
    public final Vector3f position;
    public final Quaternionf orientation;

    public MD5Joint(String name, int parentIndex, Vector3f position, Quaternionf orientation) {
        this.name = name;
        this.parentIndex = parentIndex;
        this.position = position;
        this.orientation = orientation;
    }

    public static MD5Joint processLine(String line) {
        String name = null;
        int parentIndex = -2;
        Vector3f position = null;
        Quaternionf orientation = null;

        Matcher matcher = PATTERN_JOINT.matcher(line);
        if (matcher.matches()) {
            name = (matcher.group(1));
            parentIndex = (Integer.parseInt(matcher.group(2)));
            float x = Float.parseFloat(matcher.group(3));
            float y = Float.parseFloat(matcher.group(4));
            float z = Float.parseFloat(matcher.group(5));
            position = new Vector3f(x, y, z);

            x = Float.parseFloat(matcher.group(6));
            y = Float.parseFloat(matcher.group(7));
            z = Float.parseFloat(matcher.group(8));
            orientation = calculateQuaternion(new Quaternionf(x, y, z, 0f));
        }

        if (name == null || parentIndex == -2 || position == null || orientation == null)
            throw new RuntimeException("");
        return new MD5Joint(name, parentIndex, position, orientation);

    }

    private static Quaternionf calculateQuaternion(Quaternionf q) {
        float temp = 1.0f - q.lengthSquared();

        if (temp < 0.0f)
            q.w = 0.0f;
        else
            q.w = -(float) (Math.sqrt(temp));

        return q;
    }
}
