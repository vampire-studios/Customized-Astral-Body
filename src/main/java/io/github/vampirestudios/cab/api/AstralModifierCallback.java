package io.github.vampirestudios.cab.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionResult;

public interface AstralModifierCallback {

    Event<AstralModifierCallback> EVENT = EventFactory.createArrayBacked(AstralModifierCallback.class, (listeners) -> {
        return (matrixStack, f, client, world) -> {
            for (AstralModifierCallback event : listeners) {
                return event.renderCustom(matrixStack, f, client, world);
            }

            return InteractionResult.FAIL;
        };
    });

    InteractionResult renderCustom(PoseStack matrixStack, float f, Minecraft client, ClientLevel world);

}