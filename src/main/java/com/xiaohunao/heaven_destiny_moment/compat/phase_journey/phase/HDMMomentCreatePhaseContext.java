package com.xiaohunao.heaven_destiny_moment.compat.phase_journey.phase;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.confluence.phase_journey.common.phase.PhaseContext;

import java.util.List;

public class HDMMomentCreatePhaseContext extends PhaseContext {

    public static final MapCodec<HDMMomentCreatePhaseContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("phase").forGetter(HDMMomentCreatePhaseContext::getPhase),
            Codec.BOOL.fieldOf("disable_all").forGetter(HDMMomentCreatePhaseContext::disableAll),
            ResourceLocation.CODEC.listOf().fieldOf("banned_moments").forGetter(HDMMomentCreatePhaseContext::bannedMoments)
    ).apply(instance, HDMMomentCreatePhaseContext::new));

    private final boolean disableAll;
    private final List<ResourceLocation> bannedMoments;

    public HDMMomentCreatePhaseContext(ResourceLocation phase, boolean disableAll, List<ResourceLocation> bannedMoments) {
        super(phase);
        this.disableAll = disableAll;
        this.bannedMoments = bannedMoments;
    }

    public boolean disableAll() {
        return disableAll;
    }

    public List<ResourceLocation> bannedMoments() {
        return bannedMoments;
    }
}
