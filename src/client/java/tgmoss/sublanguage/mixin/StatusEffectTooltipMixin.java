package tgmoss.sublanguage.mixin;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tgmoss.sublanguage.Option;
import tgmoss.sublanguage.Sublanguage;

@Mixin(AbstractInventoryScreen.class)
public abstract class StatusEffectTooltipMixin {
	@Inject(method = "getStatusEffectDescription(Lnet/minecraft/entity/effect/StatusEffectInstance;)Lnet/minecraft/text/Text;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void addSublanguage(StatusEffectInstance statusEffect, CallbackInfoReturnable<Text> cir, MutableText mutableText) {
		String key = statusEffect.getTranslationKey();
		if(Option.BILINGUAL_ITEM_TOOLTIPS.isFalse()
				|| Sublanguage.isMissingSubTranslation(key)) {
			return;
		}
		String translation = Sublanguage.getSubTranslation(key);
		if(translation.equals(mutableText.getString())) {
			return;
		}
		mutableText.append(Option.getBilingualSeparator())
				.append(Text.literal(translation).formatted(Formatting.GRAY));
	}
}
