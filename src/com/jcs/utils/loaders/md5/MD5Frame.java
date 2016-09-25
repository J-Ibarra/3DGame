package com.jcs.utils.loaders.md5;

import java.util.ArrayList;
import java.util.List;

public class MD5Frame {

    private int id;

    private Float[] frameData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float[] getFrameData() {
        return frameData;
    }

    public void setFrameData(Float[] frameData) {
        this.frameData = frameData;
    }

    public static MD5Frame parse(String blockId, List<String> blockBody) throws Exception {
        MD5Frame result = new MD5Frame();
        String[] tokens = blockId.trim().split("\\s+");
        if (tokens != null && tokens.length >= 2) {
            result.setId(Integer.parseInt(tokens[1]));
        } else {
            throw new Exception("Wrong frame definition: " + blockId);
        }

        List<Float> data = new ArrayList<>();
        for (String line : blockBody) {
            List<Float> lineData = parseLine(line);
            if (lineData != null) {
                data.addAll(lineData);
            }
        }
        Float[] dataArr = new Float[data.size()];
        data.toArray(dataArr);
        result.setFrameData(dataArr);

        return result;
    }

    private static List<Float> parseLine(String line) {
        String[] tokens = line.trim().split("\\s+");
        List<Float> data = new ArrayList<>();
        for (String token : tokens) {
            data.add(Float.parseFloat(token));
        }
        return data;
    }
}
