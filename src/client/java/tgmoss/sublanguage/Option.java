package tgmoss.sublanguage;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public enum Option {
	SUBLANGUAGE("sublanguage", "zh_tw"),
	BILINGUAL_DISPLAY("bilingualDisplay", Option.FALSE),
	BILINGUAL_SEPARATOR("bilingualSeparator", " - "),
	BILINGUAL_ITEM_TOOLTIPS("bilingualItemTooltips", Option.FALSE);

	private final String key;
	public String value;

	public static final String TRUE = "true", FALSE = "false";

	Option(String key, String defaultValue) {
		this.key = key;
		this.value = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public void loadFrom(Map<String, String> optionMap) {
		if(optionMap.containsKey(key)) {
			value = optionMap.get(key);
		} else {
			Sublanguage.LOGGER.warn("找不到配置项：{}", key);
		}
	}

	public void writeTo(PrintWriter printWriter) {
		printWriter.println(key + ":" + value);
	}

	public boolean isTrue() {
		return TRUE.equals(this.value);
	}

	public boolean isFalse() {
		return FALSE.equals(this.value);
	}

	public static Text getBilingualSeparator() {
		return Text.literal(BILINGUAL_SEPARATOR.value).formatted(Formatting.DARK_GRAY);
	}

	public static void writeAllTo(PrintWriter printWriter) {
		for(Option option : Option.values()) {
			option.writeTo(printWriter);
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void init() {
		try {
			File modOptionsDir = new File(MinecraftClient.getInstance().runDirectory, "config");
			modOptionsDir.mkdir();
			Sublanguage.modOptionsFile = new File(modOptionsDir, Sublanguage.MOD_ID + ".txt");
			if(Sublanguage.modOptionsFile.createNewFile()) {
				// 写出默认配置项。
				final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
						Files.newOutputStream(Sublanguage.modOptionsFile.toPath()), StandardCharsets.UTF_8));
				writeAllTo(printWriter);
				printWriter.close();
			}
		} catch(IOException e) {
			Sublanguage.LOGGER.error("无法初始化配置项。", e);
		}
	}
}
