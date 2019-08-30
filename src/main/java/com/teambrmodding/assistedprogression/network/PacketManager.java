package com.teambrmodding.assistedprogression.network;

import com.teambr.nucleus.network.packet.INetworkMessage;
import com.teambrmodding.assistedprogression.lib.Reference;
import com.teambrmodding.assistedprogression.network.packet.ReadGrinderRecipePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 8/28/2019
 */
public class PacketManager {
    // Our network wrapper
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Reference.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    /**
     * Registers all packets
     */
    public static void initPackets() {
        registerMessage(ReadGrinderRecipePacket.class, ReadGrinderRecipePacket::process);
    }

    // Local hold for next packet id
    private static int nextPacketId = 0;

    /**
     * Registers a message to the network registry
     *
     * @param packet The packet class
     */
    @SuppressWarnings("unchecked")
    private static <T extends INetworkMessage> void registerMessage(Class<T> packet,
                                                                    BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        INSTANCE.registerMessage(nextPacketId, packet,
                INetworkMessage::encode,
                (buf) -> {
                    try {
                        T msg = packet.newInstance();
                        msg.decode(buf);
                        return msg;
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                },
                messageConsumer);
        nextPacketId++;
    }
}