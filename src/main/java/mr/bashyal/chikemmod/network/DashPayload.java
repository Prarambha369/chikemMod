package mr.bashyal.chikemmod.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class DashPayload implements CustomPayload {
    public static final Identifier IDENTIFIER = Identifier.of("chikemmod", "dash");
    public static final CustomPayload.Id<DashPayload> ID = new CustomPayload.Id<>(IDENTIFIER);
    public static final DashPayload INSTANCE = new DashPayload();

    // Required for payload registration
    public DashPayload() {
        // No data
    }

    public DashPayload(PacketByteBuf buf) {
        // No data to read for this payload
    }

    @Override
    public CustomPayload.Id<DashPayload> getId() {
        return ID;
    }
}
