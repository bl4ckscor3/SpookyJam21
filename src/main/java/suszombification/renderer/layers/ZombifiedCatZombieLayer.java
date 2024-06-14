package suszombification.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import suszombification.SZClientHandler;
import suszombification.SuspiciousZombification;

public class ZombifiedCatZombieLayer<T extends Cat> extends RenderLayer<T, CatModel<T>> {
	private static final ResourceLocation TEXTURE = SuspiciousZombification.resLoc("textures/entity/zombified_cat_zombie_layer.png");
	private final CatModel<T> model;

	public ZombifiedCatZombieLayer(RenderLayerParent<T, CatModel<T>> parentRenderer, EntityModelSet modelSet) {
		super(parentRenderer);
		model = new CatModel<>(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_CAT_ZOMBIE_LAYER));
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, T cat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		coloredCutoutModelCopyLayerRender(getParentModel(), model, TEXTURE, pose, buffer, packedLight, cat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 0xFFFFFFFF);
	}
}
