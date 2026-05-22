package live.qsmc.quipt.fabric.net;

import live.qsmc.quipt.fabric.QuiptFabric;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Arrays;
import java.util.Objects;

public class PluginMessagePacket implements CustomPacketPayload {
    private final byte[] data;

    public PluginMessagePacket(byte[] data) {
        this.data = data;
    }

    public PluginMessagePacket(FriendlyByteBuf buf) {
        this(getWrittenBytes(buf));
    }

    public static CustomPacketPayload.Type<PluginMessagePacket> CHANNEL_ID = new CustomPacketPayload.Type<>(
        QuiptFabric.BUNGEE_CHANNEL
    );

//    public static StreamCodec<RegistryFriendlyByteBuf, PluginMessagePacket> CODEC = StreamCodec.of(
//        (value, buf) -> writeBytes(buf, value.array()),
//        PluginMessagePacket::new
//    );

    private static byte[] getWrittenBytes(FriendlyByteBuf buf) {
        byte[] bs = new byte[buf.readableBytes()];
        buf.readBytes(bs);
        return bs;
    }

    private static void writeBytes(FriendlyByteBuf buf, byte[] v) {
        buf.writeBytes(v);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginMessagePacket that = (PluginMessagePacket) o;
        return Objects.deepEquals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return CHANNEL_ID;
    }
}