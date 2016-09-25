package com.jcs.utils.loaders.md5;

import java.util.List;

/**
 * Created by Jcs on 25/9/2016.
 */
public class MD5HeaderData {

    public final String version;
    public final String commandLine;
    public final int numJoints;
    public final int numMeshes;

    public MD5HeaderData(String version, String commandLine, int numJoints, int numMeshes) {
        this.version = version;
        this.commandLine = commandLine;
        this.numJoints = numJoints;
        this.numMeshes = numMeshes;
    }

    public static MD5HeaderData process(List<String> header) {
        String version = null;
        String commandLine = null;
        int numJoints = -1;
        int numMeshes = -1;

        for (int i = 0; i < header.size(); i++) {
            String line = header.get(i);
            String[] tokens = line.split("\\s+");
            int numTokens = tokens != null ? tokens.length : 0;
            if (numTokens > 1) {
                String paramName = tokens[0];
                String paramValue = tokens[1];
                if (paramName.equals("MD5Version"))
                    version = paramValue;
                else if (paramName.equals("commandline"))
                    commandLine = line.substring(line.indexOf(" ") + 1);
                else if (paramName.equals("numJoints"))
                    numJoints = Integer.parseInt(paramValue);
                else if (paramName.equals("numMeshes"))
                    numMeshes = Integer.parseInt(paramValue);
            }
        }

        if (version == null || commandLine == null || numJoints == -1 || numMeshes == -1)
            throw new RuntimeException();
        return new MD5HeaderData(version, commandLine, numJoints, numMeshes);
    }
}
