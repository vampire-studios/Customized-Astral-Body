package io.github.vampirestudios.cab.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.vampirestudios.cab.api.AstralBodyModifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public class AstralRendering {

    public static void renderCustomAstralBody(MatrixStack matrixStack, MinecraftClient client, SkyProperties dimension, ClientWorld world, VertexBuffer lightSkyBuffer,
                                              VertexBuffer darkSkyBuffer, VertexBuffer starsBuffer, VertexFormat skyVertexFormat, float f, TextureManager textureManager) {
        RenderSystem.disableTexture();
        Vec3d vec3d = world.method_23777(client.gameRenderer.getCamera().getBlockPos(), f);
        float g = (float)vec3d.x;
        float h = (float)vec3d.y;
        float i = (float)vec3d.z;
        BackgroundRenderer.setFogBlack();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.depthMask(false);
        RenderSystem.enableFog();
        RenderSystem.color3f(g, h, i);
        lightSkyBuffer.bind();
        skyVertexFormat.startDrawing(0L);
        lightSkyBuffer.draw(matrixStack.peek().getModel(), 7);
        VertexBuffer.unbind();
        skyVertexFormat.endDrawing();
        RenderSystem.disableFog();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] fs = world.getSkyProperties().getSkyColor(world.getSkyAngleRadians(f), f);
        float s;
        float t;
        int o;
        float q;
        float r;
        if (fs != null) {
            RenderSystem.disableTexture();
            RenderSystem.shadeModel(7425);
            matrixStack.push();
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
            s = MathHelper.sin(world.getSkyAngleRadians(f)) < 0.0F ? 180.0F : 0.0F;
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(s));
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
            float k = fs[0];
            t = fs[1];
            float m = fs[2];
            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferBuilder.begin(6, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(k, t, m, fs[3]).next();

            for(o = 0; o <= 16; ++o) {
                float p = (float)o * 6.2831855F / 16.0F;
                q = MathHelper.sin(p);
                r = MathHelper.cos(p);
                bufferBuilder.vertex(matrix4f, q * 120.0F, r * 120.0F, -r * 40.0F * fs[3]).color(fs[0], fs[1], fs[2], 0.0F).next();
            }

            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            matrixStack.pop();
            RenderSystem.shadeModel(7424);
        }

        RenderSystem.enableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        matrixStack.push();
        s = 1.0F - world.getRainGradient(f);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, s);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(world.getSkyAngleRadians(f) * 360.0F));
        Matrix4f matrix4f2 = matrixStack.peek().getModel();
        t = ((AstralBodyModifier)dimension).getSunSize();
        Vector3f sunTint = ((AstralBodyModifier)dimension).getSunTint();
        textureManager.bindTexture(((AstralBodyModifier)dimension).getSunTexture());
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_TEXTURE);
        bufferBuilder.vertex(matrix4f2, -t, 100.0F, -t).color(sunTint.getX(), sunTint.getY(), sunTint.getZ(), 1.0F).texture(0.0F, 0.0F).next();
        bufferBuilder.vertex(matrix4f2, t, 100.0F, -t).color(sunTint.getX(), sunTint.getY(), sunTint.getZ(), 1.0F).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(matrix4f2, t, 100.0F, t).color(sunTint.getX(), sunTint.getY(), sunTint.getZ(), 1.0F).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(matrix4f2, -t, 100.0F, t).color(sunTint.getX(), sunTint.getY(), sunTint.getZ(), 1.0F).texture(0.0F, 1.0F).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        t = ((AstralBodyModifier)dimension).getMoonSize();
        Vector3f vector3f2 = ((AstralBodyModifier)dimension).getMoonTint();
        textureManager.bindTexture(((AstralBodyModifier)dimension).getMoonTexture());
        int u = world.getMoonPhase();
        o = u % 4;
        int w = u / 4 % 2;
        q = (float) o / 4.0F;
        r = (float) w / 2.0F;
        float z = (float)(o + 1) / 4.0F;
        float aa = (float)(w + 1) / 2.0F;
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_TEXTURE);
        bufferBuilder.vertex(matrix4f2, -t, -100.0F, t).color(vector3f2.getX(), vector3f2.getY(), vector3f2.getZ(), 1.0F).texture(z, aa).next();
        bufferBuilder.vertex(matrix4f2, t, -100.0F, t).color(vector3f2.getX(), vector3f2.getY(), vector3f2.getZ(), 1.0F).texture(q, aa).next();
        bufferBuilder.vertex(matrix4f2, t, -100.0F, -t).color(vector3f2.getX(), vector3f2.getY(), vector3f2.getZ(), 1.0F).texture(q, r).next();
        bufferBuilder.vertex(matrix4f2, -t, -100.0F, -t).color(vector3f2.getX(), vector3f2.getY(), vector3f2.getZ(), 1.0F).texture(z, r).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.disableTexture();
        float ab = world.method_23787(f) * s;
        if (ab > 0.0F) {
            RenderSystem.color4f(ab, ab, ab, ab);
            starsBuffer.bind();
            skyVertexFormat.startDrawing(0L);
            starsBuffer.draw(matrixStack.peek().getModel(), 7);
            VertexBuffer.unbind();
            skyVertexFormat.endDrawing();
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableFog();
        matrixStack.pop();
        RenderSystem.disableTexture();
        RenderSystem.color3f(0.0F, 0.0F, 0.0F);
        double d = client.player.getCameraPosVec(f).y - world.getLevelProperties().getSkyDarknessHeight();
        if (d < 0.0D) {
            matrixStack.push();
            matrixStack.translate(0.0D, 12.0D, 0.0D);
            darkSkyBuffer.bind();
            skyVertexFormat.startDrawing(0L);
            darkSkyBuffer.draw(matrixStack.peek().getModel(), 7);
            VertexBuffer.unbind();
            skyVertexFormat.endDrawing();
            matrixStack.pop();
        }

        if (world.getSkyProperties().isAlternateSkyColor()) {
            RenderSystem.color3f(g * 0.2F + 0.04F, h * 0.2F + 0.04F, i * 0.6F + 0.1F);
        } else {
            RenderSystem.color3f(g, h, i);
        }

        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();
    }

}
