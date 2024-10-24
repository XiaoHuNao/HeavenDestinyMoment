package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.AttributeContext;
import com.xiaohunao.heaven_destiny_moment.common.context.WeightedContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AttributeRewardContext extends RewardContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("attribute");
    public static final Codec<AttributeRewardContext> CODEC = WeightedContext.codec(AttributeContext.CODEC)
            .fieldOf("attributes_info")
            .xmap(AttributeRewardContext::new, AttributeRewardContext::getAttribute)
            .codec();
    private WeightedContext<AttributeContext> attributes = WeightedContext.create();

    public AttributeRewardContext(Attribute attribute, String name, double amount, int operation, int weight) {
        addAttribute(attribute, name, amount, operation, weight);
    }
    public AttributeRewardContext(WeightedContext<AttributeContext> attributeWeighted) {
        this.attributes = attributeWeighted;
    }

    public WeightedContext<AttributeContext> getAttribute() {
        return attributes;
    }


    @Override
    public void createReward(MomentInstance momentInstance, Player player) {
        Level serverLevel = momentInstance.getLevel();
        attributes.getRandomValue(serverLevel.random).ifPresent(reward -> {
            Attribute attribute = reward.attribute();
            AttributeModifier attributeModifier = reward.attributeModifier();
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance != null) {
                instance.removeModifier(attributeModifier);
                instance.addPermanentModifier(attributeModifier);
            }
        });
    }

    public AttributeRewardContext addAttribute(Attribute attribute, String name, double amount, int operation, int weight) {
        attributes.add(new AttributeContext(attribute, name, amount, operation), weight);
        return this;
    }

    @Override
    public Codec<? extends RewardContext> getCodec() {
        return CODEC;
    }
}
