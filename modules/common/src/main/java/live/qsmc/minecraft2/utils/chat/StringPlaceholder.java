package live.qsmc.minecraft2.utils.chat;

import live.qsmc.core2.data.annotations.Nullable;
import net.kyori.adventure.audience.Audience;
import org.json.JSONObject;

@FunctionalInterface
public interface StringPlaceholder  {

    String convert(@Nullable Audience viewer);

//    @Override
//    default JSONObject json() {
//        return Placeholder.super.json();
//    }
}
