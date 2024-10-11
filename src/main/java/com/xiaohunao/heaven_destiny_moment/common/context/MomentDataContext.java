package com.xiaohunao.heaven_destiny_moment.common.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecExtra;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.ConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.EntityInfoContext;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.RewardContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MomentDataContext {
    public static final Codec<MomentDataContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("ready_time",0).forGetter(MomentDataContext::getReadyTime),
            CodecExtra.setOf(RewardContext.CODEC).optionalFieldOf("rewards",Sets.newHashSet()).forGetter(MomentDataContext::getRewards),
            CodecExtra.setOf(ConditionContext.CODEC).optionalFieldOf("conditions",Sets.newHashSet()).forGetter(MomentDataContext::getConditions),
            Codec.list(Codec.list(EntityInfoContext.CODEC)).optionalFieldOf("waves",Lists.newArrayList()).forGetter(MomentDataContext::getWaves),
            MobSpawnSettingsContext.CODEC.optionalFieldOf("mob_spawn_settings",MobSpawnSettingsContext.Default).forGetter(MomentDataContext::getMobSpawnSettingsContext)
    ).apply(instance, MomentDataContext::new));

    protected int readyTime;
    protected Set<RewardContext> rewards;
    protected Set<ConditionContext> conditions;
    protected List<List<EntityInfoContext>> waves;
    protected MobSpawnSettingsContext mobSpawnSettingsContext;

    public MomentDataContext(int readyTime, Set<RewardContext> rewards, Set<ConditionContext> conditions, List<List<EntityInfoContext>> waves,MobSpawnSettingsContext mobSpawnSettingsContext) {
        this.readyTime = readyTime;
        this.rewards = rewards;
        this.conditions = conditions;
        this.waves = waves;
        this.mobSpawnSettingsContext = mobSpawnSettingsContext;
    }

    public boolean canCreate(MomentInstance momentInstance, Level level, BlockPos pos, @Nullable Player player) {
        if (conditions.isEmpty()) return true;
        for (ConditionContext condition : conditions) {
            return condition.hasCustom() ? condition.canCreateCustom(momentInstance, level, pos, player) : condition.canCreate(momentInstance, level, pos, player);
        }
        return true;
    }

    public void createReward(MomentInstance momentInstance, Player player) {
        for (RewardContext reward : rewards) {
            if (reward.hasCustom()) {
                reward.createRewardCustom(momentInstance, player);
            } else {
                reward.createReward(momentInstance, player);
            }
        }
    }

    public int getReadyTime() {
        return readyTime;
    }

    public Set<RewardContext> getRewards() {
        return rewards;
    }

    public Set<ConditionContext> getConditions() {
        return conditions;
    }

    public List<List<EntityInfoContext>> getWaves() {
        return waves;
    }

    public MobSpawnSettingsContext getMobSpawnSettingsContext() {
        return mobSpawnSettingsContext;
    }

    public static class Builder {
        private int readyTime = 0;
        private Set<RewardContext> rewards = Sets.newHashSet();
        private Set<ConditionContext> conditions = Sets.newHashSet();
        private List<List<EntityInfoContext>> waves = Lists.newArrayList();
        private boolean allowOriginalBiomeSpawnSettings = false;
        private boolean forceSurfaceSpawning = false;
        private boolean ignoreLightLevel = false;
        private MobSpawnSettings.Builder mobSpawnSettings = new MobSpawnSettings.Builder();
        private Map<MobCategory, Double> spawnCategoryMultiplier = Maps.newHashMap();
        private MobSpawnListContext mobSpawnListContext = new MobSpawnListContext(Lists.newArrayList(), Lists.newArrayList(),true);

        public MomentDataContext build() {
            return new MomentDataContext(readyTime, rewards, conditions, waves, new MobSpawnSettingsContext(allowOriginalBiomeSpawnSettings,forceSurfaceSpawning,ignoreLightLevel,mobSpawnSettings.build(),spawnCategoryMultiplier, mobSpawnListContext));
        }

        public Builder addReward(RewardContext... reward) {
            rewards.addAll(List.of(reward));
            return this;
        }
        public Builder addCondition(ConditionContext... condition) {
            conditions.addAll(List.of(condition));
            return this;
        }
        public Builder readyTime(int readyTime) {
            this.readyTime = readyTime;
            return this;
        }
        public Builder allowOriginalBiomeSpawnSettings (boolean allowOriginalBiomeSpawnSettings) {
            this.allowOriginalBiomeSpawnSettings = allowOriginalBiomeSpawnSettings;
            return this;
        }
        public Builder forceSurfaceSpawning (boolean forceSurfaceSpawning) {
            this.forceSurfaceSpawning = forceSurfaceSpawning;
            return this;
        }
        public Builder ignoreLightLevel (boolean ignoreLightLevel) {
            this.ignoreLightLevel = ignoreLightLevel;
            return this;
        }
        public Builder addEntitySpawnInfo(MobCategory mobCategory ,MobSpawnSettings.SpawnerData... spawnerData){
            for (MobSpawnSettings.SpawnerData data : spawnerData) {
                mobSpawnSettings.addSpawn(mobCategory,data);
            }
            return this;
        }
        public Builder creatureGenerationProbability(float creatureGenerationProbability) {
            this.mobSpawnSettings.creatureGenerationProbability(creatureGenerationProbability);
            return this;
        }
        public Builder spawnMultiplier(MobCategory mobCategory, double multiplier) {
            this.spawnCategoryMultiplier.put(mobCategory,multiplier);
            return this;
        }
        public Builder addBlackWhiteListEntityType(EntityType<?> entityType) {
            this.mobSpawnListContext.addEntityType(entityType);
            return this;
        }
        public Builder addBlackWhiteListTagKey(TagKey<EntityType<?>> tagKey) {
            this.mobSpawnListContext.addTagKey(tagKey);
            return this;
        }
        public Builder switchListType(){
            this.mobSpawnListContext = this.mobSpawnListContext.switchListType();
            return this;
        }

    }
}