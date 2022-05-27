package noppes.npcs.controllers.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.entity.data.DataSkinOverlays;
import noppes.npcs.scripted.interfaces.ISkinOverlay;

public class SkinOverlay implements ISkinOverlay {
    public DataSkinOverlays parent;
    public ResourceLocation location = null;
    public String texture;
    public boolean glow = true;
    public float alpha = 1.0F;
    public float size = 1.0F;

    public float speedX = 0.0F;
    public float speedY = 0.0F;

    public float scaleX = 1.0F;
    public float scaleY = 1.0F;

    public float offsetX = 0.0F;
    public float offsetY = 0.0F;
    public float offsetZ = 0.0F;

    public SkinOverlay() {
    }

    public SkinOverlay(String texture) {
        this.texture = texture;
    }

    public SkinOverlay(String texture, boolean glow, float alpha, float size, float speedX, float speedY,
                       float scaleX, float scaleY, float offsetX, float offsetY, float offsetZ) {
        this.texture = texture;
        this.glow = glow;
        this.alpha = alpha;
        this.size = size;
        this.speedX = speedX;
        this.speedY = speedY;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public void setTexture(String texture) {
        this.texture = texture;
        this.updateClient();
    }
    public String getTexture() {
        return texture;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
        this.updateClient();
    }
    public boolean getGlow() {
        return glow;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.updateClient();
    }
    public float getAlpha() {
        return alpha;
    }

    public void setSize(float size) {
        this.size = size;
        this.updateClient();
    }
    public float getSize() {
        return size;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.updateClient();
    }
    public float getScaleX() {
        return scaleX;
    }
    public float getScaleY() {
        return scaleY;
    }

    public void setSpeed(float speedX, float speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.updateClient();
    }
    public float getSpeedX() {
        return speedX;
    }
    public float getSpeedY() {
        return speedY;
    }

    public void setOffset(float offsetX, float offsetY, float offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.updateClient();
    }
    public float getOffsetX() {
        return offsetX;
    }
    public float getOffsetY() {
        return offsetY;
    }
    public float getOffsetZ() {
        return offsetZ;
    }

    public void setLocation(ResourceLocation location) {
        this.location = location;
        this.updateClient();
    }
    public ResourceLocation getLocation() {
        return this.location;
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.texture = compound.getString("SkinOverlayTexture");
        this.glow = compound.getBoolean("SkinOverlayGlow");
        this.alpha = compound.getFloat("SkinOverlayAlpha");
        this.size = compound.getFloat("SkinOverlaySize");
        this.speedX = compound.getFloat("SkinOverlaySpeedX");
        this.speedY = compound.getFloat("SkinOverlaySpeedY");
        this.scaleX = compound.getFloat("SkinOverlayScaleX");
        this.scaleY = compound.getFloat("SkinOverlayScaleY");
        this.offsetX = compound.getFloat("SkinOverlayOffsetX");
        this.offsetY = compound.getFloat("SkinOverlayOffsetY");
        this.offsetZ = compound.getFloat("SkinOverlayOffsetZ");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("SkinOverlayTexture", this.getTexture());
        compound.setBoolean("SkinOverlayGlow", this.getGlow());
        compound.setFloat("SkinOverlayAlpha", this.getAlpha());
        compound.setFloat("SkinOverlaySize", this.getSize());
        compound.setFloat("SkinOverlaySpeedX", this.getSpeedX());
        compound.setFloat("SkinOverlaySpeedY", this.getSpeedY());
        compound.setFloat("SkinOverlayScaleX", this.getScaleX());
        compound.setFloat("SkinOverlayScaleY", this.getScaleY());
        compound.setFloat("SkinOverlayOffsetX", this.getOffsetX());
        compound.setFloat("SkinOverlayOffsetY", this.getOffsetY());
        compound.setFloat("SkinOverlayOffsetZ", this.getOffsetZ());

        return compound;
    }

    public static ISkinOverlay overlayFromNBT(NBTTagCompound compound) {
        SkinOverlay overlay = new SkinOverlay();
        overlay.readFromNBT(compound);
        return overlay;
    }

    private void updateClient() {
        if (this.parent != null) {
            this.parent.updateClient();
        }
    }
}