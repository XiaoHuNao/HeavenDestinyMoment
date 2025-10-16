package com.xiaohunao.heaven_destiny_moment.common.function;

import net.minecraft.world.level.Level;


@FunctionalInterface
public interface MomentProbabilityFunction {
    double getProbability(Level level);
}
