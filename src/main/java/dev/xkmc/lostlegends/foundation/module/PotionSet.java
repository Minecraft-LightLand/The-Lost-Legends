package dev.xkmc.lostlegends.foundation.module;

import dev.xkmc.l2core.init.reg.registrate.PotionBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class PotionSet {

	public static PotionSet single(PotionBuilder builder, String id, Holder<MobEffect> effect, Holder<Potion> base, ItemLike mat, int dur, int amp) {
		return new PotionSet(builder.regPotion(id, id, effect, base, mat, dur, amp));
	}

	public static PotionSet potion2(PotionBuilder builder, String id, Holder<MobEffect> effect, Holder<Potion> base, ItemLike mat, int dur, int durLong, int amp) {
		var p1 = builder.regPotion(id, id, effect, base, mat, dur, amp);
		var p2 = builder.regPotion("long_" + id, id, effect, p1, Items.REDSTONE, durLong, amp);
		return new PotionSet(p1, p2);
	}

	public static PotionSet potion2(PotionBuilder builder, String id, Holder<MobEffect> effect, PotionSet base, ItemLike mat, int dur, int durLong, int amp) {
		var p1 = builder.regPotion(id, id, effect, base.base, mat, dur, amp);
		var p2 = builder.regPotion("long_" + id, id, effect, p1, Items.REDSTONE, durLong, amp);
		if (base.longPotion != null) {
			builder.addMix(base.longPotion, mat, p2);
		}
		return new PotionSet(p1, p2);
	}

	public static PotionSet potion3(PotionBuilder builder, String id, Holder<MobEffect> effect, Holder<Potion> base, ItemLike mat, int dur, int durLong, int durStrong, int amp, int ampStrong) {
		var p1 = builder.regPotion(id, id, effect, base, mat, dur, amp);
		var p2 = builder.regPotion("long_" + id, id, effect, p1, Items.REDSTONE, durLong, amp);
		var p3 = builder.regPotion("strong_" + id, id, effect, p1, Items.GLOWSTONE_DUST, durStrong, ampStrong);
		return new PotionSet(p1, p2, p3);
	}

	public static PotionSet potion3(PotionBuilder builder, String id, Holder<MobEffect> effect, PotionSet base, ItemLike mat, int dur, int durLong, int durStrong, int amp, int ampStrong) {
		var p1 = builder.regPotion(id, id, effect, base.base, mat, dur, amp);
		var p2 = builder.regPotion("long_" + id, id, effect, p1, Items.REDSTONE, durLong, amp);
		var p3 = builder.regPotion("strong_" + id, id, effect, p1, Items.GLOWSTONE_DUST, durStrong, ampStrong);
		if (base.longPotion != null) {
			builder.addMix(base.longPotion, mat, p2);
		}
		if (base.strongPotion != null) {
			builder.addMix(base.strongPotion, mat, p3);
		}
		return new PotionSet(p1, p2, p3);
	}

	public final Holder<Potion> base;
	@Nullable
	public final Holder<Potion> longPotion, strongPotion;

	private PotionSet(Holder<Potion> base, Holder<Potion> longPotion, Holder<Potion> strongPotion) {
		this.base = base;
		this.longPotion = longPotion;
		this.strongPotion = strongPotion;
	}

	private PotionSet(Holder<Potion> base, Holder<Potion> longPotion) {
		this.base = base;
		this.longPotion = longPotion;
		this.strongPotion = null;
	}

	private PotionSet(Holder<Potion> base) {
		this.base = base;
		this.longPotion = null;
		this.strongPotion = null;
	}

}
