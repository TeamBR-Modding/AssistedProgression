package com.teambrmodding.assistedprogression.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.teambr.nucleus.client.ModelHelper;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.*;
import java.util.function.Function;

/**
 * This file was created for Assisted-Progression
 * <p>
 * Assisted-Progression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 09/01/2019
 */

@SuppressWarnings("deprecation")
public final class ModelPipette implements IUnbakedModel {

    /*******************************************************************************************************************
     * Variables                                                                                                       *
     *******************************************************************************************************************/

    // Global
    // Reference to model location, used to get and wrap the default model
    public static final ModelResourceLocation LOCATION =
            new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "pipette"), "inventory");

    // Reference to the texture for the mask/cover
    public static final ResourceLocation maskLocation
            = new ResourceLocation(Reference.MOD_ID, "items/pipette_mask");


    // Minimal Z offset to prevent depth-fighting
    private static final float NORTH_Z_FLUID = 7.498f / 16f;
    private static final float SOUTH_Z_FLUID = 8.502f / 16f;

    // Cache of generated models, no need to re-bake each cycle
    private static final Map<String, IBakedModel> cache = new HashMap<>();

    // Local
    // Reference to stored fluid
    private Fluid fluid;

    // The base pipette model generated at load-time, does not contain mask or fluid
    private IBakedModel baseModel;

    /**
     * Creates a model wrapped around the baked model, allows rendering of fluids, be sure to set the base after
     * creation
     * @param fluid The fluid
     */
    public ModelPipette(@Nullable Fluid fluid) {
        this.fluid = fluid == null ? Fluids.EMPTY : fluid;
    }

    /*******************************************************************************************************************
     * IUnbakedModel                                                                                                   *
     *******************************************************************************************************************/

    /**
     * Bakes the model with all relevant information, this is the "live" model
     * @param bakery Minecraft model bakery
     * @param spriteGetter Function to get textures
     * @param sprite Sprite info
     * @param format Vertex format
     * @return A model baked with all info present
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public IBakedModel bake(@Nonnull ModelBakery bakery, @Nonnull java.util.function.Function<ResourceLocation, TextureAtlasSprite>
            spriteGetter, ISprite sprite, @Nullable VertexFormat format) {

        // Setup Perspectives
        IModelState state = sprite.getState();
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);

        // Sprites and quads initialization
        TRSRTransformation transform = state.apply(java.util.Optional.empty()).orElse(TRSRTransformation.identity());
        TextureAtlasSprite fluidSprite = null;
        TextureAtlasSprite particleSprite = null;
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        Random random = new Random();
        random.setSeed(42);

        // Load fluid sprite if not an empty pipette
        if (fluid != Fluids.EMPTY) {
            fluidSprite = spriteGetter.apply(fluid.getAttributes().getStillTexture());
        }

        // Draw the wrapped model, the pipette itself with no fluid or cover
        builder.addAll(baseModel.getQuads(null, null, random));

        // Draw fluid
        if (fluidSprite != null) {
            // We are going to use the mask as a stencil for the liquid
            TextureAtlasSprite liquid = spriteGetter.apply(maskLocation);

            // Build new texture based on stencil
            builder.addAll(ItemTextureQuadConverter.convertTextureHorizontal(format, transform, liquid, fluidSprite,
                    NORTH_Z_FLUID, Direction.NORTH, fluid.getAttributes().getColor(), 1));
            builder.addAll(ItemTextureQuadConverter.convertTextureHorizontal(format, transform, liquid, fluidSprite,
                    SOUTH_Z_FLUID, Direction.SOUTH,  fluid.getAttributes().getColor(), 1));
            particleSprite = fluidSprite;
        }

        // Draw mask
        if (maskLocation != null) {
            // Draw rest of pipette, the clear cover over the fluid, do this by making an item with texture of mask
            IBakedModel model =
                    (new ItemLayerModel(ImmutableList.of(maskLocation))).bake(bakery, spriteGetter, sprite, format);
            builder.addAll(model.getQuads(null, null, random));
            particleSprite = model.getParticleTexture();
        }

        // The fully processed model
        return new PipetteDynamicModel(bakery, baseModel, builder.build(), particleSprite, transformMap);
    }

    /**
     * Set the base model to the provided, we use this to wrap the vanilla model
     * @param base The vanilla model, no fluid or cover texture
     */
    public void setBaseModel(IBakedModel base) {
        this.baseModel = base;
    }

    /**
     * Process the model, used when adding data to the model
     * @param customData Custom info
     * @return An instance of the model with custom data applied
     */
    @Override
    public ModelPipette process(ImmutableMap<String, String> customData) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(customData.get("fluid")));

        if (fluid == null) fluid = this.fluid;

        // create new model with correct liquid
        return new ModelPipette(fluid);
    }

    /**
     * Get textures used in this model, not really needed for us as we stitch ours
     * @param modelGetter The texture getter
     * @param missingTextureErrors Error list
     * @return List of textures used by this model
     */
    @Override
    @Nonnull
    public Collection<ResourceLocation> getTextures(@Nonnull java.util.function.Function<ResourceLocation, IUnbakedModel> modelGetter,
                                                    @Nonnull Set<String> missingTextureErrors) {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.add(maskLocation);
        return builder.build();
    }

    /**
     * Not needed for us, return empty list
     * @return Empty list
     */
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    /*******************************************************************************************************************
     * Override Handler                                                                                                *
     *******************************************************************************************************************/

    /**
     * Class to define model data to bake at runtime. This will take the actual stack, and build a new model based on
     * what fluid is inside
     */
    public static final class PipetteOverrideList extends ItemOverrideList {

        // Public reference to an instance, we don't need a new one every time
        public static PipetteOverrideList INSTANCE;

        // Reference to the minecraft model bakery
        public final ModelBakery bakery;

        /**
         * Creates the handler
         * @param bakery The instance of the minecraft model bakery
         */
        public PipetteOverrideList(ModelBakery bakery) {
            this.bakery = bakery;
        }

        /**
         * Gets a model with context info
         * @param originalModel The orginal model, the one Minecraft is loading before you substitute
         * @param stack The itemstack for the model
         * @param world World
         * @param entity Entity holding
         * @return The new model to render instead, we will wrap the original add fluid info
         */
        @Override
        public IBakedModel getModelWithOverrides(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack,
                                                 @Nullable World world, @Nullable LivingEntity entity) {
            return FluidUtil.getFluidContained(stack)
                    .map(fluidStack -> {
                        Fluid fluid = fluidStack.getFluid();
                        String name = fluid.getRegistryName().toString();

                        if (!cache.containsKey(name)) {
                            ModelPipette parent = new ModelPipette(null).process(ImmutableMap.of("fluid", name));
                            parent.setBaseModel(originalModel);
                            Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                            textureGetter = location -> Minecraft.getInstance().getTextureMap().getAtlasSprite(location.toString());

                            IBakedModel bakedModel
                                    = parent.bake(bakery, textureGetter,
                                    (SimpleModelState) ModelHelper.DEFAULT_TOOL_STATE, DefaultVertexFormats.ITEM);
                            cache.put(name, bakedModel);
                            return bakedModel;
                        }

                        return cache.get(name);
                    })
                    // not a fluid item apparently
                    .orElse(((PipetteDynamicModel) originalModel).baseModel); // empty pipette
        }
    }

    /*******************************************************************************************************************
     * Baked Model                                                                                                     *
     *******************************************************************************************************************/

    /**
     * The baked instance of this model
     *
     * This will take in the original model created by Minecraft on load, and wrap around it. This is the model
     * to register in the loading stage, also has constructor that generates models at runtime based on info present
     */
    public static class PipetteDynamicModel extends BakedItemModel implements IDynamicBakedModel {

        // Model Bakery to pass into override
        private ModelBakery bakery;

        // Minecraft generated model
        public IBakedModel baseModel;

        public PipetteDynamicModel(ModelBakery bakery, IBakedModel base,
                                   ImmutableList<BakedQuad> quads,
                                   TextureAtlasSprite particle,
                                   ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms) {
            super(quads, particle, transforms, new PipetteOverrideList(bakery));
            this.bakery = bakery;
            this.baseModel = base;
        }

        /**
         * Creates a simple wrapper around the vanilla model to reflect into our dynamic model
         * @param modelBakery The instance of the model bakery
         * @param parent The model made by Minecraft
         */
        public PipetteDynamicModel(ModelBakery modelBakery, IBakedModel parent) {
            this(modelBakery, parent, ImmutableList.copyOf(parent.getQuads(null, null, new Random())),
                    parent.getParticleTexture(), PerspectiveMapWrapper.getTransforms(parent.getItemCameraTransforms()));
        }

        /**
         * Used to define the override list, since we don't want just the vanilla one
         * @return The instance of the override handler, creates it if not loaded
         */
        @Override
        @Nonnull
        public ItemOverrideList getOverrides() {
            if(PipetteOverrideList.INSTANCE == null ||
                    PipetteOverrideList.INSTANCE.bakery == null)
                PipetteOverrideList.INSTANCE = new PipetteOverrideList(bakery);
            return PipetteOverrideList.INSTANCE;
        }

        /**
         * Handle transforms based on state
         * @param cameraTransformType Camera Type
         * @return Rotations for perspective
         */
        @Override
        @Nonnull
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType) {
            // Wrap the base and have it handle the movement
            return PerspectiveMapWrapper
                    .handlePerspective(
                            this,
                            ModelHelper.DEFAULT_TOOL_STATE,
                            cameraTransformType);
        }

        /***************************************************************************************************************
         * Wrapper methods                                                                                             *
         ***************************************************************************************************************/

        @Nonnull @Override public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) { return baseModel.getQuads(state, side, rand, extraData); }
        @Override public IBakedModel getBakedModel() { return baseModel; }
        @Override public boolean isAmbientOcclusion() { return baseModel.isAmbientOcclusion(); }
        @Override public boolean isGui3d() { return baseModel.isGui3d(); }
        @Override public boolean isBuiltInRenderer() { return baseModel.isBuiltInRenderer(); }
        @Nonnull @Override public TextureAtlasSprite getParticleTexture() { return baseModel.getParticleTexture(); }
    }
}