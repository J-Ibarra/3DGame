package com.jcs.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipInputStream;

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

    public static ZipInputStream getZipInputStream(String resource) {
        ZipInputStream zipStream;
        zipStream = new ZipInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource));
        return zipStream;
    }

    /*public static ZipEntry[] getZipEntries(String resource) {
        ZipInputStream zipInputStream = getZipInputStream(resource);
        List<ZipEntry> zipEntries = new ArrayList<>();

        try {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                zipEntries.add(zipEntry);
                //System.out.println(zipEntry.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read the Zip: " + resource);
        }

        ZipEntry[] entries = new ZipEntry[zipEntries.size()];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = zipEntries.get(i);
        }

        return entries;
    }*/

    public static int getZipEntries(ZipInputStream zipInputStream) {
        int nEntries = 0;

        try {
            while (zipInputStream.getNextEntry() != null) {
                nEntries++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read the zipInputStream ");
        }

        return nEntries;
    }

    public static BufferedReader[] getStringOfZipEntries(String resource) {
        ZipInputStream zipInputStream = getZipInputStream(resource);
        int zipEntries = getZipEntries(zipInputStream);
        BufferedReader[] bufferedReaders = new BufferedReader[zipEntries];
        zipInputStream = getZipInputStream(resource);

        try {
            int i = 0;
            while (zipInputStream.getNextEntry() != null) {
                InputStream inputStream = zipInputStream;
                Reader reader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(reader);
                bufferedReaders[i] = new BufferedReader(reader);

                String line;
                int j = 0;
                while ((line = br.readLine()) != null) {
                    if (j == 2)
                        System.out.println(line);
                    j++;
                }


                i++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            for (BufferedReader bufferedReader : bufferedReaders) {
                String line;
                int j = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    if (j == 2)
                        System.out.println(line);
                    j++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return bufferedReaders;
    }

}
