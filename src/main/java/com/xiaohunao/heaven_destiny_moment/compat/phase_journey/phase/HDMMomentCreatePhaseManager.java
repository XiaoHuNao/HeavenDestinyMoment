package com.xiaohunao.heaven_destiny_moment.compat.phase_journey.phase;

import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import org.confluence.phase_journey.common.phase.PhaseManager;
import org.confluence.phase_journey.common.util.PhaseUtils;

import java.util.Collection;
import java.util.Map;

public class HDMMomentCreatePhaseManager extends PhaseManager<HDMMomentCreatePhaseContext> {

    public static final HDMMomentCreatePhaseManager MANAGER = new HDMMomentCreatePhaseManager();

    @SubscribeEvent
    public void onMomentCreate(MomentEvent.Create event) {
        for (Map.Entry<ResourceLocation, Collection<HDMMomentCreatePhaseContext>> entry : phaseContexts.asMap().entrySet()) {
            if (PhaseUtils.hadLevelFinishedPhase(entry.getKey(), event.getMomentInstance().getLevel())) {
                continue;
            }
            for (HDMMomentCreatePhaseContext ctx : entry.getValue()) {
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
}
