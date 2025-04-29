package net.dawson.adorablehamsterpets.attachment;

import net.dawson.adorablehamsterpets.AdorableHamsterPets;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;

public class ModEntityAttachments {

    // This PacketCodec automatically uses the updated HamsterShoulderData.CODEC
    public static final PacketCodec<RegistryByteBuf, HamsterShoulderData> HAMSTER_DATA_PACKET_CODEC =
            PacketCodecs.registryCodec(HamsterShoulderData.CODEC);

    public static final AttachmentType<HamsterShoulderData> HAMSTER_SHOULDER_DATA =
            AttachmentRegistry.<HamsterShoulderData>builder()
                    .persistent(HamsterShoulderData.CODEC) // Uses updated CODEC
                    .syncWith(
                            HAMSTER_DATA_PACKET_CODEC,    // Uses updated PacketCodec implicitly
                            AttachmentSyncPredicate.all()
                    )
                    .buildAndRegister(Identifier.of(AdorableHamsterPets.MOD_ID, "hamster_shoulder_data"));

    public static void registerAttachments() {
        AdorableHamsterPets.LOGGER.info("Registering Mod Entity Attachments for " + AdorableHamsterPets.MOD_ID);
    }
}