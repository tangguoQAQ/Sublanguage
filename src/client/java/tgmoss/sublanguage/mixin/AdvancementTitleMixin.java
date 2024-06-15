package tgmoss.sublanguage.mixin;

import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tgmoss.sublanguage.Option;
import tgmoss.sublanguage.Sublanguage;

@Mixin(AdvancementDisplay.class)
public abstract class AdvancementTitleMixin {
	@ModifyArg(method = "fromJson(Lcom/google/gson/JsonObject;)Lnet/minecraft/advancement/AdvancementDisplay;", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementDisplay;<init>(Lnet/minecraft/item/ItemStack;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Lnet/minecraft/util/Identifier;Lnet/minecraft/advancement/AdvancementFrame;ZZZ)V"), index = 1)
	private static Text addSublanguage(Text text) {
		if(!(text.getContent() instanceof TranslatableTextContent title)) {
			return text;
		}
		String key = title.getKey();
		if(Option.BILINGUAL_DISPLAY.isFalse()
				|| Sublanguage.isMissingSubTranslation(key)) {
			return text;
		}
		String translation = Sublanguage.getSubTranslation(key);
		if(translation.equals(Sublanguage.getTranslation(key))) {
			return text;
		}

		return text.copy().append(Option.getBilingualSeparator())
				.append(Text.literal(translation).formatted(Formatting.GRAY));
	}
}
