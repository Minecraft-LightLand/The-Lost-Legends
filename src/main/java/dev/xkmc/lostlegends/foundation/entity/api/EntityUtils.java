package dev.xkmc.lostlegends.foundation.entity.api;

import com.mojang.serialization.Codec;
import dev.xkmc.l2core.base.entity.SyncedData;
import net.minecraft.network.syncher.EntityDataSerializers;

public class EntityUtils {

	public static final SyncedData.Serializer<Float> FLOAT = new SyncedData.Simple<>(EntityDataSerializers.FLOAT, Codec.FLOAT);

}
