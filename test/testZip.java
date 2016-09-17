import java.io.*;
import java.util.zip.ZipInputStream;

/**
 * Created by Jcs on 13/9/2016.
 */
public class testZip {
    public static void main(String[] args) throws Exception{
        String arg = "test/cube.zip";
        ZipInputStream zipStream = new ZipInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(arg));

        System.out.println(zipStream.getNextEntry());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = zipStream.read(buffer)) > 0) {
            baos.write(buffer, 0, read);
        }
        zipStream.close();
        byte[] arr = baos.toByteArray();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(arr)));

        StringBuilder result = new StringBuilder();

        try {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line + '\n');
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result.toString());

    }
}
