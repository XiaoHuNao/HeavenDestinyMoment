package com.xiaohunao.heaven_destiny_moment;

import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.AmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.ConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.EntityInfoContext;
import com.xiaohunao.heaven_destiny_moment.common.context.predicate.PredicateContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.RewardContext;
import com.xiaohunao.heaven_destiny_moment.common.init.ModItems;
import com.xiaohunao.heaven_destiny_moment.common.init.ModMoments;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.network.ModMessages;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

@Mod(HeavenDestinyMoment.MODID)
public class HeavenDestinyMoment {
    public static final String MODID = "heaven_destiny_moment";
    public static final Logger LOGGER = LogUtils.getLogger();
    public HeavenDestinyMoment() {
        loadClasses();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        ModItems.ITEMS.register(modEventBus);

        modEventBus.addListener(this::onDataPackRegistryNewRegistry);
        modEventBus.addListener(this::onFMLCommonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
    public static String asDescriptionId(String path) {
        return MODID + "." + path;
    }

    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
        });
    }

    @SubscribeEvent
    public void onDataPackRegistryNewRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ModMoments.MOMENT_KEY, Moment.CODEC,Moment.CODEC);
    }

    public static void loadClasses() {
        ModMoments.init();
        AmountContext.register();
        ConditionContext.register();
        EntityInfoContext.register();
        PredicateContext.register();
        RewardContext.register();
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}