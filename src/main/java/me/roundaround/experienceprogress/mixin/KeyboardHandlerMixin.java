package me.roundaround.experienceprogress.mixin;

import me.roundaround.experienceprogress.ExperienceProgressMod;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
  @Shadow
  @Final
  private Minecraft minecraft;

  @Shadow
  protected abstract void debugFeedbackComponent(Component message);

  @Inject(method = "handleDebugKeys", at = @At(value = "TAIL"), cancellable = true)
  private void processExperienceProgressF3(KeyEvent input, CallbackInfoReturnable<Boolean> info) {
    if (ExperienceProgressMod.toggleKeyBinding.matches(input)) {
      boolean state = this.minecraft.debugEntries.toggleStatus(ExperienceProgressMod.DEBUG_HUD_ENTRY_IDENTIFIER);
      this.debugFeedbackComponent(Component.translatable(state ? "experienceprogress.on" : "experienceprogress.off"));
      info.setReturnValue(true);
    }
  }
}
