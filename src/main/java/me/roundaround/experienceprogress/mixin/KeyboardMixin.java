package me.roundaround.experienceprogress.mixin;

import me.roundaround.experienceprogress.ExperienceProgressMod;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.gui.hud.ChatHud;

@Mixin(Keyboard.class)
public class KeyboardMixin {
  @Shadow
  @Final
  private MinecraftClient client;

  @Inject(method = "processF3", at = @At(value = "RETURN"), cancellable = true)
  private void aboveWorldRendererReload(int key, CallbackInfoReturnable<Boolean> info) {
    if (!info.getReturnValue() && key == GLFW.GLFW_KEY_X) {
      ExperienceProgressMod.enabled = !ExperienceProgressMod.enabled;
      ((KeyboardAccessor) this).invokeDebugLog(ExperienceProgressMod.enabled ? "experienceprogress.on" : "experienceprogress.off");
      info.setReturnValue(true);
    }
  }

  @Inject(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V", shift = At.Shift.AFTER, ordinal = 11))
  private void onDebugHelp(int key, CallbackInfoReturnable<Boolean> info) {
    ChatHud chatHud = this.client.inGameHud.getChatHud();
    chatHud.addMessage(Text.translatable("experienceprogress.debug.help"));
  }
}
