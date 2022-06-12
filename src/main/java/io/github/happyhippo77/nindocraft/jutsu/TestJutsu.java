package io.github.happyhippo77.nindocraft.jutsu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;

public class TestJutsu extends ProjectileJutsu {
    public TestJutsu(PlayerEntity caster, Vec3d direction, Vec3d pos) {
        super(caster, 15,
                10,
                new ArrayList<Integer>(Arrays.asList(1,2,3,4)),
                15,
                0.1f,
                2,
                direction,
                pos);
    }

    @Override
    public void clientTick(MinecraftClient client) {
        super.clientTick(client);
        if (client.world != null && this.range > 0) {
            client.world.addParticle(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }
}
