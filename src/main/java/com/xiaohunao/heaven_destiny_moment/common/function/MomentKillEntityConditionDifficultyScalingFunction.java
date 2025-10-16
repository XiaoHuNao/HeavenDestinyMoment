package com.xiaohunao.heaven_destiny_moment.common.function;

import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;

@FunctionalInterface
public interface MomentKillEntityConditionDifficultyScalingFunction {
    int scale(int baseValue, MomentInstance momentInstance);
}
