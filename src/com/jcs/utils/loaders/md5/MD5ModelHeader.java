package com.jcs.utils.loaders.md5;

import java.util.List;

public class MD5ModelHeader {

    public String version;
    public String commandLine;
    public int numJoints;
    public int numMeshes;


    public static MD5ModelHeader parse(List<String> lines) {
        MD5ModelHeader header = new MD5ModelHeader();
        int numLines = lines != null ? lines.size() : 0;
        if (numLines == 0) {
            throw new RuntimeException("Cannot process empty file");
        }

        boolean finishHeader = false;
        for (int i = 0; i < numLines && !finishHeader; i++) {
            String line = lines.get(i);
            String[] tokens = line.split("\\s+");
            int numTokens = tokens != null ? tokens.length : 0;
            if (numTokens > 1) {
                String paramName = tokens[0];
                String paramValue = tokens[1];

                switch (paramName) {
                    case "MD5Version":
                        header.version = paramValue;
                        break;
                    case "commandline":
                        header.commandLine = paramValue;
                        break;
                    case "numJoints":
                        header.numJoints = Integer.parseInt(paramValue);
                        break;
                    case "numMeshes":
                        header.numMeshes = Integer.parseInt(paramValue);
                        break;
                    case "joints":
                        finishHeader = true;
                        break;
                }
            }
        }

        return header;
    }
}
