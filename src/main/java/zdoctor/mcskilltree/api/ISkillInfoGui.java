package zdoctor.mcskilltree.api;

import net.minecraft.client.gui.IGuiEventListener;
import zdoctor.mcskilltree.client.gui.skills.SkillEntryGui;

public interface ISkillInfoGui extends IGuiEventListener {

    boolean withinBounds(double mouseX, double mouseY);

    void draw(int guiLeft, int guiTop, int mouseX, int mouseY);

    SkillEntryGui getSkillEntry();

    void onClose();
}
