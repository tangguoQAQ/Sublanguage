package tgmoss.sublanguage;

import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Sublanguage {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "sublanguage";
	@Nullable
	private static TranslationStorage subTranslationStorage;
	@Nullable
	public static File modOptionsFile;

	public static void setSublanguage(ResourceManager manager, LanguageDefinition sublanguage) {
		Sublanguage.subTranslationStorage = TranslationStorage.load(manager,
				List.of(Option.SUBLANGUAGE.value), sublanguage.rightToLeft());
		LOGGER.info("已加载第二语言（{}）的翻译对象。", Option.SUBLANGUAGE.value);
	}

	public static boolean isMissingSubTranslation(String key) {
		return Sublanguage.subTranslationStorage == null || !Sublanguage.subTranslationStorage.hasTranslation(key);
	}

	/**
	 * @throws NullPointerException 调用时 {@code Sublanguage.subTranslationStorage} 可能为空。
	 */
	public static String getSubTranslation(String key) throws NullPointerException {
		return Objects.requireNonNull(Sublanguage.subTranslationStorage).get(key);
	}

	public static String getTranslation(String key) {
		return Language.getInstance().get(key);
	}

	public static Text translate(ItemStack itemStack) {
		Text name = itemStack.getName();
		String key = itemStack.getTranslationKey();
		if(Option.BILINGUAL_DISPLAY.isFalse()
				|| isMissingSubTranslation(key)
				|| itemStack.hasCustomName()) {
			return name;
		}
		String subName = getSubTranslation(key);
		if(subName.equals(getTranslation(key))) {
			return name;
		}
		return name.copy().append(Option.getBilingualSeparator())
				.append(Text.literal(subName).formatted(Formatting.GRAY));
	}
}