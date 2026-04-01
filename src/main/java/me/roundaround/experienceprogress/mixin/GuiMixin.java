package me.roundaround.experienceprogress.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.roundaround.experienceprogress.ExperienceProgressMod;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.util.CommonColors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
  @Shadow
  @Final
  private Minecraft minecraft;

  @WrapOperation(
      method = "extractHotbarAndDecorations", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;extractBackground" +
               "(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
  )
  )
  private void wrapRenderBar(
      ContextualBarRenderer instance,
      GuiGraphicsExtractor context,
      DeltaTracker renderTickCounter,
      Operation<Void> original,
      @Share("bar") LocalRef<ContextualBarRenderer> barRef
  ) {
    barRef.set(instance);
    original.call(instance, context, renderTickCounter);
  }

  @Inject(
      method = "extractHotbarAndDecorations", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;extractExperienceLevel" +
               "(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;I)V"
  )
  )
  private void onDrawExperienceLevel(
      GuiGraphicsExtractor context,
      DeltaTracker tickCounter,
      CallbackInfo ci,
      @Share("bar") LocalRef<ContextualBarRenderer> barRef
  ) {
    if (!this.minecraft.debugEntries.isCurrentlyEnabled(ExperienceProgressMod.DEBUG_HUD_ENTRY_IDENTIFIER) ||
        this.minecraft.player == null) {
      return;
    }

    int experienceNeeded = this.minecraft.player.getXpNeededForNextLevel();
    float currentProgress = this.minecraft.player.experienceProgress;
    int currentExperience = (int) (currentProgress * experienceNeeded);

    ContextualBarRenderer bar = barRef.get();
    int x = bar.left(this.minecraft.getWindow());
    int y = bar.top(this.minecraft.getWindow());

    int x1 = x - 2;
    int x2 = x + 182 + 2;

    this.renderNumber(context, String.valueOf(currentExperience), x1, y, true);
    this.renderNumber(context, String.valueOf(experienceNeeded), x2, y, false);
  }

  @Unique
  private void renderNumber(GuiGraphicsExtractor context, String number, int x, int y, boolean rightAligned) {
    Font textRenderer = this.minecraft.font;
    int renderX = x + (rightAligned ? -textRenderer.width(number) : 0);
    int renderY = y + Math.round((5 - textRenderer.lineHeight + 2) / 2f);

    context.text(textRenderer, number, renderX + 1, renderY, CommonColors.BLACK, false);
    context.text(textRenderer, number, renderX - 1, renderY, CommonColors.BLACK, false);
    context.text(textRenderer, number, renderX, renderY + 1, CommonColors.BLACK, false);
    context.text(textRenderer, number, renderX, renderY - 1, CommonColors.BLACK, false);
    context.text(textRenderer, number, renderX, renderY, CommonColors.WHITE, false);
  }
}
