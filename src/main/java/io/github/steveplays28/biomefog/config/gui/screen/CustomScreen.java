package io.github.steveplays28.biomefog.config.gui.screen;

import io.github.steveplays28.biomefog.config.gui.widget.CustomWidget;
import io.github.steveplays28.biomefog.config.gui.widget.SelectableCustomWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CustomScreen extends Screen {
	public static final int WHITE_COLOR = 16777215;

	public final Text title;
	public final Screen parent;

	protected CustomScreen(Text title, Screen parent) {
		super(title);
		this.title = title;
		this.parent = parent;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Element childElement : this.children()) {
			childElement.mouseClicked(mouseX, mouseY, button);

			if (childElement instanceof CustomWidget childCustomWidget) {
				mouseClickedRecursive(childCustomWidget, mouseX, mouseY, button);
			}

			if (childElement.isMouseOver(mouseX, mouseY)) {
				if (button == 0) {
					this.setDragging(true);
				}

				setFocused(childElement);
			}
		}

		return true;
	}

	protected void mouseClickedRecursive(@NotNull CustomWidget customWidget, double mouseX, double mouseY, int button) {
		for (CustomWidget childCustomWidget : customWidget.getChildWidgets()) {
			childCustomWidget.mouseClicked(mouseX, mouseY, button);

			// TODO: Move to widget code so widgets can work in any screen
			if (childCustomWidget instanceof SelectableCustomWidget selectableCustomChildWidget) {
				if (selectableCustomChildWidget.isMouseOver(mouseX, mouseY)) {
					setFocused(selectableCustomChildWidget);

					if (button == 0) {
						setDragging(true);
					}
				}
			}

			mouseClickedRecursive(childCustomWidget, mouseX, mouseY, button);
		}
	}

	@Override
	public void setFocused(@Nullable Element focused) {
		if (getFocused() instanceof SelectableCustomWidget focusedSelectableCustomWidget) {
//			focusedSelectableCustomWidget.setFocused(false);
		}

		if (focused instanceof SelectableCustomWidget selectableCustomWidget) {
			selectableCustomWidget.setFocused(true);
		}

		super.setFocused(focused);
	}

	@Override
	public boolean changeFocus(boolean lookForwards) {
		// TODO: Implement
		return super.changeFocus(lookForwards);
	}

	@Override
	public void close() {
		if (client == null) return;

		client.setScreen(parent);
	}

	public Text getTitle() {
		return title;
	}
}
