package tgmoss.sublanguage.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tgmoss.sublanguage.Sublanguage;

@Mixin(InGameHud.class)
public abstract class HeldItemTooltipMixin {
	@Redirect(method = "renderHeldItemTooltip(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getName()Lnet/minecraft/text/Text;"))
	private Text addSublanguage(ItemStack currentStack) {
		return Sublanguage.translate(currentStack);
	}
}
