package tgmoss.sublanguage.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import tgmoss.sublanguage.Option;
import tgmoss.sublanguage.Sublanguage;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class SublanguageOptionsScreen extends GameOptionsScreen {
	private static final Text LANGUAGE_WARNING_TEXT = Text.literal("(")
			.append(Text.translatable("options.languageWarning")).append(")").formatted(Formatting.GRAY);
	final LanguageManager languageManager;
	private SublanguageSelectionListWidget sublanguageSelectionListWidget;
	private static final Text PREFERRED_LANGUAGE = Text.literal("首选语言");
	private static final Text SUBLANGUAGE = Text.literal("第二语言");

	public SublanguageOptionsScreen(Screen parent, GameOptions options, LanguageManager languageManager) {
		super(parent, options, Text.translatable("options.language"));
		this.languageManager = languageManager;
	}

	protected void init() {
		this.sublanguageSelectionListWidget = new SublanguageSelectionListWidget(this.client);
		this.addSelectableChild(this.sublanguageSelectionListWidget);
		this.addDrawableChild(this.gameOptions.getForceUnicodeFont()
				.createWidget(this.gameOptions, this.width / 2 - 155, this.height - 38, 150));
		this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.onDone())
				.dimensions(this.width / 2 - 155 + 160, this.height - 38, 150, 20).build());
		super.init();
	}

	void onDone() {
		Objects.requireNonNull(this.client);
		SublanguageSelectionListWidget.SublanguageEntry languageEntry = sublanguageSelectionListWidget.getSelectedOrNull();
		if (languageEntry != null && !languageEntry.languageCode.equals(this.languageManager.getLanguage())) {
			this.languageManager.setLanguage(languageEntry.languageCode);
			this.gameOptions.language = languageEntry.languageCode;
			this.client.reloadResources();
			this.gameOptions.write();
		}
		Sublanguage.setSublanguage((ResourceManager) languageManager, Objects.requireNonNull(
				languageManager.getLanguage(sublanguageSelectionListWidget.selectedSublanguage)));
		this.client.setScreen(this.parent);
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (KeyCodes.isToggle(keyCode)) {
			SublanguageSelectionListWidget.SublanguageEntry sublanguageEntry = sublanguageSelectionListWidget.getSelectedOrNull();
			if (sublanguageEntry != null) {
				sublanguageEntry.onPressed();
				this.onDone();
				return true;
			}
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.sublanguageSelectionListWidget.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 16777215);
		context.drawCenteredTextWithShadow(this.textRenderer, LANGUAGE_WARNING_TEXT, this.width / 2, this.height - 56, 8421504);
		super.render(context, mouseX, mouseY, delta);
	}

	@Environment(EnvType.CLIENT)
	private class SublanguageSelectionListWidget extends AlwaysSelectedEntryListWidget<SublanguageSelectionListWidget.SublanguageEntry> {
		private String selectedSublanguage;

		public SublanguageSelectionListWidget(MinecraftClient client) {
			super(client, SublanguageOptionsScreen.this.width, SublanguageOptionsScreen.this.height, 32,
					SublanguageOptionsScreen.this.height - 65 + 4, 18);
			String string = SublanguageOptionsScreen.this.languageManager.getLanguage();
			SublanguageOptionsScreen.this.languageManager.getAllLanguages().forEach((languageCode, languageDefinition) -> {
				SublanguageEntry languageEntry = new SublanguageEntry(getEntryCount(), languageCode, languageDefinition);
				this.addEntry(languageEntry);
				if (string.equals(languageCode)) {
					this.setSelected(languageEntry);
				} else if(Option.SUBLANGUAGE.value.equals(languageCode)) {
					selectedSublanguage = languageCode;
				}
			});
			if (this.getSelectedOrNull() != null) {
				this.centerScrollOn(this.getSelectedOrNull());
			}
		}

		protected int getScrollbarPositionX() {
			return super.getScrollbarPositionX() + 20;
		}

		public int getRowWidth() {
			return super.getRowWidth() + 50;
		}

		protected void renderBackground(DrawContext context) {
			SublanguageOptionsScreen.this.renderBackground(context);
		}

		@Environment(EnvType.CLIENT)
		public class SublanguageEntry extends AlwaysSelectedEntryListWidget.Entry<SublanguageEntry> {
			final int index;
			final String languageCode;
			private final Text languageDefinition;
			private long clickTime;

			public SublanguageEntry(int index, String languageCode, LanguageDefinition languageDefinition) {
				this.index = index;
				this.languageCode = languageCode;
				this.languageDefinition = languageDefinition.getDisplayText();
			}

			public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
				if(isSelectedEntry(index)) {
					context.drawTextWithShadow(textRenderer, PREFERRED_LANGUAGE,
							left + entryHeight - textRenderer.getWidth(PREFERRED_LANGUAGE) , y + 1, 16777215);
				} else if(selectedSublanguage.equals(this.languageCode)) {
					context.drawTextWithShadow(textRenderer, SUBLANGUAGE,
							left + entryHeight - textRenderer.getWidth(SUBLANGUAGE) , y + 1, 16777215);
					drawSelectionHighlight(context, y, entryWidth, entryHeight, 0, 5609762);
				}

				context.drawCenteredTextWithShadow(textRenderer, this.languageDefinition,
						SublanguageSelectionListWidget.this.width / 2, y + 1, 16777215);
			}

			public boolean mouseClicked(double mouseX, double mouseY, int button) {
				if (button == 0) {
					this.onPressed();
					if (Util.getMeasuringTimeMs() - this.clickTime < 250L) {
						selectedSublanguage = this.languageCode;
					}

					this.clickTime = Util.getMeasuringTimeMs();
					return true;
				} else {
					this.clickTime = Util.getMeasuringTimeMs();
					return false;
				}
			}

			void onPressed() {
				SublanguageSelectionListWidget.this.setSelected(this);
			}

			public Text getNarration() {
				return Text.translatable("narrator.select", this.languageDefinition);
			}

		}
	}
}
