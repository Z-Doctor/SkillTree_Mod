package zdoctor.mcskilltree.api;

import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.renderer.ItemRenderer;
import zdoctor.mcskilltree.skilltree.SkillTreeInfo;

public interface ISkillTreeTabGui extends INestedGuiEventHandler {
    int getPage();
    SkillTreeInfo getDisplayInfo();
    boolean isActive();
    void setActive(boolean active);
    void reload();
    void buildTree();
    void drawTab(int guiLeft, int guiTop, boolean isSelected);

    void drawIcon(int guiLeft, int guiTop, ItemRenderer renderItemIn);

    void preDrawContents(int guiLeft, int guiTop, int mouseX, int mouseY);
    void drawContents(int guiLeft, int guiTop, int mouseX, int mouseY);
    void postDrawContents(int guiLeft, int guiTop, int mouseX, int mouseY);

    void drawMisc(int guiLeft, int guiTop, int mouseX, int mouseY);

    void onOpen();
    void onClose();
}
