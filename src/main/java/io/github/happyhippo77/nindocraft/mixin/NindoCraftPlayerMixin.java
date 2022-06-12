package io.github.happyhippo77.nindocraft.mixin;

import com.mojang.authlib.GameProfile;
import io.github.happyhippo77.nindocraft.NindoCraft;
import io.github.happyhippo77.nindocraft.handsigning.HandSignSequence;
import io.github.happyhippo77.nindocraft.util.NindoCraftPlayer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.management.ObjectInstance;
import java.util.Random;

@Mixin(ServerPlayerEntity.class)
public abstract class NindoCraftPlayerMixin implements NindoCraftPlayer {

	private HandSignSequence handSignSequence = new HandSignSequence(new Integer[0]);
	private int chakra;
	private int stamina;
	private int staminaScore;
	private int maxStamina;
	private boolean rechargeStamina = true;
	private boolean isHandSigning;

	private int seconds = 0;

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		System.out.println(isHandSigning);

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

		PacketByteBuf buf1 = PacketByteBufs.create();
		buf1.writeInt(this.chakra);
		buf1.writeInt(this.stamina);
		buf1.writeInt(this.staminaScore);
		buf1.writeInt(this.maxStamina);
		ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, new Identifier(NindoCraft.MOD_ID, "player_stats_to_renderer"), buf1);

		PacketByteBuf buf2 = PacketByteBufs.create();
		buf2.writeBoolean(this.isHandSigning);
		ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, new Identifier(NindoCraft.MOD_ID, "handsigns_to_key_handler"), buf2);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(MinecraftServer MinecraftServer, ServerWorld world, GameProfile profile, PlayerPublicKey publicKey, CallbackInfo ci) {
		maxStamina = 100 + 50 * staminaScore;

		ServerPlayNetworking.registerGlobalReceiver(new Identifier(NindoCraft.MOD_ID, "add_handsign_to_player"), (server, player, handler, buf, responseSender) -> {
			System.out.println("add_handsign_to_player Called");
			this.handSignSequence.addHandSign(buf.readInt());
		});
		ServerPlayNetworking.registerGlobalReceiver(new Identifier(NindoCraft.MOD_ID, "start_handsign_sequence"), (server, player, handler, buf, responseSender) -> {
			System.out.println("start_handsign_sequence Called");
			this.isHandSigning = true;
			server.execute(() -> {
				this.setHandSignSequence(new HandSignSequence(new Integer[0]));
			});
		});
		ServerPlayNetworking.registerGlobalReceiver(new Identifier(NindoCraft.MOD_ID, "end_handsign_sequence"), (server, player, handler, buf, responseSender) -> {
			System.out.println("end_handsign_sequence Called");
			this.isHandSigning = false;
			server.execute(() -> {
				this.setHandSignSequence(new HandSignSequence(new Integer[0]));
			});
		});
		ServerPlayNetworking.registerGlobalReceiver(new Identifier(NindoCraft.MOD_ID, "cast_jutsu_from_client"), (server, player, handler, buf, responseSender) -> {
			System.out.println("cast_jutsu_from_client Called");
			server.execute(() ->{
				this.handSignSequence.cast((PlayerEntity) (Object) this);
			});
		});
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
		System.out.println("Custom NBT saved");
		nbt.putInt("chakra", this.chakra);
		nbt.putInt("stamina", this.stamina);
	}
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound nbt, CallbackInfo ci) {
		System.out.println("Custom NBT loaded");
		this.chakra = nbt.getInt("chakra");
		this.stamina = nbt.getInt("stamina");
		System.out.println(this.stamina);
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
