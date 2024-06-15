package tgmoss.sublanguage.mixin;

import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tgmoss.sublanguage.Option;
import tgmoss.sublanguage.Sublanguage;

import java.util.List;
import java.util.Objects;

@Mixin(LanguageManager.class)
public abstract class LanguageManagerMixin {
	@Shadow
	public abstract LanguageDefinition getLanguage(String code);

	@ModifyConstant(method = "reload(Lnet/minecraft/resource/ResourceManager;)V", constant = @Constant(intValue = 2))
	private int modifyListSize(int value) {
		return 3;
	}

	@Inject(method = "reload(Lnet/minecraft/resource/ResourceManager;)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void init(ResourceManager manager, CallbackInfo ci, List<String> list, boolean bl) {
		try {
			LanguageDefinition sublanguage = this.getLanguage(Option.SUBLANGUAGE.value);
			Objects.requireNonNull(sublanguage);
			list.add(Option.SUBLANGUAGE.value);
			Sublanguage.setSublanguage(manager, sublanguage);
		} catch(NullPointerException exception) {
			Sublanguage.LOGGER.error("无法加载第二语言的翻译对象：{}", Option.SUBLANGUAGE.value, exception);
		}
	}
}
