package tgmoss.sublanguage.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tgmoss.sublanguage.client.gui.SublanguageOptionsScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
	@ModifyArg(method = "init()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TexturedButtonWidget;<init>(IIIIIIILnet/minecraft/util/Identifier;IILnet/minecraft/client/gui/widget/ButtonWidget$PressAction;Lnet/minecraft/text/Text;)V"), index = 10)
	private ButtonWidget.PressAction initSublanguageOptionsScreen(ButtonWidget.PressAction pressAction) {
		MinecraftClient client = ((ScreenAccessor) this).getClient();
		return (button) -> client.setScreen(
				new SublanguageOptionsScreen(((TitleScreen) (Object) this), client.options, client.getLanguageManager()));
	}
}
