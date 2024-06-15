package tgmoss.sublanguage.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import tgmoss.sublanguage.Option;
import tgmoss.sublanguage.Sublanguage;

@Environment(EnvType.CLIENT)
public class SublanguageClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Sublanguage.LOGGER.info("欢迎使用 Sublanguage 模组！作者：Tgmoss，QQ 3212517216。");
		Option.init();
	}
}
