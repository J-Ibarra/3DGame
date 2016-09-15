package com.jcs.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
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
     * Reads the specified resource and returns the raw data as a {@link ByteBuffer}.
     *
     * @param resource the resource to read
     * @return the resource data in {@link ByteBuffer}
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource) {
        ByteBuffer buffer;
        try {
            Path path = Paths.get(currentThread().getContextClassLoader().getResource(resource).toURI());
            if (isReadable(path)) {
                SeekableByteChannel fc = newByteChannel(path);
                buffer = createByteBuffer((int) fc.size() + 1);

                while (fc.read(buffer) != -1) ;

                fc.close();
                buffer.flip();
                return buffer;
            } else
                throw new Exception("could not load resource: " + resource);
        } catch (Exception e) {
            throw new RuntimeException("could not load resource: " + resource);
        }
    }

    /**
     * Reads the specified resource and returns the raw data as a byte array.
     *
     * @param resource the resource to read
     * @return the resource data in int[]
     */
    public static byte[] ioResourceToByteArray(String resource) {
        ByteBuffer data = ioResourceToByteBuffer(resource);
        byte[] arr = new byte[data.remaining()];
        data.get(arr);
        return arr;
    }

    /**
     * Reads the specified resource and returns the raw data as a {@link BufferedReader}.
     *
     * @param resource the resource to read
     * @return the resource data in {@link BufferedReader}
     */
    public static BufferedReader ioResourceToBufferedReader(String resource) {
        byte[] arr = ioResourceToByteArray(resource);
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(arr)));
    }
}
