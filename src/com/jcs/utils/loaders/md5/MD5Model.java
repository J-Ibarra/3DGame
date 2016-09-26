package com.jcs.utils.loaders.md5;

import com.jcs.utils.IOUtils;

import java.util.ArrayList;
import java.util.List;

public class MD5Model {

    public MD5ModelHeader header;
    public List<MD5Joint> joints;
    public List<MD5Mesh> meshes = new ArrayList<>();

    public static MD5Model parse(String meshModelFile) {
        List<String> lines = IOUtils.ioResourceToListString(meshModelFile);

        MD5Model result = new MD5Model();

        int numLines = lines != null ? lines.size() : 0;
        if (numLines == 0) {
            throw new RuntimeException("Cannot process empty file");
        }

        // Parse Header
        boolean headerEnd = false;
        int start = 0;
        for (int i = 0; i < numLines && !headerEnd; i++) {
            String line = lines.get(i).trim();
            headerEnd = line.endsWith("{");
            start = i;
        }
        if (!headerEnd) {
            throw new RuntimeException("Cannot find header");
        }
        List<String> headerBlock = lines.subList(0, start);
        MD5ModelHeader header = MD5ModelHeader.parse(headerBlock);
        result.header = header;

        // Parse the rest of block
        int blockStart = 0;
        boolean inBlock = false;
        String blockId = "";
        for (int i = start; i < numLines; i++) {
            String line = lines.get(i);
            if (line.endsWith("{")) {
                blockStart = i;
                blockId = line.substring(0, line.lastIndexOf(" "));
                inBlock = true;
            } else if (inBlock && line.endsWith("}")) {
                List<String> blockBody = lines.subList(blockStart + 1, i);
                parseBlock(result, blockId, blockBody);
                inBlock = false;
            }
        }

        return result;
    }

    private static void parseBlock(MD5Model model, String blockId, List<String> blockBody) {
        switch (blockId) {
            case "joints":
                model.joints = MD5Joint.parse(blockBody);
                break;
            case "mesh":
                MD5Mesh md5Mesh = MD5Mesh.process(blockBody);
                model.meshes.add(md5Mesh);
                break;
            default:
                break;
        }
    }

}
