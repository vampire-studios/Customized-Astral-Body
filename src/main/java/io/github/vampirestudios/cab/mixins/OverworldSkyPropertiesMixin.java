package io.github.vampirestudios.cab.mixins;

import io.github.vampirestudios.cab.Vector4i;
import io.github.vampirestudios.cab.api.AstralBodyModifier;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DimensionSpecialEffects.OverworldEffects.class)
public class OverworldSkyPropertiesMixin implements AstralBodyModifier {

	@Override
	public boolean hasCustomSky() {
		return true;
	}

	@Override
	public boolean hasCustomAstralBody() {
		return true;
	}

	@Override
	public float getSunSize() {
		return 80.0F;
	}

	@Override
	public float getMoonSize() {
		return 100.0F;
	}

	@Override
	public Vector4i getMoonTint() {
		return new Vector4i(112, 45, 255, 255);
	}
}