package suszombification.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.resources.ResourceLocation;
import suszombification.SZClientHandler;
import suszombification.SuspiciousZombification;

public class ZombifiedCatZombieLayer extends RenderLayer<CatRenderState, CatModel> {
	private static final ResourceLocation TEXTURE = SuspiciousZombification.resLoc("textures/entity/zombified_cat_zombie_layer.png");
	private final CatModel model;

	public ZombifiedCatZombieLayer(RenderLayerParent<CatRenderState, CatModel> parentRenderer, EntityModelSet modelSet) {
		super(parentRenderer);
		model = new CatModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_CAT_ZOMBIE_LAYER));
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, CatRenderState renderState, float yRot, float xRot) {
		coloredCutoutModelCopyLayerRender(model, TEXTURE, pose, buffer, packedLight, renderState, 0xFFFFFFFF);
	}
}
