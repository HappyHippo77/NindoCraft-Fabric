package io.github.happyhippo77.nindocraft.jutsu;

import io.github.happyhippo77.nindocraft.networking.NindoCraftServerPackets;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TestJutsu extends NinjutsuProjectile {
    public TestJutsu(PlayerEntity caster, Vec3d direction, World world, Vec3d pos) {
        super(caster, 200,
                10,
                IntArrayList.of(1, 2, 3, 4),
                15,
                0.1f,
                2,
                direction,
                world,
                pos);
    }

    @Override
    public void serverTick(MinecraftServer server) {
        super.serverTick(server);
        if (world != null && this.range > 0) {
            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, new BlockPos(pos.x, pos.y, pos.z))) {
                NindoCraftServerPackets.sendRenderParticle(player, ParticleTypes.FLAME, (float) pos.x, (float) pos.y, (float) pos.z, 0, 0, 0);
            }
        }
    }
}
