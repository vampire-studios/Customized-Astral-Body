package io.github.vampirestudios.cab;

import net.fabricmc.api.ModInitializer;

public class CustomAstralBody implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("You're running Custom Astral Body v2.0.0-1.16.3");

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
