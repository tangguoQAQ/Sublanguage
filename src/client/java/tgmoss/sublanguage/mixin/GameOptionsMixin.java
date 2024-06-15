package tgmoss.sublanguage.mixin;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tgmoss.sublanguage.Option;
import tgmoss.sublanguage.Sublanguage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
	@Shadow
	@Final
	private static Splitter COLON_SPLITTER;

	@Inject(method = "load()V", at = @At("TAIL"))
	private void loadSublanguageOptions(CallbackInfo ci) {
		if(Sublanguage.modOptionsFile == null) {
			Sublanguage.LOGGER.error("无法加载配置项。");
			return;
		}
		try(BufferedReader bufferedReader = Files.newBufferedReader(Sublanguage.modOptionsFile.toPath(), Charsets.UTF_8)) {
			Map<String, String> optionMap = new HashMap<>();
			bufferedReader.lines().map(line -> COLON_SPLITTER.split(line).iterator()).forEach(iterator -> {
				try {
					optionMap.put(iterator.next(), iterator.next());
				} catch(NoSuchElementException ignored) {
					// 跳过错误的配置行。
				}
			});
			for(Option option : Option.values()) {
				option.loadFrom(optionMap);
			}
		} catch(Exception e) {
			Sublanguage.LOGGER.error("无法加载配置项。", e);
		}
	}

	@Inject(method = "write()V", at = @At("TAIL"))
	private void writeSublanguageOptions(CallbackInfo ci) {
		if(Sublanguage.modOptionsFile == null) {
			Sublanguage.LOGGER.error("无法写出配置项。");
			return;
		}
		try(final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
				Files.newOutputStream(Sublanguage.modOptionsFile.toPath()), StandardCharsets.UTF_8))) {
			Option.writeAllTo(printWriter);
		} catch(IOException e) {
			Sublanguage.LOGGER.error("无法写出配置项。", e);
		}
	}
}
