package io.ncbpfluffybear.fluffysconstruct.api.web;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockData;
import io.ncbpfluffybear.fluffysconstruct.data.SmelterySystem;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class WebUtils {
    // the folder inside the base server folder which contains website files
    private static final Map<String, WebResource> RESOURCES = new HashMap<>();
    private static WebResource ERROR_PAGE = null;

    public static NamespacedKey UUID_URL = new NamespacedKey(FCPlugin.getInstance(), "url_uuid");

    private WebUtils() {
        throw new InstantiationError();
    }

    public static void copyWebpages() {
        try {
            ERROR_PAGE = new WebResource("404.html");
            addResource("index.html");
            addResource("favicon.ico");
            addResource("invdisplay.html");
            addResource("progress.html");
            addResource("styles.css");
        } catch (IOException e) {
            ChatUtils.logError("Failed to add a web resource " + e);
        }

    }

    public static void addResource(String name) throws IOException {
        RESOURCES.put(name, new WebResource(name));
    }

    public static WebResource getResource(String name) {
        return RESOURCES.get(name);
    }

    public static byte[] getResponse(String request) {
        ChatUtils.warn("Requesting " + request);

        RequestType requestType = RequestType.find(request);
        switch (requestType) {
            case PNG -> {
                try (InputStream input = FCPlugin.class.getResourceAsStream("/" + request)) {
                    return input == null ? null : input.readAllBytes();
                } catch (IOException e) {
                    ChatUtils.logError("Failed to load an image resource " + e);
                    return ERROR_PAGE.getHtml();
                }
            }
            case STYLES -> {
                return RESOURCES.get("styles.css").getHtml();
            }
            case SMELTERY -> {
                UUID uuid = UUID.fromString(request.split("smeltery/")[1]);
                ChatUtils.warn("Loading smeltery " + uuid);
                SmelterySystem system = FCPlugin.getSmelteryRepository().getSystem(uuid);
                if (system == null) {
                    return ERROR_PAGE.getHtml();
                }

                byte[] response = new SmelteryPage(system).getPage();
                if (response == null) return ERROR_PAGE.getHtml();
                return response;
            }
            default -> {
                WebResource resource = RESOURCES.getOrDefault(request, ERROR_PAGE);
                return resource.getHtml();
            }
        }
    }

    enum RequestType {

        NORMAL((request) -> false),
        PNG((request) -> request.endsWith("png")),
        STYLES((request) -> request.endsWith("styles.css")),
        SMELTERY((request) -> request.contains("smeltery/"));


        private final Predicate<String> matcher;

        RequestType(Predicate<String> matcher) {
            this.matcher = matcher;
        }

        @Nonnull
        public static RequestType find(String request) {
            for (RequestType type : RequestType.values()) {
                if (type.matcher.test(request)) {
                    return type;
                }
            }

            return NORMAL;
        }

    }
}
