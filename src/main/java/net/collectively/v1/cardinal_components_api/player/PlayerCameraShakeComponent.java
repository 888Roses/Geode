package net.collectively.v1.cardinal_components_api.player;

import net.collectively.v1.cardinal_components_api._internal.GeodeEntityComponentIndex;
import net.collectively.v1.cardinal_components_api.foundation.PlayerEntityComponent;
import net.collectively.v1.core.math;
import net.collectively.v1.core.types.double2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class PlayerCameraShakeComponent extends PlayerEntityComponent implements CommonTickingComponent, AutoSyncedComponent {
    // region Essentials

    public PlayerCameraShakeComponent(PlayerEntity player) {
        super(player);
    }

    @Override
    public ComponentKey<? extends Component> getKey() {
        return GeodeEntityComponentIndex.PLAYER_CAMERA_SHAKE;
    }

    // endregion

    // region Data

    @Override
    public void readData(ReadView readView) {
        currentShake = readView.read("currentShake", ShakeSettings.CODEC).orElse(null);
        currentShakeStartTime = readView.getLong("currentShakeStartTime", 0);
        currentShakeVector = readView.read("currentShakeVector", double2.CODEC).orElse(null);
    }

    @Override
    public void writeData(WriteView writeView) {
        if (currentShake != null) writeView.put("currentShake", ShakeSettings.CODEC, currentShake);
        writeView.putLong("currentShakeStartTime", currentShakeStartTime);
        if (currentShakeVector != null) writeView.put("currentShakeVector", double2.CODEC, currentShakeVector);
    }

    // endregion

    private ShakeSettings currentShake;
    private long currentShakeStartTime;
    private double2 currentShakeVector;

    public long getCurrentShakeStartTime() {
        return currentShakeStartTime;
    }

    public boolean isShaking() {
        if (currentShake == null) return false;
        return world.getTime() <= currentShakeStartTime + currentShake.duration();
    }

    /// Warning! could be outdated. Check [#isShaking()].
    public @Nullable ShakeSettings getCurrentShake() {
        return currentShake;
    }

    public double2 getCurrentShakeVector() {
        return currentShakeVector;
    }

    public void shake(ShakeSettings settings) {
        currentShake = settings;
        currentShakeStartTime = world.getTime();
        sync();
    }

    @Override
    public void tick() {
        if (isShaking()) {
            double progress = 1 - -1 * (currentShakeStartTime - world.getTime()) / (double) currentShake.duration();
            double intensity = currentShake.intensity() * currentShake.easingFunction().get(progress);

            Random random = player.getRandom();
            double angle = math.deg2rad(random.nextFloat() * 360f);
            currentShakeVector = new double2(math.sin(angle), math.cos(angle)).mul(intensity * 0.0001);
        }
    }
}

