package io.github.happyhippo77.nindocraft.mixin;

import com.mojang.authlib.GameProfile;
import io.github.happyhippo77.nindocraft.handsigning.HandSignSequence;
import io.github.happyhippo77.nindocraft.networking.NindoCraftServerPackets;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow public abstract double getAttributeValue(EntityAttribute attribute);

	@Shadow @Final private AttributeContainer attributes;

	@Inject(method = "onKilledBy", at = @At("TAIL"))
	public void onKilledBy(LivingEntity adversary, CallbackInfo ci) {
		if (adversary instanceof ServerPlayerEntity) {
			if (attributes.hasAttribute(EntityAttributes.GENERIC_MAX_HEALTH)) {
				int exp = (int) (getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH) / 2);
				if (attributes.hasAttribute(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
					exp *= getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
				}

				NindoCraftPlayer nindoCraftPlayer = (NindoCraftPlayer) (ServerPlayerEntity) adversary;

				nindoCraftPlayer.addExp(exp);
			}
		}
	}
}
