package me.roundaround.experienceprogress.mixin;

import me.roundaround.experienceprogress.ExperienceProgressMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
  @Shadow
  @Final
  private MinecraftClient client;

  @Shadow
  public abstract TextRenderer getTextRenderer();

  @Inject(at = @At("RETURN"), method = "renderExperienceBar")
  public void renderExperienceProgress(DrawContext drawContext, int x, CallbackInfo info) {
    if (!ExperienceProgressMod.enabled) {
      return;
    }

    PlayerEntity player = this.client.player;
    if (player == null) {
      return;
    }

    int experienceNeeded = player.getNextLevelExperience();
    float currentProgress = player.experienceProgress;
    int currentExperience = (int) (currentProgress * experienceNeeded);

    int x1 = x - 2;
    int x2 = x + 182 + 2;
    int y = this.client.getWindow().getScaledHeight() - 32 + 4;

    this.renderNumber(drawContext, String.valueOf(currentExperience), x1, y, true);
    this.renderNumber(drawContext, String.valueOf(experienceNeeded), x2, y, false);
  }

  @Unique
  private void renderNumber(
      DrawContext drawContext, String number, int x, int y, boolean rightAligned
  ) {
    TextRenderer textRenderer = this.getTextRenderer();
    int renderX = x + (rightAligned ? -textRenderer.getWidth(number) : 0);
    int renderY = y + Math.round((5 - textRenderer.fontHeight) / 2f);

    drawContext.drawText(textRenderer, number, renderX + 1, renderY, 0, false);
    drawContext.drawText(textRenderer, number, renderX - 1, renderY, 0, false);
    drawContext.drawText(textRenderer, number, renderX, renderY + 1, 0, false);
    drawContext.drawText(textRenderer, number, renderX, renderY - 1, 0, false);
    drawContext.drawText(textRenderer, number, renderX, renderY, 0xFFFFFF, false);
  }
}
