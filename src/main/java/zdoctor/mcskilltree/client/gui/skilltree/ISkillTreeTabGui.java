package zdoctor.mcskilltree.client.gui.skilltree;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.renderer.ItemRenderer;
import zdoctor.mcskilltree.skills.Skill;
import zdoctor.mcskilltree.skilltree.SkillTree;
import zdoctor.mcskilltree.skilltree.SkillTreeInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ISkillTreeTabGui extends INestedGuiEventHandler {
    int getPage();
    SkillTreeInfo getDisplayInfo();
    boolean isActive();
    void setActive(boolean active);
    void reload();
    void buildTree();
    void drawTab(int guiLeft, int guiTop, boolean isSelected);

    void drawIcon(int guiLeft, int guiTop, ItemRenderer renderItemIn);
    void drawContents(int guiLeft, int guiTop, int mouseX, int mouseY);

    void drawToolTips(int mouseX, int mouseY);
}
