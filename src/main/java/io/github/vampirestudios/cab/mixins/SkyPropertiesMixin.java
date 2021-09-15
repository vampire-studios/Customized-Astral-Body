package io.github.vampirestudios.cab.mixins;

import io.github.vampirestudios.cab.api.AstralBodyModifier;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DimensionSpecialEffects.class)
public class SkyPropertiesMixin implements AstralBodyModifier {

}