package io.github.happyhippo77.nindocraft.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.happyhippo77.nindocraft.NindoCraft;
import io.github.happyhippo77.nindocraft.handsigning.HandSignSequence;
import io.github.happyhippo77.nindocraft.networking.NindoCraftServerPackets;
import io.github.happyhippo77.nindocraft.util.JutsuEntry;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class NindoCraftPlayerMixin implements NindoCraftPlayer {
	@Shadow public abstract void enterCombat();

	private HandSignSequence handSignSequence = new HandSignSequence(new int[0]);
	private ArrayList<String> knownJutsu = new ArrayList<>();
	private int chakra;
	private int stamina;
	private int maxStamina;
	private boolean rechargeStamina = true;
	private int exp;
	private int level;
	private int nextLevel;
	private int statPoints;
	private int staminaScore;
	private int ninjutsuScore;
	private int genjutsuScore;
	private int taijutsuScore;
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

	@Inject(method = "onSpawn", at = @At("TAIL"))
	public void onSpawn(CallbackInfo ci) {
		updateMaxStamina();
		updateNextLevel();

		if (!this.handledFirstJoin) {
			firstJoin();
		}
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(MinecraftServer MinecraftServer, ServerWorld world, GameProfile profile, PlayerPublicKey publicKey, CallbackInfo ci) {
		this.defaultWalkingSpeed = serverPlayerEntity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
		NbtCompound knownJutsuNbt = new NbtCompound();
		int i = 0;
		for (String str : this.knownJutsu) {
			knownJutsuNbt.putString(String.valueOf(i), str);
		}
		nbt.put("known_jutsu", knownJutsuNbt);
		nbt.putInt("chakra", this.chakra);
		nbt.putInt("stamina", this.stamina);
		nbt.putInt("nindocraft_experience", this.exp);
		nbt.putInt("nindocraft_level", this.level);
		nbt.putInt("stat_points", this.statPoints);
		nbt.putInt("stamina_score", this.staminaScore);
		nbt.putInt("ninjutsu_score", this.ninjutsuScore);
		nbt.putInt("genjutsu_score", this.genjutsuScore);
		nbt.putInt("taijutsu_score", this.taijutsuScore);
		nbt.putBoolean("handledFirstJoin", this.handledFirstJoin);
	}
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound nbt, CallbackInfo ci) {
		NbtCompound nbtCompound = (NbtCompound) nbt.get("known_jutsu");
		ArrayList<String> arrayList = new ArrayList<>();
		if (!nbtCompound.isEmpty()) {
			for (String key : nbtCompound.getKeys()) {
				arrayList.add(nbtCompound.getString(key));
			}
			knownJutsu = arrayList;
		}
		System.out.println(knownJutsu);

		this.chakra = nbt.getInt("chakra");
		this.stamina = nbt.getInt("stamina");
		this.exp = nbt.getInt("nindocraft_experience");
		this.level = nbt.getInt("nindocraft_level");
		this.statPoints = nbt.getInt("stat_points");
		this.staminaScore = nbt.getInt("stamina_score");
		this.ninjutsuScore = nbt.getInt("ninjutsu_score");
		this.genjutsuScore = nbt.getInt("genjutsu_score");
		this.taijutsuScore = nbt.getInt("taijutsu_score");
		this.handledFirstJoin = nbt.getBoolean("handledFirstJoin");
	}

	private void updateNextLevel() {
		// Leveling up to level 1 will cost 100 exp,
		// for every level gained after, it will cost an additional 20
		this.nextLevel = 100 + (20 * (this.level + 1));
	}

	private void updateMaxStamina() {
		// With a stamina score of 0, you'll have 100 max stamina,
		// at each stamina upgrade after, you'll gain 20 max stamina
		this.maxStamina = 100 + (20 * staminaScore);
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
	public ArrayList<String> getKnownJutsu() {
		return this.knownJutsu;
	}

	@Override
	public void setKnownJutsu(ArrayList<String> arrayList) {
		this.knownJutsu = arrayList;
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
	public int getMaxStamina() {
		return maxStamina;
	}

	@Override
	public boolean canRechargeStamina() {
		return rechargeStamina;
	}

	@Override
	public void setRechargeStamina(boolean bool) {
		this.rechargeStamina = bool;
	}

	@Override
	public int getExp() {
		return this.exp;
	}

	@Override
	public void addExp(int i) {
		this.exp += i;
		checkExp();
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getNextLevel() {
		return this.nextLevel;
	}

	@Override
	public int getStatPoints() {
		return this.statPoints;
	}
	@Override
	public void setStatPoints(int i) {
		this.statPoints = i;
	}

	@Override
	public void checkExp() {
		if (this.exp >= this.nextLevel) {
			this.exp -= this.nextLevel;
			this.level += 1;
			this.statPoints += 2;
			updateNextLevel();
			NindoCraftServerPackets.sendLevelUp((ServerPlayerEntity) (Object) this);
			checkExp();
		}
	}

	public void checkJutsu(String type) {
		for (JutsuEntry entry : NindoCraft.jutsuIndex) {
			if (this.level == entry.getLevel()) {
				if (Objects.equals(type, entry.getType())) {
					this.knownJutsu.add(entry.getName());
				}
			}
		}
	}

	@Override
	public int getStaminaScore() {
		return staminaScore;
	}

	@Override
	public void setStaminaScore(int i) {
		this.staminaScore = i;
		updateMaxStamina();
	}

	@Override
	public int getNinjutsuScore() {
		return this.ninjutsuScore;
	}

	@Override
	public void setNinjutsuScore(int i) {
		checkJutsu("ninjutsu");
		this.ninjutsuScore = i;
	}

	@Override
	public int getGenjutsuScore() {
		return this.genjutsuScore;
	}

	@Override
	public void setGenjutsuScore(int i) {
		checkJutsu("genjutsu");
		this.genjutsuScore = i;
	}

	@Override
	public int getTaijutsuScore() {
		return this.taijutsuScore;
	}

	@Override
	public void setTaijutsuScore(int i) {
		checkJutsu("taijutsu");
		this.taijutsuScore = i;
	}
}
