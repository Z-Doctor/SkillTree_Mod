package zdoctor.mcskilltree.skills.criterion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameter;
import zdoctor.mcskilltree.api.ISkillHandler;
import zdoctor.mcskilltree.api.ISkillProperty;
import zdoctor.mcskilltree.api.SkillApi;
import zdoctor.mcskilltree.registries.SkillTreeRegistries;
import zdoctor.mcskilltree.skills.Skill;
import zdoctor.mcskilltree.skills.SkillData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkillPredicate {
    public static final SkillPredicate ANY = new SkillPredicate();

    private final Skill skill;
    private final LootContext.EntityTarget source;
    private final ISkillProperty<?>[] properties;

    public SkillPredicate() {
        skill = null;
        source = null;
        properties = null;
    }

    public LootParameter<? extends Entity> getSource() {
        return source != null ? source.getParameter() : null;
    }

    public SkillPredicate(Skill skill, LootContext.EntityTarget source, ISkillProperty<?>[] properties) {
        this.skill = skill;
        this.source = source;
        this.properties = properties;
    }

    public boolean test(LivingEntity entity) {
        ISkillHandler handler = SkillApi.getSkillHandler(entity);
        if (handler == ISkillHandler.EMPTY)
            return false;
        else if (this == ANY)
            return true;
        else if (!handler.hasSkill(skill))
            return false;

        if (properties != null) {
            SkillData data = handler.getData(skill);
            for (ISkillProperty<?> property : properties) {
                if (!data.match(property))
                    return false;
            }
        }
        return true;
    }

    public static SkillPredicate deserialize(JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            JsonObject jsonObject = JSONUtils.getJsonObject(element, "skill");

            ResourceLocation skillLocation = new ResourceLocation(jsonObject.get("skill").getAsString());
            Skill skill = SkillTreeRegistries.SKILLS.getValue(skillLocation);

            if (skill == null)
                return ANY;

            LootContext.EntityTarget source = LootContext.EntityTarget.fromString(jsonObject.get("entity").getAsString());

            if (!jsonObject.has("properties"))
                return new SkillPredicate(skill, source, null);

            List<ISkillProperty<?>> skillProperties = new ArrayList<>();
            skill.getProperties(skillProperties);

            List<ISkillProperty<?>> propertyList = new ArrayList<>();

            JsonArray jsonArray = JSONUtils.getJsonArray(jsonObject.get("properties"), "properties_array");
            if (!jsonArray.isJsonNull()) {
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject propObject = JSONUtils.getJsonObject(jsonElement, "properties_element");
                    ISkillProperty<?> found = null;
                    for (ISkillProperty<?> property : skillProperties) {
                        if (!propObject.has(property.getKey()))
                            continue;
                        ISkillProperty<?> prop = property.deserialize(propObject);
                        propertyList.add(prop);
                        found = property;
                        break;
                    }
                    if (found != null)
                        skillProperties.remove(found);
                }
            }

            return new SkillPredicate(skill, source, propertyList.toArray(new ISkillProperty<?>[0]));
        } else {
            return ANY;
        }
    }

    public JsonElement serialize() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject jsonobject = new JsonObject();
            if (skill != null) {
                jsonobject.addProperty("skill", Objects.requireNonNull(skill.getRegistryName()).toString());
            }

            if (source != null) {
                jsonobject.addProperty("entity", source.toString());
            }

            if (properties == null)
                return jsonobject;

            JsonArray jsonArray = new JsonArray();

            for (ISkillProperty<?> property : properties) {
                jsonArray.add(property.serialize());
            }
            jsonobject.add("properties", jsonArray);
            return jsonobject;
        }
    }

    public static class Builder {

    }
}
