package live.qsmc.fabric2.net;

import live.qsmc.fabric2.QuiptFabric;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.util.Arrays;
import java.util.Objects;

public class PluginMessagePacket implements CustomPayload {
    private final byte[] data;

    public PluginMessagePacket(byte[] data) {
        this.data = data;
    }

    public PluginMessagePacket(PacketByteBuf buf) {
        this(getWrittenBytes(buf));
    }

    public static CustomPayload.Id<PluginMessagePacket> CHANNEL_ID = new CustomPayload.Id<>(
        QuiptFabric.BUNGEE_CHANNEL
    );

    public static PacketCodec<RegistryByteBuf, PluginMessagePacket> CODEC = PacketCodec.of(
        (value, buf) -> writeBytes(buf, value.data),
        PluginMessagePacket::new
    );

    private static byte[] getWrittenBytes(PacketByteBuf buf) {
        byte[] bs = new byte[buf.readableBytes()];
        buf.readBytes(bs);
        return bs;
    }

    private static void writeBytes(PacketByteBuf buf, byte[] v) {
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
    public Id<? extends CustomPayload> getId() {
        return CHANNEL_ID;
    }
}