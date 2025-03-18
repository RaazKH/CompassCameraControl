package com.compassCameraControl;

import net.runelite.client.config.*;
import java.awt.event.KeyEvent;

@ConfigGroup("compasscameracontrol")
public interface CompassCameraControlConfig extends Config
{
	@ConfigItem(
		keyName = "controlMode",
		name = "Mode",
		position = 1,
		description = "Cycle: North -> South -> East -> West (default)<br/>" +
			"Snap to Closest: Snaps the camera to the nearest cardinal direction"
	)
	default ControlMode controlMode()
	{
		return ControlMode.CYCLE;
	}

	@ConfigItem(
		keyName = "cycleOrder",
		name = "Cycle Order",
		position = 2,
		description = "Example 1: N-S-E-W<br/>" +
			"Example 2: S-N-E<br/>" +
			"Example 3: N-S"
	)
	default String cycleOrder()
	{
		return "N,S,E,W";
	}

	@ConfigItem(
		keyName = "shiftClickMode",
		name = "Shift-Click",
		position = 3,
		description = "Off: Always active, ignores Shift<br/>" +
			"On Shift: Only works when Shift is held<br/>" +
			"Off Shift: Only works when Shift is not held"
	)
	default ShiftMode shiftClickMode()
	{
		return ShiftMode.OFF;
	}

	@ConfigSection(
		name = "Keybindings",
		position = 4,
		closedByDefault = true,
		description = "Settings for snapping the camera to cardinal directions<br/>" +
			"To prevent these keys from appearing in chat<br/>" +
			"Enable the \"Key Remapping\" plugin (included with RuneLite)"
	)
	String cardinalKeybindingSnap = "cardinalKeybindingSnap";

	@ConfigItem(
			keyName = "snapCloseKey",
			name = "Snap to Closest",
			description = "Snaps the camera to the nearest cardinal direction on key press",
			position = 5,
			section = cardinalKeybindingSnap
	)
	default ModifierlessKeybind snapCloseKey() {
		return new ModifierlessKeybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
			keyName = "cycleCardinalKey",
			name = "Cycle Cardinal",
			description = "Cycles through cardinal directions on key press",
			position = 6,
			section = cardinalKeybindingSnap
	)
	default ModifierlessKeybind cycleCardinalKey() {
		return new ModifierlessKeybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "lookNorthKey",
		name = "Look North Key",
		description = "Face camera North on key press",
		position = 7,
		section = cardinalKeybindingSnap
	)
	default ModifierlessKeybind lookNorthKey() {
		return new ModifierlessKeybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "lookSouthKey",
		name = "Look South Key",
		description = "Face camera South on key press",
		position = 8,
		section = cardinalKeybindingSnap
	)
	default ModifierlessKeybind lookSouthKey() {
		return new ModifierlessKeybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "lookEastKey",
		name = "Look East Key",
		description = "Face camera East on key press",
		position = 9,
		section = cardinalKeybindingSnap
	)
	default ModifierlessKeybind lookEastKey() {
		return new ModifierlessKeybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "lookWestKey",
		name = "Look West Key",
		description = "Face camera West on key press",
		position = 10,
		section = cardinalKeybindingSnap
	)
	default ModifierlessKeybind lookWestKey() {
		return new ModifierlessKeybind(KeyEvent.VK_UNDEFINED, 0);
	}
}
