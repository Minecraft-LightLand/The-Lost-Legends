package dev.xkmc.lostlegends.init;

import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import dev.xkmc.lostlegends.modules.deco.LLDecoBlocks;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LostLegends.MODID)
@EventBusSubscriber(modid = LostLegends.MODID, bus = EventBusSubscriber.Bus.MOD)
public class LostLegends {

	public static final String MODID = "lostlegends";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			MODID, 1
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	private static final List<LLModuleBase> MODULES = new ArrayList<>();

	public LostLegends(IEventBus bus) {
		MODULES.add(new DeepNether());
		MODULES.add(new LLDecoBlocks());
		AttackEventHandler.register(2583, new LLAttackListener());
	}

	private static void initHandlers() {
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			LostLegends.initHandlers();
			for (var e : MODULES)
				e.commonInit();
		});
	}

	@SubscribeEvent
	public static void onAttribute(EntityAttributeModificationEvent event) {
	}

	@SubscribeEvent
	public static void onDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
	}

	@SubscribeEvent
	public static void registerCap(RegisterCapabilitiesEvent event) {
	}


	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void gatherDataInit(GatherDataEvent event) {
		initHandlers();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		for (var e : MODULES)
			e.gatherData(event);
		new LLDamageTypes(REGISTRATE).generate();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void gatherDataLate(GatherDataEvent event) {
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
