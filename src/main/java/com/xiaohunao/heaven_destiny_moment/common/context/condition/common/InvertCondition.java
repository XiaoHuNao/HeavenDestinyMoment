package com.xiaohunao.heaven_destiny_moment.common.context.condition.common;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.automation.AutomationContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.ICondition;

public record InvertCondition(ICondition condition) implements ICondition{
    public static final MapCodec<InvertCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ICondition.CODEC.fieldOf("invert").forGetter(InvertCondition::condition)
    ).apply(instance, InvertCondition::new));

    public static InvertCondition of(ICondition condition) {
        return new InvertCondition(condition);
    }

    @Override
    public boolean matches(AutomationContext automationContext) {
        return !condition.matches(automationContext);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
