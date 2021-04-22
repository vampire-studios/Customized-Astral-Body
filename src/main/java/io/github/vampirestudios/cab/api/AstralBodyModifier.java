package io.github.vampirestudios.cab.api;

import io.github.vampirestudios.cab.Vector4i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
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
    default Vector4i getSunTint() {
        return new Vector4i(255, 255, 255, 255);
    }

    @Environment(EnvType.CLIENT)
    default Vector4i getMoonTint() {
        return new Vector4i(255, 255, 255, 255);
    }

    @Environment(EnvType.CLIENT)
    default Identifier getSunTexture() {
        return new Identifier("textures/environment/sun.png");
    }

    @Environment(EnvType.CLIENT)
    default Identifier getMoonTexture() {
        return new Identifier("textures/environment/moon_phases.png");
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