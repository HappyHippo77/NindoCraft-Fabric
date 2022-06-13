package io.github.happyhippo77.nindocraft.jutsu;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileJutsu extends Jutsu {
    public float range;
    public float speed;
    public float damage;
    public Vec3d direction;
    public World world;
    public Vec3d pos;

    public ProjectileJutsu(PlayerEntity caster, int exp, int chakra, IntArrayList handSigns, float range, float speed, float damage, Vec3d direction, World world, Vec3d pos) {
        super(caster, exp, chakra, handSigns);
        this.range = range;
        this.speed = speed;
        this.damage = damage;
        this.direction = direction;
        this.world = world;
        this.pos = pos;
    }

    public void serverTick(MinecraftServer server) {
        if (range > 0) {
            pos = pos.add(speed * direction.x,speed * direction.y,speed * direction.z);
            range -= speed;
        }
    }

    @Override
    public void cast() {
        super.cast();
        ServerTickEvents.START_SERVER_TICK.register(this::serverTick);
    }
}
