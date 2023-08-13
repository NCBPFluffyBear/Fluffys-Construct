package io.ncbpfluffybear.fluffysconstruct.utils;

import com.google.common.io.CharStreams;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class FileUtils {

    private static final String NAME_PATTERN = "(.*)\\..*";

    public FileUtils() {
        throw new InstantiationError();
    }

    /**
     * Loads a packaged resource in the jar file
     */
    public static void copyJarResource(String resName, File destFile) {
        try(InputStream res = FCPlugin.class.getResourceAsStream( "/" + resName)) {
            if (res == null) {
                Bukkit.getLogger().log(Level.SEVERE, "No local resource found named " + resName);
                return;
            }
            Files.copy(res, destFile.toPath());
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to copy " + resName, e);
        }
    }

    public static String readJarResource(String resName) {
        try(InputStream res = FCPlugin.class.getResourceAsStream( "/" + resName)) {
            if (res == null) {
                Bukkit.getLogger().log(Level.SEVERE, "No local resource found named " + resName);
                return null;
            }
            Scanner s = new Scanner(res).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to copy " + resName, e);
        }
        return null;
    }

}
