package tgmoss.sublanguage.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tgmoss.sublanguage.Option;
import tgmoss.sublanguage.Sublanguage;

import java.util.List;

@Mixin(BannerPatternItem.class)
public abstract class BannerPatternTooltipMixin {
	@Inject(method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V", at = @At("RETURN"))
	private void addSublanguage(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
		String key = ((BannerPatternItem) (Object) this).getTranslationKey() + ".desc";
		if(Option.BILINGUAL_ITEM_TOOLTIPS.isFalse()
				|| Sublanguage.isMissingSubTranslation(key)) {
			return;
		}
		String translation = Sublanguage.getSubTranslation(key);
		if(translation.equals(Sublanguage.getTranslation(key))) {
			return;
		}
		MutableText description = this.getDescription()
				.append(Option.getBilingualSeparator())
				.append(Text.literal(translation).formatted(Formatting.GRAY));
		tooltip.set(tooltip.size() - 1, description);
	}

	@Shadow
	public abstract MutableText getDescription();
}
