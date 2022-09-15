package io.github.vampirestudios.cab;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.vampirestudios.cab.api.AstralBodyModifier;
import io.github.vampirestudios.cab.rendering.AstralRendering;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CustomAstralBody implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("You're running Custom Astral Body v2.0.0-1.16.3");

        WorldRenderEvents.START.register(new WorldRenderEvents.Start() {
            @Override
            public void onStart(WorldRenderContext context) {
                ClientLevel clientLevel = context.world();
                Minecraft minecraft = context.gameRenderer().getMinecraft();
                PoseStack matrices = context.matrixStack();
                Matrix4f projectionMatrix = context.projectionMatrix();
                context.
                if (((AstralBodyModifier) clientLevel.effects()).hasCustomSky()) {
                    if (minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.END || ((AstralBodyModifier) clientLevel.effects()).isEndSky()) {
                        context.worldRenderer().renderEndSky(matrices);
                    }
                    if (minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.NORMAL) {
                        if (((AstralBodyModifier) clientLevel.effects()).hasCustomAstralBody()) {
                            AstralRendering.renderCustomAstralBody(matrices, minecraft, projectionMatrix, clientLevel.effects(), clientLevel, skyBuffer, darkBuffer,
                                    starBuffer, partialTick, skyFogSetup);
                        } else {
                            RenderSystem.disableTexture();
                            Vec3 vec3d = clientLevel.getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), partialTick);
                            float g = (float)vec3d.x;
                            float h = (float)vec3d.y;
                            float i = (float)vec3d.z;
                            FogRenderer.levelFogColor();
                            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
                            RenderSystem.depthMask(false);
                            RenderSystem.setShaderColor(g, h, i, 1.0F);
                            ShaderInstance shader = RenderSystem.getShader();
                            this.skyBuffer.drawWithShader(matrices.last().pose(), projectionMatrix, shader);
                            RenderSystem.enableBlend();
                            RenderSystem.defaultBlendFunc();
                            float[] fs = clientLevel.effects().getSunriseColor(clientLevel.getTimeOfDay(partialTick), partialTick);
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
                                s = Mth.sin(clientLevel.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
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
                            s = 1.0F - clientLevel.getRainLevel(partialTick);
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
                            matrices.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
                            matrices.mulPose(Vector3f.XP.rotationDegrees(clientLevel.getTimeOfDay(partialTick) * 360.0F));
                            Matrix4f matrix4f3 = matrices.last().pose();
                            t = 30.0F;
                            RenderSystem.setShader(GameRenderer::getPositionTexShader);
                            RenderSystem.setShaderTexture(0, SUN_LOCATION);
                            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                            bufferBuilder.vertex(matrix4f3, -t, 100.0F, -t).uv(0.0F, 0.0F).endVertex();
                            bufferBuilder.vertex(matrix4f3, t, 100.0F, -t).uv(1.0F, 0.0F).endVertex();
                            bufferBuilder.vertex(matrix4f3, t, 100.0F, t).uv(1.0F, 1.0F).endVertex();
                            bufferBuilder.vertex(matrix4f3, -t, 100.0F, t).uv(0.0F, 1.0F).endVertex();
                            BufferUploader.drawWithShader(bufferBuilder.end());
                            t = 20.0F;
                            RenderSystem.setShaderTexture(0, MOON_LOCATION);
                            int u = clientLevel.getMoonPhase();
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
                            BufferUploader.drawWithShader(bufferBuilder.end());
                            RenderSystem.disableTexture();
                            float ab = clientLevel.getStarBrightness(partialTick) * s;
                            if (ab > 0.0F) {
                                RenderSystem.setShaderColor(ab, ab, ab, ab);
                                this.starBuffer.drawWithShader(matrices.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
                            }

                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                            RenderSystem.disableBlend();
                            matrices.popPose();
                            RenderSystem.disableTexture();
                            RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                            double d = minecraft.player.getEyePosition(partialTick).y - clientLevel.getLevelData().getHorizonHeight(clientLevel);
                            if (d < 0.0D) {
                                matrices.pushPose();
                                matrices.translate(0.0D, 12.0D, 0.0D);
                                this.darkBuffer.drawWithShader(matrices.last().pose(), projectionMatrix, shader);
                                matrices.popPose();
                            }

                            if (clientLevel.effects().hasGround()) {
                                RenderSystem.setShaderColor(g * 0.2F + 0.04F, h * 0.2F + 0.04F, i * 0.6F + 0.1F, 1.0F);
                            } else {
                                RenderSystem.setShaderColor(g, h, i, 1.0F);
                            }

                            RenderSystem.enableTexture();
                            RenderSystem.depthMask(true);
                        }
                    }
                    if (((AstralBodyModifier) clientLevel.effects()).hasFullyCustomSky()) {
                        ((AstralBodyModifier) clientLevel.effects()).render(matrices, partialTick, minecraft, clientLevel);
                    }
                }
            }
        });
//        AstralModifierCallback.EVENT.register((matrixStack, f, client, world) -> {
//            if(BuiltinRegistries.BIOME.getKey(world.getBiomeAccess().getBiome(client.player.getBlockPos())).get() == BiomeKeys.BADLANDS) {
//                RenderSystem.disableAlphaTest();
//                RenderSystem.enableBlend();
//                RenderSystem.defaultBlendFunc();
//                RenderSystem.depthMask(false);
//                client.getTextureManager().bindTexture(new Identifier("textures/environment/end_sky.png"));
//                Tessellator tessellator = Tessellator.getInstance();
//                BufferBuilder bufferBuilder = tessellator.getBuffer();
//
//                for(int i = 0; i < 6; ++i) {
//                    matrixStack.push();
//                    if (i == 1) {
//                        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
//                    }
//
//                    if (i == 2) {
//                        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
//                    }
//
//                    if (i == 3) {
//                        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
//                    }
//
//                    if (i == 4) {
//                        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
//                    }
//
//                    if (i == 5) {
//                        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
//                    }
//
//                    Matrix4f matrix4f = matrixStack.peek().getModel();
//                    bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
//                    bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(40, 40, 40, 255).next();
//                    bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 16.0F).color(40, 40, 40, 255).next();
//                    bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(16.0F, 16.0F).color(40, 40, 40, 255).next();
//                    bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(16.0F, 0.0F).color(40, 40, 40, 255).next();
//                    tessellator.draw();
//                    matrixStack.pop();
//                }
//
//                RenderSystem.depthMask(true);
//                RenderSystem.enableTexture();
//                RenderSystem.disableBlend();
//                RenderSystem.enableAlphaTest();
//                return ActionResult.SUCCESS;
//            }
//            return ActionResult.PASS;
//        });
    }

}
