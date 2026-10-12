package live.qsmc.quipt.tests;

import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;

public class MainTests {

    @Test
    public void test(){
//        String test = System.getenv("BRANCH_NAME").contains("snapshot") ? System.getenv("BRANCH_NAME") : "false";

        System.out.println(new JSONObject().put("value", MessageUtils.serialize(text("[0]", NamedTextColor.DARK_GREEN)
            .append(text(" has been uploaded successfully.", NamedTextColor.GREEN))
            .appendNewline()
            .append(text("Click ", NamedTextColor.GREEN))
            .append(text("here", NamedTextColor.YELLOW).clickEvent(ClickEvent.openUrl("[1]")))
            .append(text(" to download.", NamedTextColor.GREEN)))));
    }
}
