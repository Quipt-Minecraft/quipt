package live.qsmc.minecraft2.utils.chat;

import live.qsmc.core2.data.annotations.Nullable;
import net.kyori.adventure.audience.Audience;

@FunctionalInterface
public interface StringPlaceholder extends Placeholder<String> {

    String convert(@Nullable Audience viewer);
}
