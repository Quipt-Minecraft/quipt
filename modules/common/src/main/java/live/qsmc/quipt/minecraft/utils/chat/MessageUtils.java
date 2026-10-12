package live.qsmc.quipt.minecraft.utils.chat;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.config.files.MessagesConfig;
import live.qsmc.quipt.core.data.annotations.Nullable;
import live.qsmc.quipt.core.data.registries.Registry;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;


public class MessageUtils {

    private static MessagesConfig config;
    static Registry<StringPlaceholder> placeholders;


    public static void init() {
        Quipt.INSTANCE.logger().log("Messages", "Initializing Messages...");
        config = Quipt.INSTANCE.configs().register(MessagesConfig.class);
        placeholders = Quipt.INSTANCE.registries().register("placeholders", () -> null);
        createDefaultMessages();
        config.save();

    }

    public static Registry<StringPlaceholder> placeholders() {
        return placeholders;
    }

    public static void register(String key, String serializedComponent) {
        if (!config.messages.has(key)) {
            config.messages.put(key, serializedComponent);
        }
    }

    public static void register(String key, Component deserializedComponent) {
        register(key, serialize(deserializedComponent));
    }

    private static void createDefaultMessages() {
        register("cmd.error.no_perm", "{\"text\":\"Sorry, you don't have the permission to run that command.\",\"color\":\"red\"}");
        register("cmd.error.no_command", "{\"text\":\"Sorry, couldn't find the command \\\"[0]\\\". Please check your spelling and try again.\",\"color\":\"red\"}");
        register("cmd.error.no_console", "{\"text\":\"Sorry, this command can only be run by players.\",\"color\":\"red\"}");
        register("cmd.session.start", "{\"text\":\"Session started\",\"color\":\"green\"}");
        register("cmd.session.end", "{\"text\":\"Session ended\",\"color\":\"green\"}");
        register("cmd.session.reward", "{\"text\":\"You have been rewarded with [0]\",\"color\":\"green\"}");
        register("cmd.session.task", "{\"text\":\"You have been assigned the task [0]\",\"color\":\"green\"}");
    }

    public static Component deserialize(JSONObject json) {
        return deserialize(json.toString());
    }

    public static Component deserialize(String json) {
        return GsonComponentSerializer.gson().deserialize(json);
    }

    public static String plainText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String serialize(Component component) {
        String a = GsonComponentSerializer.gson().serialize(component);
        if (!a.startsWith("{")) a = "{\"text\":" + a + "}";
        return a;
    }

    public static Component get(String key, Object... replacements) {
        String raw = get(key);
        for (int index = 0; index != replacements.length; index++) {
            String rawReplacement;
            if (replacements[index] instanceof Component) rawReplacement = serialize((Component) replacements[index]);
            else rawReplacement = replacements[index].toString();
            raw = raw.replace("[" + index + "]", rawReplacement);
        }

        return deserialize(raw);
    }


    private static String get(String key) {
        if (!config.messages.has(key)) {
            //attempt to load from registry
            save();
        }
        return config.messages.has(key) ? config.messages.getString(key) : serialize(translatable(key));
    }

    public static Component parse(@NotNull String serializedComponent, @Nullable Audience viewer) {
        while (serializedComponent.contains("${") && serializedComponent.contains("}")) {
            int start = serializedComponent.indexOf("${");
            int end = serializedComponent.indexOf("}", start);
            String key = serializedComponent.substring(start + 2, end);
            if (placeholders().get(key).isPresent())
                serializedComponent = serializedComponent.replace("${" + key + "}", placeholders().get(key).get().convert(viewer));
        }
        return deserialize(serializedComponent);
    }

    public static void save() {
        config.save();
    }
}
