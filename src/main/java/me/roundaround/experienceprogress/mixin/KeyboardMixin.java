package me.roundaround.experienceprogress.mixin;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.roundaround.experienceprogress.ExperienceProgressMod;
import net.minecraft.client.Keyboard;
import net.minecraft.text.Text;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
  @Shadow
  public abstract void sendMessage(Text message);

  @Shadow
  public abstract void debugLog(Text message);

  @Inject(method = "processF3", at = @At(value = "RETURN"), cancellable = true)
  private void aboveWorldRendererReload(int key, CallbackInfoReturnable<Boolean> info) {
    if (!info.getReturnValue() && key == GLFW.GLFW_KEY_X) {
      ExperienceProgressMod.enabled = !ExperienceProgressMod.enabled;
      this.debugLog(
          Text.translatable(ExperienceProgressMod.enabled ? "experienceprogress.on" : "experienceprogress.off"));
      info.setReturnValue(true);
    }
  }

  @Inject(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;sendMessage(Lnet/minecraft/text/Text;)V", shift = At.Shift.AFTER, ordinal = 13))
  private void onDebugHelp(int key, CallbackInfoReturnable<Boolean> info) {
    this.sendMessage(Text.translatable("experienceprogress.debug.help"));
  }
}
