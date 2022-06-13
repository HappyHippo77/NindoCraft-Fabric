package io.github.happyhippo77.nindocraft.mixin;

import com.mojang.authlib.GameProfile;
import io.github.happyhippo77.nindocraft.handsigning.HandSignSequence;
import io.github.happyhippo77.nindocraft.networking.NindoCraftServerPackets;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class NindoCraftPlayerMixin implements NindoCraftPlayer {

	@Shadow @Final public ServerPlayerInteractionManager interactionManager;
	private HandSignSequence handSignSequence = new HandSignSequence(new int[0]);
	private int chakra;
	private int stamina;
	private int staminaScore;
	private int maxStamina;
	private boolean rechargeStamina = true;
	private boolean isHandSigning;

	private boolean handledFirstJoin = false;

	private int seconds = 0;

	private double defaultWalkingSpeed = 0;

	private final ServerPlayerEntity serverPlayerEntity = ((ServerPlayerEntity) (Object) this);

	@Inject(method = "onDeath", at = @At("TAIL"))
	public void onDeath(DamageSource damageSource, CallbackInfo ci) {
		this.stamina = this.maxStamina;
		this.chakra = 0;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		if (this.seconds == 20) {
			this.seconds = 0;
			if (rechargeStamina) {
				if (this.stamina < this.maxStamina) {
					this.stamina += 1;
				}
			}
		}
		else {
			this.seconds += 1;
		}

		if (this.stamina == 0) {
			if (serverPlayerEntity.interactionManager.getGameMode() != GameMode.CREATIVE && serverPlayerEntity.interactionManager.getGameMode() != GameMode.SPECTATOR) {
				serverPlayerEntity.kill();
			}
		}
		if (this.stamina < this.maxStamina / 2 / 2 / 2) {
			serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(defaultWalkingSpeed / 4);
			serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100));
			serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100));
			serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 25));
		}
		if (this.stamina < this.maxStamina / 2 / 2) {
			serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(defaultWalkingSpeed / 3);
			serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100));
		}
		else if (this.stamina < this.maxStamina / 2) {
			serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(defaultWalkingSpeed / 2);
		}
		else {
			serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(defaultWalkingSpeed);
		}

		NindoCraftServerPackets.sendRenderPlayerStatsPacket(this);
	}

	public void firstJoin() {
		this.stamina = this.maxStamina;
		this.handledFirstJoin = true;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(MinecraftServer MinecraftServer, ServerWorld world, GameProfile profile, PlayerPublicKey publicKey, CallbackInfo ci) {
		this.maxStamina = 100 + 50 * this.staminaScore;
		this.defaultWalkingSpeed = serverPlayerEntity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);

		if (!this.handledFirstJoin) {
			firstJoin();
		}

	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putInt("chakra", this.chakra);
		nbt.putInt("stamina", this.stamina);
		nbt.putBoolean("handledFirstJoin", this.handledFirstJoin);
	}
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound nbt, CallbackInfo ci) {
		this.chakra = nbt.getInt("chakra");
		this.stamina = nbt.getInt("stamina");
		this.handledFirstJoin = nbt.getBoolean("handledFirstJoin");
	}

	@Override
	public boolean isHandSigning() {
		return this.isHandSigning;
	}

	@Override
	public void setHandSigning(boolean bool) {
		this.isHandSigning = bool;
	}

	@Override
	public HandSignSequence getHandSignSequence() {
		return this.handSignSequence;
	}

	@Override
	public void setHandSignSequence(HandSignSequence sequence) {
		this.handSignSequence = sequence;
	}

	@Override
	public int getChakra() {
		return this.chakra;
	}

	@Override
	public void setChakra(int i) {
		this.chakra = i;
	}

	@Override
	public int getStamina() {
		return this.stamina;
	}

	@Override
	public void setStamina(int i) {
		this.stamina = i;
	}

	@Override
	public int getStaminaScore() {
		return staminaScore;
	}

	@Override
	public void setStaminaScore(int i) {
		this.staminaScore = i;
		this.maxStamina = 100 + 50 * staminaScore;
	}

	@Override
	public int getMaxStamina() {
		return maxStamina;
	}

	@Override
	public boolean doRechargeStamina() {
		return rechargeStamina;
	}

	@Override
	public void setRechargeStamina(boolean bool) {
		this.rechargeStamina = bool;
	}
}
