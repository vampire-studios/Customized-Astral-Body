package io.github.vampirestudios.cab.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.Vector3f;

public interface AstralBodyModifier {

    @Environment(EnvType.CLIENT)
    default float getSunSize() {
        return 30.0F;
    }

    @Environment(EnvType.CLIENT)
    default float getMoonSize() {
        return 20.0F;
    }

    @Environment(EnvType.CLIENT)
    default Vector3f getSunTint() {
        return new Vector3f(1.0F, 1.0F, 1.0F);
    }

    @Environment(EnvType.CLIENT)
    default Vector3f getMoonTint() {
        return new Vector3f(1.0F, 1.0F, 1.0F);
    }

}
