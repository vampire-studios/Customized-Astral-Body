package io.github.vampirestudios.cab.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.vampirestudios.cab.Vector4i;
import io.github.vampirestudios.cab.api.AstralBodyModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class AstralRendering {

    public static void renderCustomAstralBody(PoseStack matrices, Minecraft client, Matrix4f projectionMatrix, DimensionSpecialEffects dimension, ClientLevel world, VertexBuffer lightSkyBuffer,
                                              VertexBuffer darkSkyBuffer, VertexBuffer starsBuffer, float f, Runnable skyFogSetup) {
        RenderSystem.disableTexture();
        Vec3 vec3d = world.getSkyColor(client.gameRenderer.getMainCamera().getPosition(), f);
        float g = (float)vec3d.x;
        float h = (float)vec3d.y;
        float i = (float)vec3d.z;
        FogRenderer.levelFogColor();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(g, h, i, 1.0F);
        ShaderInstance shader = RenderSystem.getShader();
        lightSkyBuffer.drawWithShader(matrices.last().pose(), projectionMatrix, shader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] fs = world.effects().getSunriseColor(world.getTimeOfDay(f), f);
        float s;
        float t;
        float p;
        float q;
        float r;
        if (fs != null) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.disableTexture();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            matrices.pushPose();
            matrices.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            s = Mth.sin(world.getSunAngle(f)) < 0.0F ? 180.0F : 0.0F;
            matrices.mulPose(Vector3f.ZP.rotationDegrees(s));
            matrices.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
            float k = fs[0];
            t = fs[1];
            float m = fs[2];
            Matrix4f matrix4f2 = matrices.last().pose();
            bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            bufferBuilder.vertex(matrix4f2, 0.0F, 100.0F, 0.0F).color(k, t, m, fs[3]).endVertex();

            for(int o = 0; o <= 16; ++o) {
                p = (float)o * 6.2831855F / 16.0F;
                q = Mth.sin(p);
                r = Mth.cos(p);
                bufferBuilder.vertex(matrix4f2, q * 120.0F, r * 120.0F, -r * 40.0F * fs[3]).color(fs[0], fs[1], fs[2], 0.0F).endVertex();
            }
            BufferUploader.drawWithShader(bufferBuilder.end());
            matrices.popPose();
        }

        RenderSystem.enableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        matrices.pushPose();
        s = 1.0F - world.getRainLevel(f);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
        matrices.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrices.mulPose(Vector3f.XP.rotationDegrees(world.getTimeOfDay(f) * 360.0F));
        Matrix4f matrix4f3 = matrices.last().pose();
        t = ((AstralBodyModifier)dimension).getSunSize();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Vector4i sunTint = ((AstralBodyModifier)dimension).getSunTint();
        RenderSystem.setShaderTexture(0, ((AstralBodyModifier)dimension).getSunTexture());
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f3, -t, 100.0F, -t).color(sunTint.x(), sunTint.y(), sunTint.z(), sunTint.w()).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f3, t, 100.0F, -t).color(sunTint.x(), sunTint.y(), sunTint.z(), sunTint.w()).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f3, t, 100.0F, t).color(sunTint.x(), sunTint.y(), sunTint.z(), sunTint.w()).uv(1.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix4f3, -t, 100.0F, t).color(sunTint.x(), sunTint.y(), sunTint.z(), sunTint.w()).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
        t = ((AstralBodyModifier)dimension).getMoonSize();
        Vector4i moonTint = ((AstralBodyModifier)dimension).getSunTint();
        RenderSystem.setShaderTexture(0, ((AstralBodyModifier)dimension).getMoonTexture());
        int u = world.getMoonPhase();
        int v = u % 4;
        int w = u / 4 % 2;
        float x = (float)(v) / 4.0F;
        p = (float)(w) / 2.0F;
        q = (float)(v + 1) / 4.0F;
        r = (float)(w + 1) / 2.0F;
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f3, -t, -100.0F, t).color(moonTint.x(), moonTint.y(), moonTint.z(), moonTint.w()).uv(q, r).endVertex();
        bufferBuilder.vertex(matrix4f3, t, -100.0F, t).color(moonTint.x(), moonTint.y(), moonTint.z(), moonTint.w()).uv(x, r).endVertex();
        bufferBuilder.vertex(matrix4f3, t, -100.0F, -t).color(moonTint.x(), moonTint.y(), moonTint.z(), moonTint.w()).uv(x, p).endVertex();
        bufferBuilder.vertex(matrix4f3, -t, -100.0F, -t).color(moonTint.x(), moonTint.y(), moonTint.z(), moonTint.w()).uv(q, p).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
        RenderSystem.disableTexture();
        float ab = world.getStarBrightness(f) * s;
        if (ab > 0.0F) {
            RenderSystem.setShaderColor(u, u, u, u);
            FogRenderer.setupNoFog();
            starsBuffer.bind();
            starsBuffer.drawWithShader(matrices.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            skyFogSetup.run();
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        matrices.popPose();
        RenderSystem.disableTexture();
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
        double d = client.player.getEyePosition(f).y - world.getLevelData().getHorizonHeight(world);
        if (d < 0.0D) {
            matrices.pushPose();
            matrices.translate(0.0D, 12.0D, 0.0D);
            darkSkyBuffer.drawWithShader(matrices.last().pose(), projectionMatrix, shader);
            matrices.popPose();
        }

        if (world.effects().hasGround()) {
            RenderSystem.setShaderColor(g * 0.2F + 0.04F, h * 0.2F + 0.04F, i * 0.6F + 0.1F, 1.0F);
        } else {
            RenderSystem.setShaderColor(g, h, i, 1.0F);
        }

        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
    }

}
