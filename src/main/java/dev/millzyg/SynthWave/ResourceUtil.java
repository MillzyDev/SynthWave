package dev.millzyg.SynthWave;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ResourceUtil {
    static public void ExportResource(String resourceJARPath, String newResourcePath) throws IOException {
        URL inputUrl = ResourceUtil.class.getResource(resourceJARPath);
        File dest = new File(newResourcePath);
        assert inputUrl != null;
        FileUtils.copyURLToFile(inputUrl, dest);
    }
}
