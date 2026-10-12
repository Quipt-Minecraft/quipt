package live.qsmc.quipt.minecraft.utils.chat;

import live.qsmc.quipt.core.data.annotations.Nullable;
import net.kyori.adventure.audience.Audience;

@FunctionalInterface
public interface StringPlaceholder  {

    String convert(@Nullable Audience viewer);

//    @Override
//    default JSONObject json() {
//        return Placeholder.super.json();
//    }
}
