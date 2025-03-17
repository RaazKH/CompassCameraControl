package com.compassCameraControl;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

	@ConfigItem(
			keyName = "lookNorthKey",
			name = "Look North Key",
			description = "Keybind for Look North",
			position = 4
	)
	default Keybind lookNorthKey() {
		return new Keybind(KeyEvent.VK_UP, 0);
	}

	@ConfigItem(
			keyName = "lookSouthKey",
			name = "Look South Key",
			description = "Keybind for Look South",
			position = 5
	)
	default Keybind lookSouthKey() {
		return new Keybind(KeyEvent.VK_DOWN, 0);
	}

	@ConfigItem(
			keyName = "lookEastKey",
			name = "Look East Key",
			description = "Keybind for Look East",
			position = 6
	)
	default Keybind lookEastKey() {
		return new Keybind(KeyEvent.VK_RIGHT, 0);
	}

	@ConfigItem(
			keyName = "lookWestKey",
			name = "Look West Key",
			description = "Keybind for Look West",
			position = 7
	)
	default Keybind lookWestKey() {
		return new Keybind(KeyEvent.VK_LEFT, 0);
	}
}
