package io.github.happyhippo77.nindocraft.jutsu;

import io.github.happyhippo77.nindocraft.NindoCraft;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class ProjectileJutsu extends Jutsu {
    public float range;
    public float speed;
    public float damage;
    public Vec3d direction;
    public Vec3d pos;

    public ProjectileJutsu(PlayerEntity caster, int exp, int chakra, ArrayList<Integer> handSigns, float range, float speed, float damage, Vec3d direction, Vec3d pos) {
        super(caster, exp, chakra, handSigns);
        this.range = range;
        this.speed = speed;
        this.damage = damage;
        this.direction = direction;
        this.pos = pos;
    }

    public void serverTick(MinecraftServer server) {
        if (range > 0) {
            pos = pos.add(speed * direction.x,speed * direction.y,speed * direction.z);
            range -= speed;
        }
    }

    @Override
    public boolean cast() {
        ServerTickEvents.START_SERVER_TICK.register(this::serverTick);
        ClientTickEvents.START_CLIENT_TICK.register(this::clientTick);
        return true;
    }

    public void clientTick(MinecraftClient client) {
    }
}
