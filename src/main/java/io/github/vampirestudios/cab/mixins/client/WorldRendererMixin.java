package io.github.vampirestudios.cab.mixins.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.vampirestudios.cab.api.AstralBodyModifier;
import io.github.vampirestudios.cab.rendering.AstralRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow private ClientLevel level;
    @Shadow private VertexBuffer starBuffer;
    @Shadow private VertexBuffer skyBuffer;
    @Shadow private VertexBuffer darkBuffer;
    @Shadow @Final private static ResourceLocation MOON_LOCATION;
    @Shadow @Final private static ResourceLocation SUN_LOCATION;

    @Shadow protected abstract void renderEndSky(PoseStack matrixStack);

    /**
     * @author OliviaTheVampire
     */
    @Overwrite
    public void renderSky(PoseStack matrices, Matrix4f matrix4f, float f, Runnable runnable) {
        if (((AstralBodyModifier) this.level.effects()).hasCustomSky()) {
            if (this.minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.END || ((AstralBodyModifier) this.level.effects()).isEndSky()) {
                this.renderEndSky(matrices);
            }
            if (this.minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.NORMAL) {
                if (((AstralBodyModifier) this.level.effects()).hasCustomAstralBody()) {
                    AstralRendering.renderCustomAstralBody(matrices, minecraft, matrix4f, this.level.effects(), level, skyBuffer, darkBuffer,
                            starBuffer, f);
                } else {
                    RenderSystem.disableTexture();
                    Vec3 vec3d = this.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), f);
                    float g = (float)vec3d.x;
                    float h = (float)vec3d.y;
                    float i = (float)vec3d.z;
                    FogRenderer.levelFogColor();
                    BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
                    RenderSystem.depthMask(false);
                    RenderSystem.setShaderColor(g, h, i, 1.0F);
                    ShaderInstance shader = RenderSystem.getShader();
                    this.skyBuffer.drawWithShader(matrices.last().pose(), matrix4f, shader);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    float[] fs = this.level.effects().getSunriseColor(this.level.getTimeOfDay(f), f);
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
                        s = Mth.sin(this.level.getSunAngle(f)) < 0.0F ? 180.0F : 0.0F;
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

                        bufferBuilder.end();
                        BufferUploader.end(bufferBuilder);
                        matrices.popPose();
                    }

                    RenderSystem.enableTexture();
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    matrices.pushPose();
                    s = 1.0F - this.level.getRainLevel(f);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
                    matrices.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
                    matrices.mulPose(Vector3f.XP.rotationDegrees(this.level.getTimeOfDay(f) * 360.0F));
                    Matrix4f matrix4f3 = matrices.last().pose();
                    t = 30.0F;
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, SUN_LOCATION);
                    bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    bufferBuilder.vertex(matrix4f3, -t, 100.0F, -t).uv(0.0F, 0.0F).endVertex();
                    bufferBuilder.vertex(matrix4f3, t, 100.0F, -t).uv(1.0F, 0.0F).endVertex();
                    bufferBuilder.vertex(matrix4f3, t, 100.0F, t).uv(1.0F, 1.0F).endVertex();
                    bufferBuilder.vertex(matrix4f3, -t, 100.0F, t).uv(0.0F, 1.0F).endVertex();
                    bufferBuilder.end();
                    BufferUploader.end(bufferBuilder);
                    t = 20.0F;
                    RenderSystem.setShaderTexture(0, MOON_LOCATION);
                    int u = this.level.getMoonPhase();
                    int v = u % 4;
                    int w = u / 4 % 2;
                    float x = (float)(v) / 4.0F;
                    p = (float)(w) / 2.0F;
                    q = (float)(v + 1) / 4.0F;
                    r = (float)(w + 1) / 2.0F;
                    bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    bufferBuilder.vertex(matrix4f3, -t, -100.0F, t).uv(q, r).endVertex();
                    bufferBuilder.vertex(matrix4f3, t, -100.0F, t).uv(x, r).endVertex();
                    bufferBuilder.vertex(matrix4f3, t, -100.0F, -t).uv(x, p).endVertex();
                    bufferBuilder.vertex(matrix4f3, -t, -100.0F, -t).uv(q, p).endVertex();
                    bufferBuilder.end();
                    BufferUploader.end(bufferBuilder);
                    RenderSystem.disableTexture();
                    float ab = this.level.getStarBrightness(f) * s;
                    if (ab > 0.0F) {
                        RenderSystem.setShaderColor(ab, ab, ab, ab);
                        this.starBuffer.drawWithShader(matrices.last().pose(), matrix4f, GameRenderer.getPositionShader());
                    }

                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableBlend();
                    matrices.popPose();
                    RenderSystem.disableTexture();
                    RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                    double d = this.minecraft.player.getEyePosition(f).y - this.level.getLevelData().getHorizonHeight(this.level);
                    if (d < 0.0D) {
                        matrices.pushPose();
                        matrices.translate(0.0D, 12.0D, 0.0D);
                        this.darkBuffer.drawWithShader(matrices.last().pose(), matrix4f, shader);
                        matrices.popPose();
                    }

                    if (this.level.effects().hasGround()) {
                        RenderSystem.setShaderColor(g * 0.2F + 0.04F, h * 0.2F + 0.04F, i * 0.6F + 0.1F, 1.0F);
                    } else {
                        RenderSystem.setShaderColor(g, h, i, 1.0F);
                    }

                    RenderSystem.enableTexture();
                    RenderSystem.depthMask(true);
                }
            }
            if (((AstralBodyModifier) this.level.effects()).hasFullyCustomSky()) {
                ((AstralBodyModifier) this.level.effects()).render(matrices, f, minecraft, level);
            }
        } else {
            RenderSystem.disableTexture();
            Vec3 vec3d = this.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), f);
            float g = (float)vec3d.x;
            float h = (float)vec3d.y;
            float i = (float)vec3d.z;
            FogRenderer.levelFogColor();
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderColor(g, h, i, 1.0F);
            ShaderInstance shader = RenderSystem.getShader();
            this.skyBuffer.drawWithShader(matrices.last().pose(), matrix4f, shader);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float[] fs = this.level.effects().getSunriseColor(this.level.getTimeOfDay(f), f);
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
                s = Mth.sin(this.level.getSunAngle(f)) < 0.0F ? 180.0F : 0.0F;
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

                bufferBuilder.end();
                BufferUploader.end(bufferBuilder);
                matrices.popPose();
            }

            RenderSystem.enableTexture();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            matrices.pushPose();
            s = 1.0F - this.level.getRainLevel(f);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
            matrices.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
            matrices.mulPose(Vector3f.XP.rotationDegrees(this.level.getTimeOfDay(f) * 360.0F));
            Matrix4f matrix4f3 = matrices.last().pose();
            t = 30.0F;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SUN_LOCATION);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.vertex(matrix4f3, -t, 100.0F, -t).uv(0.0F, 0.0F).endVertex();
            bufferBuilder.vertex(matrix4f3, t, 100.0F, -t).uv(1.0F, 0.0F).endVertex();
            bufferBuilder.vertex(matrix4f3, t, 100.0F, t).uv(1.0F, 1.0F).endVertex();
            bufferBuilder.vertex(matrix4f3, -t, 100.0F, t).uv(0.0F, 1.0F).endVertex();
            bufferBuilder.end();
            BufferUploader.end(bufferBuilder);
            t = 20.0F;
            RenderSystem.setShaderTexture(0, MOON_LOCATION);
            int u = this.level.getMoonPhase();
            int v = u % 4;
            int w = u / 4 % 2;
            float x = (float)(v) / 4.0F;
            p = (float)(w) / 2.0F;
            q = (float)(v + 1) / 4.0F;
            r = (float)(w + 1) / 2.0F;
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.vertex(matrix4f3, -t, -100.0F, t).uv(q, r).endVertex();
            bufferBuilder.vertex(matrix4f3, t, -100.0F, t).uv(x, r).endVertex();
            bufferBuilder.vertex(matrix4f3, t, -100.0F, -t).uv(x, p).endVertex();
            bufferBuilder.vertex(matrix4f3, -t, -100.0F, -t).uv(q, p).endVertex();
            bufferBuilder.end();
            BufferUploader.end(bufferBuilder);
            RenderSystem.disableTexture();
            float ab = this.level.getStarBrightness(f) * s;
            if (ab > 0.0F) {
                RenderSystem.setShaderColor(ab, ab, ab, ab);
                this.starBuffer.drawWithShader(matrices.last().pose(), matrix4f, GameRenderer.getPositionShader());
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            matrices.popPose();
            RenderSystem.disableTexture();
            RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
            double d = this.minecraft.player.getEyePosition(f).y - this.level.getLevelData().getHorizonHeight(this.level);
            if (d < 0.0D) {
                matrices.pushPose();
                matrices.translate(0.0D, 12.0D, 0.0D);
                this.darkBuffer.drawWithShader(matrices.last().pose(), matrix4f, shader);
                matrices.popPose();
            }

            if (this.level.effects().hasGround()) {
                RenderSystem.setShaderColor(g * 0.2F + 0.04F, h * 0.2F + 0.04F, i * 0.6F + 0.1F, 1.0F);
            } else {
                RenderSystem.setShaderColor(g, h, i, 1.0F);
            }

            RenderSystem.enableTexture();
            RenderSystem.depthMask(true);
        }
    }

}
