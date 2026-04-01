package me.roundaround.experienceprogress;

import com.mojang.blaze3d.platform.InputConstants;
import me.roundaround.experienceprogress.generated.Constants;
import me.roundaround.gradle.api.annotation.Entrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.debug.DebugEntryNoop;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

@Entrypoint(Entrypoint.CLIENT)
public class ExperienceProgressMod implements ClientModInitializer {
  public static final Identifier DEBUG_HUD_ENTRY_IDENTIFIER = Identifier.fromNamespaceAndPath(
      Constants.MOD_ID,
      "visibility"
  );

  public static KeyMapping toggleKeyBinding;

  @Override
  public void onInitializeClient() {
    DebugScreenEntries.register(DEBUG_HUD_ENTRY_IDENTIFIER, new DebugEntryNoop());

    toggleKeyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        "experienceprogress.keybind.toggle",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_X,
        KeyMapping.Category.DEBUG
    ));
  }
}
