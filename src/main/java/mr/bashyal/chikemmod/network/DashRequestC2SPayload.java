package mr.bashyal.chikemmod.network;

import mr.bashyal.chikemmod.ChikemMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DashRequestC2SPayload() implements CustomPayload {
    public static final Id<DashRequestC2SPayload> ID = new Id<>(Identifier.of(ChikemMod.MOD_ID, "dash_request"));
    public static final DashRequestC2SPayload INSTANCE = new DashRequestC2SPayload();
    public static final PacketCodec<RegistryByteBuf, DashRequestC2SPayload> CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

