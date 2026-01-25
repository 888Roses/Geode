package net.collectively.v1.cardinal_components_api.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.collectively.v1.core.StandardEasingFunction;
import net.collectively.v1.mc.serialization.Writable;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.NotNull;

public record ShakeSettings(double intensity, long duration, @NotNull StandardEasingFunction easingFunction) implements Writable {
    public static final Codec<ShakeSettings> CODEC = RecordCodecBuilder.<ShakeSettings>mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("intensity").forGetter(ShakeSettings::intensity),
            Codec.LONG.fieldOf("duration").forGetter(ShakeSettings::duration),
            StandardEasingFunction.CODEC.fieldOf("easingFunction").forGetter(ShakeSettings::easingFunction)
    ).apply(instance, ShakeSettings::new)).codec();

    @Override
    public void write(WriteView view) {
        view.putDouble("intensity", intensity);
        view.putLong("duration", duration);
        view.put("easing_function", StandardEasingFunction.CODEC, easingFunction);
    }
}
