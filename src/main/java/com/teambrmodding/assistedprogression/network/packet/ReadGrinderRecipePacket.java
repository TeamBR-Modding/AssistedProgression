package com.teambrmodding.assistedprogression.network.packet;

import com.teambr.nucleus.network.INetworkMessage;
import com.teambrmodding.assistedprogression.managers.RecipeHelper;
import com.teambrmodding.assistedprogression.recipe.GrinderRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.RegistryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
public class ReadGrinderRecipePacket implements INetworkMessage {

    public List<GrinderRecipe> recipes = new ArrayList<>();

    @Override
    public void encode(PacketBuffer buffer) {
        for(GrinderRecipe recipe : RecipeHelper.grinderRecipes) {
            buffer.writeResourceLocation(recipe.id);
            recipe.input.write(buffer);
            buffer.writeItemStack(recipe.output);
        }
    }

    @Override
    public void decode(PacketBuffer buffer) {
        while (buffer.readableBytes() > 0) {
            final ResourceLocation id = buffer.readResourceLocation();
            final Ingredient input = Ingredient.read(buffer);
            final ItemStack output = buffer.readItemStack();
            recipes.add(new GrinderRecipe(id, input, output));
            buffer.discardReadBytes();
        }

    }

    /*******************************************************************************************************************
     * IMessageHandler                                                                                                 *
     *******************************************************************************************************************/

    public static void process(ReadGrinderRecipePacket message, Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() -> {
                RecipeHelper.grinderRecipes.clear();
                RecipeHelper.grinderRecipes.addAll(message.recipes);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
