package mr.bashyal.chikemmod.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.RegistryByteBuf;

public class DashPayloadCodec implements PacketCodec<RegistryByteBuf, DashPayload> {

    @Override
    public DashPayload decode(RegistryByteBuf buf) {
        return DashPayload.INSTANCE; // No data to decode
    }

    @Override
    public void encode(RegistryByteBuf buf, DashPayload payload) {
        // No data to encode
    }

    public static final PacketCodec<RegistryByteBuf, DashPayload> INSTANCE = new DashPayloadCodec();
}
