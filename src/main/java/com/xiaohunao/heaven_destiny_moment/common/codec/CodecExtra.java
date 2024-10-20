package com.xiaohunao.heaven_destiny_moment.common.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class CodecExtra {
    public static final Codec<UUID> UUID_CODEC = Codec.STRING
            .xmap(UUID::fromString, UUID::toString)
            .fieldOf("uuid")
            .codec();

    public static final Codec<MutableComponent> COMPONENT_CODEC = Codec.STRING
            .xmap(Component.Serializer::fromJson, Component.Serializer::toJson)
            .fieldOf("component")
            .codec();

    public static final Codec<AttributeModifier.Operation> ATTRIBUTE_MODIFIER_OPERATION_CODEC = Codec.INT
            .xmap(AttributeModifier.Operation::fromValue, AttributeModifier.Operation::toValue);
    public static final Codec<AttributeModifier> ATTRIBUTE_MODIFIER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(AttributeModifier::getName),
            Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
            ATTRIBUTE_MODIFIER_OPERATION_CODEC.fieldOf("operation").forGetter(AttributeModifier::getOperation)
    ).apply(instance, AttributeModifier::new));

    public static final Codec<MobEffectInstance> MOB_EFFECT_INSTANCE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
            Codec.INT.fieldOf("duration").forGetter(MobEffectInstance::getDuration),
            Codec.INT.fieldOf("amplifier").forGetter(MobEffectInstance::getAmplifier)
    ).apply(instance, MobEffectInstance::new));


    public static <T> Codec<Set<T>> setOf(Codec<T> codec) {
        return Codec.list(codec).xmap(HashSet::new, ArrayList::new);
    }

    public static <K,V> Codec<Map<K,V>> mapOf(Codec<K> codec, Codec<V> codec2) {
        return Codec.unboundedMap(codec, codec2).xmap(HashMap::new, HashMap::new);
    }
}