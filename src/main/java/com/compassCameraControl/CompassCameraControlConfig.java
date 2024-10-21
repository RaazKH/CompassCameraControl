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
		description = "Cycle: North -> South -> East -> West<br/>" +
			"Snap to Closest: Snaps the camera to the nearest cardinal direction"
	)
	default ControlMode controlMode()
	{
		return ControlMode.CYCLE;
	}

}
