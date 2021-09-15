package io.github.vampirestudios.cab.api;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.vampirestudios.cab.Vector4i;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;

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
    default ResourceLocation getSunTexture() {
        return new ResourceLocation("textures/environment/sun.png");
    }

    @Environment(EnvType.CLIENT)
    default ResourceLocation getMoonTexture() {
        return new ResourceLocation("textures/environment/moon_phases.png");
    }

    default boolean hasCustomSky() {
        return false;
    }

    default boolean hasFullyCustomSky() {
        return false;
    }

    default void render(PoseStack matrixStack, float f, Minecraft client, ClientLevel world) {

    }

}