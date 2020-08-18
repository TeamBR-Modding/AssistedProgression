package com.teambrmodding.assistedprogression.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import com.teambr.nucleus.client.ModelHelper;
import com.teambr.nucleus.util.RenderUtils;
import com.teambrmodding.assistedprogression.lib.Reference;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
public final class ModelPipette implements IModelGeometry<ModelPipette> {

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
     *
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
     *
     * @param bakery       Minecraft model bakery
     * @param spriteGetter Function to get textures
     *
     * @return A model baked with all info present
     */
    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial,
            TextureAtlasSprite> spriteGetter, IModelTransform modelTransform,
                            ItemOverrideList overrides, ResourceLocation modelLocation) {

        // Setup Render Material
        RenderMaterial maskMaterial =
                new RenderMaterial(RenderUtils.MC_BLOCKS_RESOURCE_LOCATION, maskLocation);

        IModelTransform transformsFromModel = ModelHelper.DEFAULT_TOOL_STATE;

        // Sprites and quads initialization
        TextureAtlasSprite fluidSprite = fluid != Fluids.EMPTY ?
                spriteGetter.apply(ForgeHooksClient.getBlockMaterial(fluid.getAttributes().getStillTexture())) :
                null;
        TextureAtlasSprite particleSprite = spriteGetter.apply(maskMaterial);

        // Setup Perspectives
        ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transformMap =
                PerspectiveMapWrapper.getTransforms(new ModelTransformComposition(transformsFromModel, modelTransform));


        TransformationMatrix transform = modelTransform.getRotation();
        ItemMultiLayerBakedModel.Builder builder =
                ItemMultiLayerBakedModel.builder(owner, particleSprite,
                        new PipetteOverrideList(bakery, owner),
                        transformMap);

        Random random = new Random();
        random.setSeed(42);
        builder.setParticle(particleSprite);

        // Draw the wrapped model, the pipette itself with no fluid or cover
        builder.addQuads(RenderType.getCutoutMipped(), baseModel.getQuads(null, null, random));

        // We are going to use the mask as a stencil for the liquid
        TextureAtlasSprite liquidMask = spriteGetter.apply(maskMaterial);

        // Draw fluid
        if (fluidSprite != null) {

            // Build new texture based on stencil
            int luminosity = fluid.getAttributes().getLuminosity();
            int color = fluid.getAttributes().getColor();

            builder.addQuads(ItemLayerModel.getLayerRenderType(luminosity > 0),
                    ItemTextureQuadConverter.convertTexture(transform, liquidMask, fluidSprite,
                            NORTH_Z_FLUID, Direction.NORTH, color, 1, luminosity));
            builder.addQuads(ItemLayerModel.getLayerRenderType(luminosity > 0),
                    ItemTextureQuadConverter.convertTexture(transform, liquidMask, fluidSprite,
                            SOUTH_Z_FLUID, Direction.SOUTH, color, 1, luminosity));

            particleSprite = fluidSprite;
            builder.setParticle(particleSprite);
        }

        // Draw mask
        // Draw rest of pipette, the clear cover over the fluid, do this by making an item with texture of mask
        IBakedModel model =
                (new ItemLayerModel(ImmutableList.of(maskMaterial))).bake(owner, bakery, spriteGetter,
                        modelTransform, overrides, modelLocation);
        builder.addQuads(RenderType.getCutout(), model.getQuads(null, null, random));


        // The fully processed model
        return builder.build();
    }

    /**
     * Set the base model to the provided, we use this to wrap the vanilla model
     *
     * @param base The vanilla model, no fluid or cover texture
     */
    public void setBaseModel(IBakedModel base) {
        this.baseModel = base;
    }

    /**
     * Process the model, used when adding data to the model
     *
     * @param customData Custom info
     *
     * @return An instance of the model with custom data applied
     */
    public ModelPipette withFluid(ImmutableMap<String, String> customData) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(customData.get("fluid")));

        if (fluid == null) fluid = this.fluid;

        // create new model with correct liquid
        return new ModelPipette(fluid);
    }

    /**
     * Get textures used in this model, not really needed for us as we stitch ours
     *
     * @param modelGetter          The texture getter
     * @param missingTextureErrors Error list
     *
     * @return List of textures used by this model
     */
    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation,
            IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Set<RenderMaterial> texs = Sets.newHashSet();

        if (owner.isTexturePresent("mask")) texs.add(owner.resolveTexture("mask"));

        return texs;
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
        private final IModelConfiguration owner;

        /**
         * Creates the handler
         *
         * @param bakery The instance of the minecraft model bakery
         */
        public PipetteOverrideList(ModelBakery bakery, IModelConfiguration own) {
            this.bakery = bakery;
            this.owner = own;
        }

        /**
         * Gets a model with context info
         *
         * @param originalModel The orginal model, the one Minecraft is loading before you substitute
         * @param stack         The itemstack for the model
         * @param world         World
         * @param entity        Entity holding
         *
         * @return The new model to render instead, we will wrap the original add fluid info
         */
        @Override
        public IBakedModel func_239290_a_(IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world,
                                          @Nullable LivingEntity entity) {

            return FluidUtil.getFluidContained(stack)
                    .map(fluidStack -> {
                        Fluid fluid = fluidStack.getFluid();
                        String name = fluid.getRegistryName().toString();

                        if (!cache.containsKey(name)) {
                            ModelPipette parent = new ModelPipette(null).withFluid(ImmutableMap.of("fluid", name));
                            parent.setBaseModel(originalModel);

                            IBakedModel bakedModel
                                    = parent.bake(owner, bakery,
                                    ModelLoader.defaultTextureGetter(), ModelRotation.X0_Y0, this, LOCATION);
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
     * <p>
     * This will take in the original model created by Minecraft on load, and wrap around it. This is the model
     * to register in the loading stage, also has constructor that generates models at runtime based on info present
     */
    public static class PipetteDynamicModel extends BakedItemModel implements IDynamicBakedModel {

        // Model Bakery to pass into override
        private ModelBakery bakery;

        // Minecraft generated model
        public IBakedModel baseModel;

        public IModelConfiguration owner;

        public PipetteDynamicModel(ModelBakery bakery, IBakedModel base,
                                   ImmutableList<BakedQuad> quads, IModelConfiguration owner,
                                   TextureAtlasSprite particle,
                                   ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transforms) {
            super(quads, particle, transforms, new PipetteOverrideList(bakery, owner), true, true);
            this.bakery = bakery;
            this.baseModel = base;
            this.owner = owner;
        }

        /**
         * Creates a simple wrapper around the vanilla model to reflect into our dynamic model
         *
         * @param modelBakery The instance of the model bakery
         * @param parent      The model made by Minecraft
         */
        public PipetteDynamicModel(ModelBakery modelBakery, IBakedModel parent, IModelConfiguration owner) {
            this(modelBakery, parent, ImmutableList.copyOf(parent.getQuads(null, null, new Random())), owner,
                    parent.getParticleTexture(), PerspectiveMapWrapper.getTransforms(parent.getItemCameraTransforms()));
        }

        /**
         * Used to define the override list, since we don't want just the vanilla one
         *
         * @return The instance of the override handler, creates it if not loaded
         */
        @Override
        @Nonnull
        public ItemOverrideList getOverrides() {
            if (PipetteOverrideList.INSTANCE == null ||
                    PipetteOverrideList.INSTANCE.bakery == null)
                PipetteOverrideList.INSTANCE = new PipetteOverrideList(bakery, owner);
            return PipetteOverrideList.INSTANCE;
        }

        @Override
        public IBakedModel handlePerspective(ItemCameraTransforms.TransformType type, MatrixStack mat) {
            return super.handlePerspective(type, mat);
        }

        /***************************************************************************************************************
         * Wrapper methods                                                                                             *
         ***************************************************************************************************************/

        @Nonnull
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state,
                                        @Nullable Direction side, @Nonnull Random rand,
                                        @Nonnull IModelData extraData) {
            return baseModel.getQuads(state, side, rand, extraData);
        }

        @Override
        public IBakedModel getBakedModel() { return baseModel; }

        @Override
        public boolean isAmbientOcclusion() { return baseModel.isAmbientOcclusion(); }

        @Override
        public boolean isGui3d() { return baseModel.isGui3d(); }

        @Override
        public boolean isBuiltInRenderer() { return baseModel.isBuiltInRenderer(); }

        @Nonnull
        @Override
        public TextureAtlasSprite getParticleTexture() { return baseModel.getParticleTexture(); }
    }
}