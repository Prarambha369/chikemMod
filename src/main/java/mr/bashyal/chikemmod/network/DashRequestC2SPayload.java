package mr.bashyal.chikemmod.network;

import mr.bashyal.chikemmod.ChikemMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record DashRequestC2SPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DashRequestC2SPayload> ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(ChikemMod.MOD_ID, "dash_request"));
    public static final DashRequestC2SPayload INSTANCE = new DashRequestC2SPayload();
    public static final StreamCodec<RegistryFriendlyByteBuf, DashRequestC2SPayload> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
