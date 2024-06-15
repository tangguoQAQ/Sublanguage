package tgmoss.sublanguage.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tgmoss.sublanguage.Option;
import tgmoss.sublanguage.Sublanguage;

@Mixin(Enchantment.class)
public abstract class EnchantmentTooltipMixin {
	@Shadow
	public abstract String getTranslationKey();

	@Inject(method = "getName(I)Lnet/minecraft/text/Text;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void addSublangage(int level, CallbackInfoReturnable<Text> cir, MutableText mutableText) {
		String key = this.getTranslationKey();
		if(Option.BILINGUAL_ITEM_TOOLTIPS.isFalse()
				|| Sublanguage.isMissingSubTranslation(key)) {
			return;
		}
		String translation = Sublanguage.getSubTranslation(key);
		if(translation.equals(Sublanguage.getTranslation(key))) {
			return;
		}
		mutableText.append(Option.getBilingualSeparator())
				.append(Text.literal(translation).formatted(Formatting.GRAY));
	}
}
