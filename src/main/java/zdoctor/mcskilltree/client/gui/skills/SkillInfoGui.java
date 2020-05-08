package zdoctor.mcskilltree.client.gui.skills;

import com.mojang.blaze3d.systems.RenderSystem;
import zdoctor.mcskilltree.api.ISkillInfoGui;
import zdoctor.mcskilltree.skilltree.SkillTreeBackground;

public class SkillInfoGui extends AbstractSkillGui implements ISkillInfoGui {
    private final SkillEntryGui entryGui;

    public SkillInfoGui(SkillEntryGui entryGui) {
        this.entryGui = entryGui;
    }

    @Override
    public boolean withinBounds(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public void draw(int guiLeft, int guiTop, int mouseX, int mouseY) {
        RenderSystem.pushMatrix();
        this.renderInside(mouseX, mouseY, guiLeft, guiTop);
        this.renderWindow(guiLeft, guiTop);
        this.renderMisc(mouseX, mouseY);
//        int left = guiLeft + 252;
//        // 140
//        RenderSystem.translatef(guiLeft + 3, guiTop, 0);
//        this.blit(252, 0, 0, 0, 252, 140);
        RenderSystem.popMatrix();
    }

    protected void renderInside(int mouseX, int mouseY, int guiLeft, int guiTop) {
    }

    protected void renderWindow(int guiLeft, int guiTop) {
        minecraft.getTextureManager().bindTexture(SkillTreeBackground.WINDOW);
        int x = getSkillEntry().getX();
        int y = getSkillEntry().getY();
        this.blit(x, y, 0, 0, 252, 140);
    }

    protected void renderMisc(int mouseX, int mouseY) {
    }

    @Override
    public SkillEntryGui getSkillEntry() {
        return entryGui;
    }

    @Override
    public void onClose() {

    }
}
