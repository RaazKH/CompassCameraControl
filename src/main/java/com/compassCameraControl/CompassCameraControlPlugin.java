package com.compassCameraControl;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.SoundEffectID;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Compass Camera Control",
	description = "Expands compass functionality",
	tags = {"camera, compass, control, navigation, usability, convenience"}
)
public class CompassCameraControlPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private CompassCameraControlConfig config;

	private static final int NORTH_YAW = 0;
	private static final int SOUTH_YAW = 1024;
	private static final int EAST_YAW = 512;
	private static final int WEST_YAW = 1536;

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuAction() == MenuAction.CC_OP && event.getMenuOption().equals("Look North"))
		{
			switch (config.controlMode())
			{
				case SNAP_TO_CLOSEST:
					alignYaw();
					break;

				case CYCLE:
				default:
					cycleYaw();
					break;
			}
			event.consume();
			client.playSoundEffect(SoundEffectID.UI_BOOP);
		}
	}

	@Provides
	@Singleton
	CompassCameraControlConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CompassCameraControlConfig.class);
	}

	private void cycleYaw()
	{
		switch (client.getCameraYaw())
		{
			case NORTH_YAW:
				client.setCameraYawTarget(SOUTH_YAW);
				break;

			case SOUTH_YAW:
				client.setCameraYawTarget(EAST_YAW);
				break;

			case EAST_YAW:
				client.setCameraYawTarget(WEST_YAW);
				break;

			default:
				client.setCameraYawTarget(NORTH_YAW);
		}
	}

	private void alignYaw()
	{
		int dNorth = Math.min(
			Math.abs(client.getCameraYawTarget() - NORTH_YAW), // north-east quadrant
			Math.abs(client.getCameraYawTarget() - (NORTH_YAW + 2048)) // north-west quadrant
		);
		int closestYaw = NORTH_YAW;
		int closestYawDistance = dNorth;

		int dSouth = Math.abs(client.getCameraYawTarget() - SOUTH_YAW);
		if (dSouth < closestYawDistance)
		{
			closestYaw = SOUTH_YAW;
			closestYawDistance = dSouth;
		}

		int dEast = Math.abs(client.getCameraYawTarget() - EAST_YAW);
		if (dEast < closestYawDistance)
		{
			closestYaw = EAST_YAW;
			closestYawDistance = dEast;
		}

		int dWest = Math.abs(client.getCameraYawTarget() - WEST_YAW);
		if (dWest < closestYawDistance)
		{
			closestYaw = WEST_YAW;
			closestYawDistance = dWest;
		}

		client.setCameraYawTarget(closestYaw);
	}
}
