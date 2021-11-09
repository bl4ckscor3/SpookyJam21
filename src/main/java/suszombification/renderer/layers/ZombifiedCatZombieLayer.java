package suszombification.renderer.layers;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.CatModel;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.ResourceLocation;
import suszombification.SuspiciousZombification;

public class ZombifiedCatZombieLayer<T extends CatEntity> extends LayerRenderer<T, CatModel<T>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SuspiciousZombification.MODID, "textures/entity/zombified_cat_zombie_layer.png");
	private final CatModel<T> model = new CatModel<>(0.01F);

	public ZombifiedCatZombieLayer(IEntityRenderer<T, CatModel<T>> parentRenderer) {
		super(parentRenderer);
	}

	@Override
	public void render(MatrixStack matrix, IRenderTypeBuffer buffer, int packedLight, T cat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		coloredCutoutModelCopyLayerRender(getParentModel(), model, TEXTURE, matrix, buffer, packedLight, cat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);
	}
}