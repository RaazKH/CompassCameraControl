package com.compassCameraControl;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.SoundEffectID;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.MenuEntry;
import org.apache.commons.lang3.ArrayUtils;

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

	private static final String SNAP_CARDINAL = "Snap Cardinal";
	private static final String CYCLE_CARDINAL = "Cycle Cardinal";

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getOption().equals("Look North"))
		{
			String newOption;
			switch (config.controlMode())
			{
				case SNAP_TO_CLOSEST:
					newOption = SNAP_CARDINAL;
					break;

				case CYCLE:
				default:
					newOption = CYCLE_CARDINAL;
					break;
			}

			MenuEntry[] menuEntries = client.getMenu().getMenuEntries();
			MenuEntry newEntry = client.getMenu().createMenuEntry(menuEntries.length - 1)
					.setOption(newOption)
					.setType(MenuAction.CC_OP);

			menuEntries = ArrayUtils.add(menuEntries, newEntry);
			client.getMenu().setMenuEntries(menuEntries);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuAction() == MenuAction.CC_OP)
		{
			switch (event.getMenuOption())
			{
				case SNAP_CARDINAL:
					alignYaw();
					event.consume();
					client.playSoundEffect(SoundEffectID.UI_BOOP);
					break;

				case CYCLE_CARDINAL:
					cycleYaw();
					event.consume();
					client.playSoundEffect(SoundEffectID.UI_BOOP);
					break;
			}
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
		}

		client.setCameraYawTarget(closestYaw);
	}
}
