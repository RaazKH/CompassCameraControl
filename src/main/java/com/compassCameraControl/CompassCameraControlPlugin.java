package com.compassCameraControl;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.KeyEvent;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.KeyCode;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.SoundEffectID;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
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

	@Inject
	private KeyManager keyManager;


	private static final int NORTH_YAW = 0;
	private static final int SOUTH_YAW = 1024;
	private static final int EAST_YAW = 1536;
	private static final int WEST_YAW = 512;

	private static final Map<Character, Integer> directionMap = Map.of(
		'N', NORTH_YAW,
		'S', SOUTH_YAW,
		'E', EAST_YAW,
		'W', WEST_YAW
	);

	private static final String SNAP_CARDINAL = "Snap Cardinal";
	private static final String CYCLE_CARDINAL = "Cycle Cardinal";

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getOption().equals("Look North"))
		{
			if (config.shiftClickMode() != ShiftMode.OFF &&
					((config.shiftClickMode() == ShiftMode.ONSHIFT && !client.isKeyPressed(KeyCode.KC_SHIFT)) ||
							(config.shiftClickMode() == ShiftMode.OFFSHIFT && client.isKeyPressed(KeyCode.KC_SHIFT))))
			{ return; }


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

			client.getMenu()
				.createMenuEntry(-1)
				.setType(MenuAction.RUNELITE_HIGH_PRIORITY)
				.setOption(newOption)
				.onClick(this::onCompassAction);
		}
	}

	public void onCompassAction(MenuEntry event)
	{
		switch (event.getOption())
		{
			case SNAP_CARDINAL:
				alignYaw();
				client.playSoundEffect(SoundEffectID.UI_BOOP);
				break;

			case CYCLE_CARDINAL:
				cycleYaw();
				client.playSoundEffect(SoundEffectID.UI_BOOP);
				break;
		}
	}

	@Provides
	@Singleton
	CompassCameraControlConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CompassCameraControlConfig.class);
	}

	private String getValidatedCycleOrder()
	{
		// Retain only "N", "E", "S", "W"
		return config.cycleOrder().toUpperCase().replaceAll("[^NESW]", "")
			.chars()
			.distinct()
			.limit(4)
			.collect(StringBuilder::new,
				StringBuilder::appendCodePoint,
				StringBuilder::append)
			.toString();
	}

	private void cycleYaw()
	{
		String cycleOrder = getValidatedCycleOrder();
		int[] yawOrder = new int[cycleOrder.length()];

		int count = 0;
		for (char direction : cycleOrder.toCharArray())
		{
			Integer yaw = directionMap.get(direction);
			if (yaw != null)
			{
				yawOrder[count++] = yaw;
			}
		}

		if (count == 0)
		{
			yawOrder = new int[]{ NORTH_YAW, SOUTH_YAW, EAST_YAW, WEST_YAW };
			count = 4;
		}

		int currentYaw = client.getCameraYaw();
		int nextYaw = yawOrder[0];

		for (int i = 0; i < count; i++)
		{
			if (yawOrder[i] == currentYaw)
			{
				nextYaw = yawOrder[(i + 1) % count];
				break;
			}
		}

		client.setCameraYawTarget(nextYaw);
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

	private final KeyListener keyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent event) { }

		@Override
		public void keyReleased(KeyEvent event) { }

		@Override
		public void keyPressed(KeyEvent event) {
			boolean handledEvent = true;

			if (config.snapCloseKey().matches(event)) {
				alignYaw();
			} else if (config.cycleCardinalKey().matches(event)) {
				cycleYaw();
			} else if (config.lookNorthKey().matches(event)) {
				client.setCameraYawTarget(NORTH_YAW);
			} else if (config.lookSouthKey().matches(event)) {
				client.setCameraYawTarget(SOUTH_YAW);
			} else if (config.lookEastKey().matches(event)) {
				client.setCameraYawTarget(EAST_YAW);}
			else if (config.lookWestKey().matches(event)) {
				client.setCameraYawTarget(WEST_YAW);
			} else {
				handledEvent = false;
			}

			if (handledEvent) {
				event.consume();
			}
		}
	};

	@Override
	protected void startUp() throws Exception {
		keyManager.registerKeyListener(keyListener);
	}

	@Override
	protected void shutDown() throws Exception {
		keyManager.unregisterKeyListener(keyListener);
	}
}
