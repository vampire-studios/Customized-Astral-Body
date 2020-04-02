package io.github.vampirestudios.cab.mixins;

import io.github.vampirestudios.cab.rendering.AstralRendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;
    @Shadow private ClientWorld world;
    @Shadow private VertexBuffer starsBuffer;
    @Shadow private VertexBuffer lightSkyBuffer;
    @Shadow private VertexBuffer darkSkyBuffer;
    @Shadow @Final private VertexFormat skyVertexFormat;
    @Shadow @Final private static Identifier MOON_PHASES;
    @Shadow @Final private static Identifier SUN;
    @Shadow @Final private TextureManager textureManager;

    @Inject(method = "renderSky", at=@At("RETURN"))
    public void onRenderSkyPost(MatrixStack matrixStack, float f, CallbackInfo info) {
        AstralRendering.renderCustomAstralBody(matrixStack, client, this.world.dimension, world, lightSkyBuffer, darkSkyBuffer,
                starsBuffer, skyVertexFormat, f, textureManager, MOON_PHASES, SUN);
    }

}
