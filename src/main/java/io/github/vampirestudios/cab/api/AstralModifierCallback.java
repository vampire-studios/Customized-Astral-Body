package io.github.vampirestudios.cab.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;

public interface AstralModifierCallback {

    Event<AstralModifierCallback> EVENT = EventFactory.createArrayBacked(AstralModifierCallback.class, (listeners) -> {
        return (matrixStack, f, client, world) -> {
            for (AstralModifierCallback event : listeners) {
                return event.renderCustom(matrixStack, f, client, world);
            }

            return ActionResult.FAIL;
        };
    });

    ActionResult renderCustom(MatrixStack matrixStack, float f, MinecraftClient client, ClientWorld world);

}