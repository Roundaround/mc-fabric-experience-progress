package me.roundaround.experienceprogress.mixin;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Keyboard.class)
public interface KeyboardAccessor {
  @Invoker
  void invokeDebugLog(String key, Object... args);
}
