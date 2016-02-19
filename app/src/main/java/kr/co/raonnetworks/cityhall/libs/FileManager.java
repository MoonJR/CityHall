package kr.co.raonnetworks.cityhall.libs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by MoonJongRak on 2015. 11. 27..
 */
public class FileManager {

    public static boolean copyFile(InputStream copyStream, File paste) {
        File pasteTmp = new File(paste.getParentFile(), paste.getName() + ".tmp");
        if (pasteTmp.exists()) {
            pasteTmp.delete();
        }
        try {
            FileOutputStream output = new FileOutputStream(pasteTmp);

            byte data[] = new byte[1024];
            int count;
            while ((count = copyStream.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.close();
            copyStream.close();

            if (paste.exists()) {
                paste.delete();
            }
            return pasteTmp.renameTo(paste);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
