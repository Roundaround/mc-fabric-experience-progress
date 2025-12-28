package me.roundaround.experienceprogress.mixin;

import me.roundaround.experienceprogress.ExperienceProgressMod;
import me.roundaround.experienceprogress.generated.Constants;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
  @Shadow
  @Final
  private MinecraftClient client;

  @Shadow
  protected abstract void debugLog(Text message);

  @Inject(method = "processF3", at = @At(value = "TAIL"), cancellable = true)
  private void processExperienceProgressF3(KeyInput input, CallbackInfoReturnable<Boolean> info) {
    if (ExperienceProgressMod.toggleKeyBinding.matchesKey(input)) {
      boolean state = this.client.debugHudEntryList.toggleVisibility(ExperienceProgressMod.DEBUG_HUD_ENTRY_IDENTIFIER);
      this.debugLog(Text.translatable(state ? "experienceprogress.on" : "experienceprogress.off"));
      info.setReturnValue(true);
    }
  }
}
