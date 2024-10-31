package com.compassCameraControl;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

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
}
