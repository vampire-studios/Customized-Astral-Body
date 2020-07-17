package io.github.vampirestudios.cab.mixins;

import io.github.vampirestudios.cab.api.AstralBodyModifier;
import net.minecraft.client.render.SkyProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SkyProperties.class)
public class SkyPropertiesMixin implements AstralBodyModifier {

}