package tgmoss.sublanguage.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tgmoss.sublanguage.Option;
import tgmoss.sublanguage.Sublanguage;

import java.util.List;

@Mixin(PotionUtil.class)
public abstract class PotionTooltipMixin {
	@Inject(method = "buildTooltip(Ljava/util/List;Ljava/util/List;F)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 2))
	private static void addSublanguage(List<StatusEffectInstance> statusEffects, List<Text> list, float durationMultiplier, CallbackInfo ci) {
		String key = statusEffects.get(list.size() - 2).getTranslationKey();
		if(Option.BILINGUAL_ITEM_TOOLTIPS.isFalse()
				|| Sublanguage.isMissingSubTranslation(key)) {
			return;
		}
		int last = list.size() - 1;
		MutableText text = list.get(last).copy();
		String translation = Sublanguage.getSubTranslation(key);
		if(translation.equals(Sublanguage.getTranslation(key))) {
			return;
		}
		list.set(last, text.append(Option.getBilingualSeparator())
				.append(Text.literal(translation).formatted(Formatting.GRAY)));
	}
}
