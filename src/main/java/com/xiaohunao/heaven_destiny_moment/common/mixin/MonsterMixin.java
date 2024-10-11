package com.xiaohunao.heaven_destiny_moment.common.mixin;


import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import com.xiaohunao.heaven_destiny_moment.common.context.MobSpawnSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Monster.class)
public class MonsterMixin {
    @Inject(method = "isDarkEnoughToSpawn", at = @At("RETURN"), cancellable = true)
    private static void isDarkEnoughToSpawn(ServerLevelAccessor serverLevelAccessor, BlockPos p_219011_, RandomSource p_219012_, CallbackInfoReturnable<Boolean> cir) {
        MomentCap.getCap(serverLevelAccessor.getLevel()).ifPresent(momentCap -> {
            MomentInstance momentInstance = momentCap.getOnlyModifiedMobSpawnSettingsMoment();
            if (momentInstance != null){
                MobSpawnSettingsContext mobSpawnSettingsContext = momentInstance.getMoment().momentDataContext().getMobSpawnSettingsContext();
                if (mobSpawnSettingsContext.ignoreLightLevel()){
                    cir.setReturnValue(true);
                }
            }
        });
    }
}
