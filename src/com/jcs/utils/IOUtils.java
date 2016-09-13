package com.jcs.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Thread.currentThread;
import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.newByteChannel;
import static org.lwjgl.BufferUtils.createByteBuffer;

/**
 * Created by Jcs on 9/9/2016.
 */
public class IOUtils {

    /**
     * Reads the specified resource and returns the raw data as a ByteBuffer.
     *
     * @param resource   the resource to read
     * @return the resource data
     * @throws Exception if an error occurs
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource) throws Exception

    {

        ByteBuffer buffer;
        Path path = Paths.get(currentThread().getContextClassLoader().getResource(resource).toURI());
        if (isReadable(path)) {
            SeekableByteChannel fc = newByteChannel(path);
            buffer = createByteBuffer((int) fc.size() + 1);

            while (fc.read(buffer) != -1) ;

            buffer.flip();
            return buffer;
        } else
            throw new Exception("could not load resource: " + resource);

    }
}
