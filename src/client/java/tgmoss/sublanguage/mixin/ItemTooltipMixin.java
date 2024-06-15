package tgmoss.sublanguage.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tgmoss.sublanguage.Sublanguage;

@Mixin(ItemStack.class)
public abstract class ItemTooltipMixin {
	@Redirect(method = "getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getName()Lnet/minecraft/text/Text;"))
	private Text addSublanguage(ItemStack itemStack) {
		return Sublanguage.translate(itemStack);
	}

}
