package com.xiaohunao.heaven_destiny_moment.compat.phase_journey.phase;

import com.mojang.datafixers.util.Pair;
import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.phase_journey.common.phase.PhaseManager;
import com.xiaohunao.phase_journey.common.phase.PhaseType;
import com.xiaohunao.phase_journey.common.util.PhaseUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Map;

public class HDMMomentCreatePhaseManager extends PhaseManager<HDMMomentCreatePhaseContext> {

    public static final HDMMomentCreatePhaseManager MANAGER = new HDMMomentCreatePhaseManager();

    @SubscribeEvent
    public void onMomentCreate(MomentEvent.Create event) {
        for (Map.Entry<PhaseType, Pair<ResourceLocation, HDMMomentCreatePhaseContext>> entry : phaseContexts.entries()) {
            HDMMomentCreatePhaseContext ctx = entry.getValue().getSecond();
            ResourceLocation phase = entry.getValue().getFirst();

            if (PhaseUtils.hadLevelFinishedPhase(phase, event.getMomentInstance().getLevel())) {
                continue;
            }
            if (ctx.disableAll()) {
                event.setCanceled(true);
                return;
            }
            if (ctx.bannedMoments().contains(event.getMomentInstance().getMomentResource())) {
                event.setCanceled(true);
                return;
            }
        }

    }
}
