package io.ncbpfluffybear.fluffysconstruct.api.web;

import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.FileUtils;
import org.bukkit.Location;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;

public class WebResource {

    private final String html;

    public WebResource(String fileName) throws IOException {
        this.html = FileUtils.readJarResource("website/" + fileName);
    }

    public byte[] getHtml() {
        return this.html.getBytes();
    }

    public byte[] getHtml(Map<String, Object> placeholders) {
        String result = this.html;
        ChatUtils.warn(result);
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            ChatUtils.warn(entry.getKey());
            if (entry.getValue() instanceof Location location) {
                result = replace(result, entry.getKey(), location.getX() + ", " + location.getY() + ", " + location.getZ());
            } else {
                result = replace(result, entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        ChatUtils.warn(result);
        return result.getBytes();
    }

    private String replace(String source, String key, String value) {
        return source.replaceAll(Matcher.quoteReplacement(key), Matcher.quoteReplacement(value));
    }

}
