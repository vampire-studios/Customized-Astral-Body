package io.github.vampirestudios.cab.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

public interface AstralBodyModifier {

    @Environment(EnvType.CLIENT)
    default boolean isEndSky() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    default boolean hasCustomAstralBody() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    default float getSunSize() {
        return 30.0F;
    }

    @Environment(EnvType.CLIENT)
    default float getMoonSize() {
        return 20.0F;
    }

    @Environment(EnvType.CLIENT)
    default Vector3f getSunTint() {
        return new Vector3f(1.0F, 1.0F, 1.0F);
    }

    @Environment(EnvType.CLIENT)
    default Vector3f getMoonTint() {
        return new Vector3f(1.0F, 1.0F, 1.0F);
    }

    @Environment(EnvType.CLIENT)
    default Identifier getSunTexture() {
        return new Identifier("texture/environment/sun.png");
    }

    @Environment(EnvType.CLIENT)
    default Identifier getMoonTexture() {
        return new Identifier("texture/environment/moon_phases.png");
    }

    default boolean hasCustomSky() {
        return false;
    }

    default boolean hasFullyCustomSky() {
        return false;
    }

    default void render(MatrixStack matrixStack, float f, MinecraftClient client, ClientWorld world) {

    }

}