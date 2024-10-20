package com.compassCameraControl;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;

@Slf4j
@PluginDescriptor(
	name = "Compass Camera Control"
)
public class CompassCameraControlPlugin extends Plugin
{
	@Inject
	private Client client;

	private static final int NORTH_YAW = 0;
	private static final int SOUTH_YAW = 1024;
	private static final int EAST_YAW = 512;
	private static final int WEST_YAW = 1536;

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event) {
		if (event.getMenuAction() == MenuAction.CC_OP && event.getMenuOption().equals("Look North")) {
			if (client.getCameraYaw() == NORTH_YAW) {
				client.setCameraYawTarget(SOUTH_YAW);
				event.consume();
			}
			if (client.getCameraYaw() == SOUTH_YAW) {
				client.setCameraYawTarget(EAST_YAW);
				event.consume();
			}
			if (client.getCameraYaw() == EAST_YAW) {
				client.setCameraYawTarget(WEST_YAW);
				event.consume();
			}
		}
	}
}
