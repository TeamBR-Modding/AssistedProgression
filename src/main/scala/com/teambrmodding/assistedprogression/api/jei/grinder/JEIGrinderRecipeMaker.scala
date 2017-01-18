package com.teambrmodding.assistedprogression.api.jei.grinder

import java.util

import com.teambr.bookshelf.helper.LogHelper
import com.teambrmodding.assistedprogression.managers.RecipeManager
import com.teambrmodding.assistedprogression.registries.GrinderRecipeHandler

import scala.collection.JavaConversions._

/**
  * Created by Dyonovan on 1/16/2016.
  */
object JEIGrinderRecipeMaker  {

    def getRecipes: java.util.List[JEIGrinderRecipe] = {
        val recipes = new util.ArrayList[JEIGrinderRecipe]()
        val grinder = RecipeManager.getHandler[GrinderRecipeHandler](RecipeManager.Grinder).recipes
        for (recipe <- grinder) {
            val input = recipe.getItemStackFromString(recipe.input)
            val output = recipe.getItemStackFromString(recipe.output)
            if (input != null && output != null) {
                output.stackSize = recipe.qty
                recipes.add(new JEIGrinderRecipe(input, output))
            } else
                LogHelper.severe("[Assisted Progression] GrinderRecipes json is corrupt! Please delete and recreate!")
        }
        recipes
    }
}
