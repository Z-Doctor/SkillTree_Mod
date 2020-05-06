package zdoctor.mcskilltree.skilltree;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zdoctor.mcskilltree.McSkillTree;
import zdoctor.mcskilltree.client.gui.skilltree.AbstractSkillTreeGui;

public class SkillTreeBackground {
    // TODO Move to more intuitive spot
    public static final ResourceLocation WINDOW = new ResourceLocation(
            McSkillTree.MODID, "textures/gui/skilltree/skill_tree.png");
    public static final ResourceLocation TABS = new ResourceLocation(
            McSkillTree.MODID, "textures/gui/skilltree/tabs.png");

    public static SkillTreeBackground DEFAULT = new SkillTreeBackground(176f, 212f);
    public static SkillTreeBackground SANDSTONE = new SkillTreeBackground(192f, 212f);
    public static SkillTreeBackground ENDSTONE = new SkillTreeBackground(208f, 212f);
    public static SkillTreeBackground DIRT = new SkillTreeBackground(224f, 212f);
    public static SkillTreeBackground NETHERRACK = new SkillTreeBackground(176f, 228f);
    public static SkillTreeBackground STONE = new SkillTreeBackground(192f, 228f);

    private float u;
    private float v;
    private int width = 16;
    private int height = 16;
    private int texWidth = 256;
    private int texHeight = 256;
    private ResourceLocation resourceLocation = WINDOW;

    public SkillTreeBackground(float u, float v) {
        this.u = u;
        this.v = v;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public SkillTreeBackground setUV(float u, float v) {
        this.u = u;
        this.v = v;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public SkillTreeBackground setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public int getTexWidth() {
        return texWidth;
    }

    public int getTexHeight() {
        return texHeight;
    }

    public SkillTreeBackground setTexDimensions(int texWidth, int texHeight) {
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        return this;
    }

    public SkillTreeBackground setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        return this;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(Minecraft minecraft, int xOffset, int yOffset) {
        int x = xOffset % 16;
        int y = yOffset % 16;
        minecraft.getTextureManager().bindTexture(getResourceLocation());
        for (int row = -1; row <= 15; ++row) {
            for (int col = -1; col <= 8; ++col) {
                AbstractSkillTreeGui.draw2DTex(x + getWidth() * row, y + getHeight() * col,
                        getU(), getV(), getWidth(), getHeight(),
                        getTexWidth(), getTexHeight());
            }
        }
    }
}
