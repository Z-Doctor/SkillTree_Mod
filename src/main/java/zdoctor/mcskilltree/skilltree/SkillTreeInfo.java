package zdoctor.mcskilltree.skilltree;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.LogicalSide;
import zdoctor.mcskilltree.client.gui.skilltree.SkillTreeTabType;

import javax.annotation.Nullable;

public class SkillTreeInfo {
    public static SkillTreeInfo DEFAULT_TREE = new SkillTreeInfo(new ItemStack(Items.DIAMOND), "default.tree", "default.tree.desc").
            setBackground(SkillTreeBackground.DEFAULT);

    private int index;
    private SkillTreeTabType type;

    private String title;
    private String description;
    private ItemStack icon;
    private SkillTreeBackground background;

    public SkillTreeInfo(ItemStack icon, String title, String description) {
        this(-1, SkillTreeTabType.VERTICAL, icon, title, description);
    }

    public SkillTreeInfo(int index, SkillTreeTabType type, ItemStack icon, String title, String description) {
        this(index, type, icon, title, description, SkillTreeBackground.DEFAULT);
    }

    public SkillTreeInfo(int index, SkillTreeTabType type, ItemStack icon, String title, String description, @Nullable SkillTreeBackground background) {
        this.index = index;
        this.type = type;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.background = background;
    }

    public boolean isLocked(LogicalSide side) {
        return false;
    }

    public SkillTreeInfo setBackground(SkillTreeBackground background) {
        this.background = background;
        return this;
    }

    public SkillTreeInfo setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public SkillTreeInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public SkillTreeInfo setIndex(int index) {
        this.index = index;
        return this;
    }

    public SkillTreeInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    @Nullable
    public SkillTreeBackground getBackground() {
        return this.background;
    }

    public SkillTreeTabType getType() {
        return type;
    }

    public SkillTreeInfo setType(SkillTreeTabType type) {
        this.type = type;
        return this;
    }
}
