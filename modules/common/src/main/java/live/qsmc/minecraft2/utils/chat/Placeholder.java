package live.qsmc.minecraft2.utils.chat;

import live.qsmc.core2.data.JsonSerializable;
import live.qsmc.core2.data.annotations.Nullable;
import net.kyori.adventure.audience.Audience;

public interface Placeholder<T> {

    T convert(@Nullable Audience viewer);
}
