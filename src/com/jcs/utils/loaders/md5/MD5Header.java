package com.jcs.utils.loaders.md5;

import java.util.List;

/**
 * Created by Jcs on 26/9/2016.
 */
public class MD5Header {

    public final String version;
    public final String commandLine;
    public final int numJoints;
    public final int numMeshes;

    public MD5Header(String version, String commandLine, int numJoints, int numMeshes) {
        this.version = version;
        this.commandLine = commandLine;
        this.numJoints = numJoints;
        this.numMeshes = numMeshes;
    }

    public static MD5Header process(List<String> strings) {
        String version = null;
        String commandLine = null;
        int numJoints = -1;
        int numMeshes = -1;

        int numLines = strings != null ? strings.size() : -1;
        if (numLines == 0) {
            throw new RuntimeException("can not processModel empty file");
        }

        for (int i = 0; i < strings.size(); i++) {
            String line = strings.get(i).trim();
            String[] tokens = line.split("\\s+");
            int numTokens = tokens != null ? tokens.length : -1;
            if (numTokens > 1) {
                String paramName = tokens[0];
                String paramValue = tokens[1];
                switch (paramName) {
                    case "MD5Version":
                        version = paramValue;
                        break;
                    case "commandline":
                        commandLine = line.substring(line.indexOf(" ") + 1);
                        break;
                    case "numJoints":
                        numJoints = Integer.parseInt(paramValue);
                        break;
                    case "numMeshes":
                        numMeshes = Integer.parseInt(paramValue);
                        break;
                }
            }
        }

        if (version == null || commandLine == null || numJoints == -1 || numMeshes == -1)
            throw new RuntimeException("could not processModel the header");
        return new MD5Header(version, commandLine, numJoints, numMeshes);
    }
}
