package zdoctor.mcskilltree.client.gui.skills;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zdoctor.mcskilltree.McSkillTree;
import zdoctor.mcskilltree.api.ISkillInfoGui;
import zdoctor.mcskilltree.skilltree.SkillTreeBackground;

@OnlyIn(Dist.CLIENT)
public class SkillInfoGui extends AbstractSkillGui implements ISkillInfoGui {
    public static final ResourceLocation WINDOW = new ResourceLocation(
            McSkillTree.MODID, "textures/gui/skilltree/skill_info.png");

    protected final SkillEntryGui entryGui;
    protected SkillTreeBackground background;

    public SkillInfoGui(SkillEntryGui entryGui) {
        this.entryGui = entryGui;
        this.background = SkillTreeBackground.DEFAULT.with(0, 7, 0, 4);
    }

    @Override
    public boolean withinBounds(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public void draw(int guiLeft, int guiTop, int mouseX, int mouseY) {
        RenderSystem.pushMatrix();
        RenderSystem.enableDepthTest();
//        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
//        RenderSystem.colorMask(false, false, false, false);
//        fill(4680, 2260, -4680, -2260, -16777216);
//        RenderSystem.colorMask(true, true, true, true);
//        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
//        RenderSystem.depthFunc(518);
//        fill(234, 113, 0, 0, -16777216);
//        RenderSystem.depthFunc(515);

        int x = getSkillEntry().getX();
        int y = getSkillEntry().getY();
        RenderSystem.translatef(x + 15, y + -67, 0);
        this.renderInside(mouseX, mouseY, guiLeft, guiTop);
//        this.renderInfo()
        this.renderWindow(guiLeft, guiTop);
        this.renderMisc(mouseX, mouseY);

//        RenderSystem.depthFunc(518);
//        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
//        RenderSystem.colorMask(false, false, false, false);
//        fill(4680, 2260, -4680, -2260, -16777216);
//        RenderSystem.colorMask(true, true, true, true);
//        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
//        RenderSystem.depthFunc(515);
        RenderSystem.popMatrix();
    }

    protected void renderInside(int mouseX, int mouseY, int guiLeft, int guiTop) {
        background.renderAt(minecraft, guiLeft % 16, guiTop % 15);
    }

    protected void renderWindow(int guiLeft, int guiTop) {
        minecraft.getTextureManager().bindTexture(WINDOW);

        RenderSystem.enableBlend();
        // 11 69
        this.blit(0, 0, 0, 0, 131, 83);
        // 0, 194; 103, 62
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
