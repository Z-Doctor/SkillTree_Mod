package zdoctor.mcskilltree.skills;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;
import zdoctor.mcskilltree.api.ClientSkillApi;
import zdoctor.mcskilltree.api.IRequirement;
import zdoctor.mcskilltree.api.ISkillHandler;
import zdoctor.mcskilltree.api.ISkillProperty;
import zdoctor.mcskilltree.registries.SkillTreeRegistries;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;

/**
 * Basic skills will act as yes/no where modders can test if a player has a skill
 */
public class Skill extends ForgeRegistryEntry<Skill> {

    public static final ISkillProperty<Integer> TIER = SkillProperty.withDefault("skill_tier", 1);
    public static final ISkillProperty<Boolean> ACTIVE = SkillProperty.withDefault("skill_active", true);

    private String translationKey;

    protected SkillDisplayInfo displayInfo;
    protected ITextComponent displayText;

    protected Set<Skill> parents;
    protected Set<Skill> children;
    protected Set<IRequirement> requirements;

    protected int cost = 1;
    protected Predicate<LivingEntity> visibleCondition = Predicates.alwaysTrue();


    // TODO Change positions to be doubles, create builder to make display info and remove x, y from constructor
    public Skill(String name, Item icon) {
        this(name, icon.getDefaultInstance());
    }

    public Skill(String name, ItemStack icon) {
        setRegistryName(name);
        this.displayText = new StringTextComponent(Objects.requireNonNull(getRegistryName()).toString());
        // TODO Add way to automate placing
        displayInfo = new SkillDisplayInfo(this).setIcon(icon);
        registerSkill();

    }

    public Skill(String name, SkillDisplayInfo displayInfo) {
        setRegistryName(name);
        this.displayText = new StringTextComponent(Objects.requireNonNull(getRegistryName()).toString());
        this.displayInfo = displayInfo;
        registerSkill();
    }

    protected void registerSkill() {
        SkillTreeRegistries.SKILLS.register(this);
    }

    public Skill position(int x, int y) {
        displayInfo.setPosition(x, y);
        return this;
    }

    /**
     * Used to get the icon for the local player. Should not be used for anything else
     *
     * @return The icon for the local player
     */
    @OnlyIn(Dist.CLIENT)
    public ItemStack getIcon() {
        return displayInfo.getIcon();
    }

    /**
     * Used to determine if the icon for the local player should be rendered in the gui page
     * and ignore mouse input. Will not effect children
     *
     * @return - True if rendering should be skipped and mouse input ignored
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isHidden() {
        return !visibleCondition.test(ClientSkillApi.getPlayer());
    }

    public Skill setVisibilityCondition(Predicate<LivingEntity> condition) {
        this.visibleCondition = condition;
        return this;
    }


    public String getUnlocalizedName() {
        if (translationKey == null)
            translationKey = Util.makeTranslationKey("skill", getRegistryName());
        return translationKey;
    }

    public SkillDisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public boolean addRequirement(IRequirement requirement) {
        if (requirement == null)
            return false;
        if (requirements == null)
            requirements = Sets.newHashSet();
        return requirements.add(requirement);
    }

    public int getRequirementCount() {
        return requirements == null ? 0 : requirements.size();
    }

    public ITextComponent getDisplayText() {
        return displayText;
    }

    @Override
    public String toString() {
        return getDisplayText().getString();
    }

    public void addChildren(Skill... skills) {
        for (Skill skill : skills) {
            if (skill != null && skill != this) {
                addChild(skill);
            }
        }
    }

    public void addChild(@Nonnull Skill child) {
        if (child == this)
            throw new IllegalArgumentException("Tried to add self as child");
        if (this.children == null)
            this.children = Sets.newHashSet();
        if (children.add(child)) {
            // TODO Add support for multiple parents and drawing connections
            child.addParent(this);
        }
    }

    public List<Skill> getChildren() {
        return new ArrayList<>(hasChildren() ? children : Sets.newHashSet());
    }

    public List<Skill> getAllChildren() {
        return getAllChildren(new ArrayList<>());
    }

    protected List<Skill> getAllChildren(List<Skill> children) {
        if (!hasChildren())
            return children;
        for (Skill child : this.children) {
            children.add(child);
            child.getAllChildren(children);
        }
        return children;
    }

    public boolean hasChildren() {
        return children != null;
    }

    public Set<Skill> getParents() {
        return parents;
    }

    public boolean hasParents() {
        return parents != null;
    }

    public void addParent(Skill parent) {
        if (this.parents == null)
            this.parents = new HashSet<>();
        if (this.parents.add(parent))
            parent.addChild(this);
    }

    /**
     * Used to get additional data that may needed to be read or written about a skill a player has.
     *
     * @param properties - A map of properties in which to add additional properties. The key must be unique for proper
     *                   so prefixing the name with your modid is recommended.
     */
    public void getProperties(List<ISkillProperty<?>> properties) {
        properties.add(TIER);
        properties.add(ACTIVE);
    }

    public boolean canBuy(ISkillHandler handler) {
        return (!handler.hasSkill(this) || canBuyMultiple()) && hasRequirements(handler) && handler.getSkillPoints() >= getCost(handler);
    }


    /**
     * Applies logic after the skill is bought and cost is deducted
     *
     * @param handler - The handler that bought the skill
     */
    public void onBuy(ISkillHandler handler, boolean firstBuy) {
    }

    private boolean hasRequirements(ISkillHandler handler) {
        return getRequirements().stream().allMatch(requirement -> requirement.test(handler));
    }

    public List<IRequirement> getRequirements() {
        List<IRequirement> requirements = new ArrayList<>();
        if (hasParents())
            for (Skill parent : getParents()) {
                requirements.add(IRequirement.asParent(parent));
            }
        if (this.requirements != null)
            requirements.addAll(this.requirements);
        return requirements;
    }

    public boolean canBuyMultiple() {
        return false;
    }

    public Skill setCost(int cost) {
        this.cost = cost;
        return this;
    }

    /**
     * Get's the price based off the handler.
     *
     * @param handler - The handler to base the price off of
     * @return The cost or -1 if no cost (i.e. can't be bought)
     */
    public int getCost(ISkillHandler handler) {
        if (handler.hasSkill(this) && !canBuyMultiple())
            return -1;
        return cost;
    }

    public Skill offset(int x, int y) {
        getDisplayInfo().setOffset(x, y);
        return this;
    }
}
