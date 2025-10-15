package com.xiaohunao.heaven_destiny_moment.compat.phase_journey;

import com.xiaohunao.heaven_destiny_moment.compat.phase_journey.event.PhaseTriggerTriggerSubscriber;
import com.xiaohunao.heaven_destiny_moment.compat.phase_journey.init.PJConditions;
import com.xiaohunao.heaven_destiny_moment.compat.phase_journey.init.PJTriggers;
import com.xiaohunao.heaven_destiny_moment.compat.phase_journey.phase.HDMMomentCreatePhaseContext;
import com.xiaohunao.heaven_destiny_moment.compat.phase_journey.phase.HDMMomentCreatePhaseManager;
import com.xiaohunao.phase_journey.common.init.PJRegistries;
import com.xiaohunao.phase_journey.common.phase.PhaseContextType;
import com.xiaohunao.xhn_lib.api.register.holder.FlexibleHolder;
import com.xiaohunao.xhn_lib.api.register.register.FlexibleRegister;
import com.xiaohunao.xhn_lib.api.register.register.MapCodecFlexibleRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;


public class PJHelper {

    public static final String MODID = "heaven_destiny_moment";

    public static final FlexibleRegister<PhaseContextType<?>> CONDITION_CODEC = MapCodecFlexibleRegister.create(PJRegistries.PHASE_CONTEXT_TYPE, MODID);

    public static final FlexibleHolder<PhaseContextType<?>, PhaseContextType<HDMMomentCreatePhaseContext>> MOMENT_CREATE = CONDITION_CODEC.registerStatic("moment_create", () -> new PhaseContextType<>(HDMMomentCreatePhaseContext.class, HDMMomentCreatePhaseManager.MANAGER));

    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(HDMMomentCreatePhaseManager.MANAGER);
        CONDITION_CODEC.register(modEventBus);

        PJConditions.CONDITION_CODEC.register(modEventBus);
        PJTriggers.TRIGGER_CODEC.register(modEventBus);
        NeoForge.EVENT_BUS.register(new PhaseTriggerTriggerSubscriber());
    }
}
