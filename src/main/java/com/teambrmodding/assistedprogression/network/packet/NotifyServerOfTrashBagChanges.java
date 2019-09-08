package com.teambrmodding.assistedprogression.network.packet;

import com.teambr.nucleus.network.packet.INetworkMessage;
import com.teambrmodding.assistedprogression.common.container.TrashBagContainer;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 9/5/2019
 */
public class NotifyServerOfTrashBagChanges implements INetworkMessage {

    public CompoundNBT tag;

    public NotifyServerOfTrashBagChanges() {}

    public NotifyServerOfTrashBagChanges(CompoundNBT inTag) {
        this.tag = inTag;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeCompoundTag(tag);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        tag = buffer.readCompoundTag();
    }

    /*******************************************************************************************************************
     * IMessageHandler                                                                                                 *
     *******************************************************************************************************************/

    public static void process(NotifyServerOfTrashBagChanges message, Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayerEntity player = ctx.get().getSender();
            if(player.openContainer instanceof TrashBagContainer) {
                TrashBagContainer container = (TrashBagContainer) player.openContainer;
                container.trashBag.setTag(message.tag);
                container.detectAndSendChanges();
            }
            ctx.get().setPacketHandled(true);
        }
    }
}
