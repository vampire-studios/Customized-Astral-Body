package io.github.vampirestudios.cab.mixins;

import io.github.vampirestudios.cab.api.AstralBodyModifier;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Dimension.class)
public class DimensionMixin implements AstralBodyModifier {

}