package me.roundaround.experienceprogress.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import me.roundaround.experienceprogress.ExperienceProgressMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Colors;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
  @Shadow
  @Final
  private MinecraftClient client;

  @WrapOperation(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/bar/Bar;renderBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
  private void wrapRenderBar(
      Bar instance,
      DrawContext context,
      RenderTickCounter renderTickCounter,
      Operation<Void> original,
      @Share("bar") LocalRef<Bar> barRef) {
    barRef.set(instance);
    original.call(instance, context, renderTickCounter);
  }

  @Inject(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/bar/Bar;drawExperienceLevel(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;I)V"))
  private void onDrawExperienceLevel(
      DrawContext context,
      RenderTickCounter tickCounter,
      CallbackInfo ci,
      @Share("bar") LocalRef<Bar> barRef) {
    if (!ExperienceProgressMod.enabled || this.client.player == null) {
      return;
    }

    int experienceNeeded = this.client.player.getNextLevelExperience();
    float currentProgress = this.client.player.experienceProgress;
    int currentExperience = (int) (currentProgress * experienceNeeded);

    Bar bar = barRef.get();
    int x = bar.getCenterX(this.client.getWindow());
    int y = bar.getCenterY(this.client.getWindow());

    int x1 = x - 2;
    int x2 = x + 182 + 2;

    this.renderNumber(context, String.valueOf(currentExperience), x1, y, true);
    this.renderNumber(context, String.valueOf(experienceNeeded), x2, y, false);
  }

  @Unique
  private void renderNumber(
      DrawContext context,
      String number,
      int x,
      int y,
      boolean rightAligned) {
    TextRenderer textRenderer = this.client.textRenderer;
    int renderX = x + (rightAligned ? -textRenderer.getWidth(number) : 0);
    int renderY = y + Math.round((5 - textRenderer.fontHeight) / 2f);

    context.drawText(textRenderer, number, renderX + 1, renderY, Colors.BLACK, false);
    context.drawText(textRenderer, number, renderX - 1, renderY, Colors.BLACK, false);
    context.drawText(textRenderer, number, renderX, renderY + 1, Colors.BLACK, false);
    context.drawText(textRenderer, number, renderX, renderY - 1, Colors.BLACK, false);
    context.drawText(textRenderer, number, renderX, renderY, Colors.WHITE, false);
  }
}
