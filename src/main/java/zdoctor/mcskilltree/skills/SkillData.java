package zdoctor.mcskilltree.skills;

import net.minecraft.nbt.CompoundNBT;
import zdoctor.mcskilltree.api.ISkillHandler;
import zdoctor.mcskilltree.api.ISkillProperty;

import java.util.*;

public class SkillData {

    private final Skill skill;
    protected List<ISkillProperty<?>> properties = new ArrayList<>();
    protected Map<ISkillProperty<?>, ISkillProperty<?>> propertiesMap = new HashMap<>();
    private final ISkillHandler skillHandler;


    public SkillData(ISkillHandler skillHandler, Skill skill) {
        this.skill = skill;
        this.skillHandler = skillHandler;
        skill.getProperties(properties);
    }

    public Skill getSkill() {
        return skill;
    }

    public int getTier() {
        return getProperty(Skill.TIER).cast();
    }

    public void setTier(int tier) {
        getProperty(Skill.TIER).setValue(() -> tier);
    }

    public ISkillProperty<?> getProperty(ISkillProperty<?> key) {
        if (propertiesMap.containsKey(key))
            return propertiesMap.get(key);
        ISkillProperty<?> property = key.copy();
        propertiesMap.put(key, property);
        return property;
    }

    public String getSkillName() {
        Skill skill = getSkill();
        return skill != null ? Objects.requireNonNull(skill.getRegistryName(), "Tried to get name of unregistered skill").toString() : null;
    }

    public void writeAdditional(CompoundNBT nbt) {
        for (ISkillProperty<?> property : properties) {
            getProperty(property).writeTo(nbt);
        }
    }

    public void readAdditional(CompoundNBT nbt) {
        for (ISkillProperty<?> property : properties) {
            getProperty(property).from(nbt);
        }
    }

}
