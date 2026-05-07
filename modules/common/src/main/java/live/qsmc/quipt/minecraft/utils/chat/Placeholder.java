package live.qsmc.quipt.minecraft.utils.chat;

import live.qsmc.quipt.core.data.annotations.Nullable;
import net.kyori.adventure.audience.Audience;

public interface Placeholder<T> {

    T convert(@Nullable Audience viewer);
}
