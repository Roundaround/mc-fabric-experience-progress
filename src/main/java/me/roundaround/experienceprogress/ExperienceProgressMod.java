package me.roundaround.experienceprogress;

import me.roundaround.experienceprogress.generated.Constants;
import me.roundaround.gradle.api.annotation.Entrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.gui.hud.debug.RendererDebugHudEntry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Entrypoint(Entrypoint.CLIENT)
public class ExperienceProgressMod implements ClientModInitializer {
  public static final Identifier DEBUG_HUD_ENTRY_IDENTIFIER = Identifier.of(Constants.MOD_ID, "visibility");

  public static KeyBinding toggleKeyBinding;

  @Override
  public void onInitializeClient() {
    DebugHudEntries.register(DEBUG_HUD_ENTRY_IDENTIFIER, new RendererDebugHudEntry());

    toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "experienceprogress.keybind.toggle",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_X,
        KeyBinding.Category.DEBUG
    ));
  }
}
