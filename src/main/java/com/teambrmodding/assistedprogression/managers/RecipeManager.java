package com.teambrmodding.assistedprogression.managers;

import com.teambrmodding.assistedprogression.registries.AbstractRecipeHandler;
import com.teambrmodding.assistedprogression.registries.GrinderRecipeHandler;
import gnu.trove.map.hash.THashMap;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ServerCommandManager;

/**
  * This file was created for NeoTech
  *
  * NeoTech is licensed under the
  * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
  * http://creativecommons.org/licenses/by-nc-sa/4.0/
  *
  * @author Paul Davis - pauljoda
  * @since 2/15/2017
  */
public class RecipeManager {

    // The list of all recipe handlers
    public static THashMap<RecipeType, AbstractRecipeHandler> recipeHandlers = new THashMap<>();

    /**
      * Called to register things
      */
    public static void preInit() {
        CraftingRecipeManager.preInit();
    }

    /**
      * Builds the recipe registry
      */
    public static void init() {
        recipeHandlers.put(RecipeType.GRINDER, new GrinderRecipeHandler().loadHandler());
    }

    /**
      * Loads all the commands from the handlers
      * @param manager The manager to register to
      */
    public static void initCommands(ServerCommandManager manager) {
        for(RecipeType recipeType : recipeHandlers.keySet()) {
            CommandBase command = recipeHandlers.get(recipeType).getCommand();
            if(command != null)
                manager.registerCommand(command);
        }
    }

    /*******************************************************************************************************************
      * Helper Methods                                                                                                  *
      *******************************************************************************************************************/

    /**
      * Used to get a recipe handler
      * @param recipeType The recipe type
      * @return The recipe handler
      */
    public static  <H extends AbstractRecipeHandler> H getHandler(RecipeType recipeType) {
        return (H) recipeHandlers.get(recipeType);
    }

    public enum RecipeType {
        GRINDER("grinder");

        private String name;

        RecipeType(String name) {
            this.name = name;
        }

        /**
          * Get the name of this recipe type
          * @return The name
          */
        public String getName() {
            return name;
        }
    }
}