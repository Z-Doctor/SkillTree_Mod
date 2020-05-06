package zdoctor.mcskilltree.world.storage.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameter;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import zdoctor.mcskilltree.McSkillTree;
import zdoctor.mcskilltree.api.SkillApi;
import zdoctor.mcskilltree.registries.SkillTreeRegistries;
import zdoctor.mcskilltree.skills.Skill;
import zdoctor.mcskilltree.world.storage.loot.SkillLootParameters;

import java.util.Set;
import java.util.function.Predicate;

public class HasSkill implements ILootCondition {
    private final Skill skill;
    private final Predicate<LivingEntity> predicate;

    public HasSkill(Skill skill) {
        this.skill = skill;
        this.predicate = entity -> SkillApi.hasSkill(entity, skill);
    }

    public Skill getSkill() {
        // TODO Remove this in favor of skill predicate
        return skill;
    }

    @Override
    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootParameters.THIS_ENTITY, SkillLootParameters.SKILL);
    }

    public static ILootCondition.IBuilder builder(Skill skill) {
        // TODO Add support for testing tiers, active, custom nbt tags/properties
        return () -> new HasSkill(skill);
    }

    @Override
    public boolean test(LootContext lootContext) {
        LivingEntity entity = (LivingEntity) lootContext.get(LootParameters.KILLER_ENTITY);
        return entity != null && predicate.test(entity);
    }


    public static class Serializer extends ILootCondition.AbstractSerializer<HasSkill> {

        public Serializer() {
            super(new ResourceLocation(McSkillTree.MODID, "has_skill"), HasSkill.class);
        }

        @Override
        public void serialize(JsonObject json, HasSkill value, JsonSerializationContext context) {
            // TODO Add support for serializing
            //json.add("predicate", value.predicate.serialize());
            json.addProperty("skill", value.getSkill().getRegistryName().toString());
        }

        @Override
        public HasSkill deserialize(JsonObject json, JsonDeserializationContext context) {
            Skill skill = SkillTreeRegistries.SKILLS.getValue(new ResourceLocation(json.get("skill").getAsString()));
            return new HasSkill(skill);
        }
    }
}
