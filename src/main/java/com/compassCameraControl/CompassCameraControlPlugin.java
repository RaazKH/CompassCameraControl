package com.compassCameraControl;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.KeyEvent;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.HashTable;
import net.runelite.api.KeyCode;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.SoundEffectID;
import net.runelite.api.WidgetNode;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarClientID;
import net.runelite.api.vars.InputType;
import net.runelite.api.widgets.Widget;
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
	private static final int WEST_YAW = 4096;
	private static final int SOUTH_YAW = 8192;
	private static final int EAST_YAW = 12288;

	private static final Map<Character, Integer> directionMap = Map.of(
		'N', NORTH_YAW,
		'S', SOUTH_YAW,
		'E', EAST_YAW,
		'W', WEST_YAW
	);

	private static final String SNAP_FACING = "Snap Facing";
	private static final String SNAP_CARDINAL = "Snap Cardinal";
	private static final String CYCLE_CARDINAL = "Cycle Cardinal";
	private static final String SNAP_THEN_CYCLE = "Snap Then Cycle";
	private static final long HYBRID_CYCLE_TIMEOUT_MS = 2000L;

	private long lastHybridClickTimeMs;

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getOption().equals("Look North"))
		{
			if (config.shiftClickMode() != ShiftMode.OFF) {
				boolean shiftHeld = client.isKeyPressed(KeyCode.KC_SHIFT);
				if (config.shiftClickMode() == ShiftMode.ONSHIFT && !shiftHeld) return;
				if (config.shiftClickMode() == ShiftMode.OFFSHIFT && shiftHeld) return;
			}

			String newOption;
			switch (config.controlMode())
			{
				case SNAP_TO_FACING:
					newOption = SNAP_FACING;
					break;

				case SNAP_TO_CLOSEST:
					newOption = SNAP_CARDINAL;
					break;

				case SNAP_THEN_CYCLE:
					newOption = SNAP_THEN_CYCLE;
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
			case SNAP_FACING:
				facingYaw();
				client.playSoundEffect(SoundEffectID.UI_BOOP);
				break;

			case SNAP_CARDINAL:
				alignYaw();
				client.playSoundEffect(SoundEffectID.UI_BOOP);
				break;

			case SNAP_THEN_CYCLE:
				hybridSnapThenCycle();
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

	private int[] cycleOrderToYaws()
	{
		// Retain only "N", "E", "S", "W"
		String cycleOrder = config.cycleOrder().toUpperCase().replaceAll("[^NESW]", "")
			.chars()
			.distinct()
			.limit(4)
			.collect(StringBuilder::new,
				StringBuilder::appendCodePoint,
				StringBuilder::append)
			.toString();

		if (cycleOrder.isEmpty())
		{
			return new int[]{ NORTH_YAW, SOUTH_YAW, EAST_YAW, WEST_YAW };
		}

		return cycleOrder.chars()
			.map(c -> directionMap.get((char) c))
			.toArray();
	}

	private void cycleYaw()
	{
		// Overloaded method so cycleOrderToYaws isn't called twice when using hybridSnapThenCycle()
		cycleYaw(cycleOrderToYaws());
	}

	private void cycleYaw(int[] yawOrder)
	{
		int currentYaw = client.getCameraYaw();
		int nextYaw = yawOrder[0];

		for (int i = 0; i < yawOrder.length; i++)
		{
			if (yawOrder[i] == currentYaw)
			{
				nextYaw = yawOrder[(i + 1) % yawOrder.length];
				break;
			}
		}

		client.setCameraYawTarget(nextYaw);
	}

	private void alignYaw()
	{
		// Overloaded method so vanilla alignYaw users maintain the same functionality
		alignYaw(new int[]{ NORTH_YAW, SOUTH_YAW, EAST_YAW, WEST_YAW });
	}

	private void alignYaw(int[] yaws)
	{
		int currentYaw = client.getCameraYawTarget();
		int closestYaw = yaws[0];
		int diff = Math.abs(currentYaw - yaws[0]);
		int closestDistance = Math.min(diff, 16384 - diff);

		for (int i = 1; i < yaws.length; i++) {
			diff = Math.abs(currentYaw - yaws[i]);
			int distance = Math.min(diff, 16384 - diff);
			if (distance < closestDistance) {
				closestYaw = yaws[i];
				closestDistance = distance;
			}
		}

		client.setCameraYawTarget(closestYaw);
	}

	private void hybridSnapThenCycle()
	{
		long nowMs = System.currentTimeMillis();
		int currentYaw = client.getCameraYaw();
		int[] allowedYaws = cycleOrderToYaws();

		// Check if currentYaw is in the allowed cardinal set
		boolean isOnAllowedCardinal = false;
		for (int yaw : allowedYaws)
		{
			if (yaw == currentYaw)
			{
				isOnAllowedCardinal = true;
				break;
			}
		}

		if (isOnAllowedCardinal && (nowMs - lastHybridClickTimeMs) <= HYBRID_CYCLE_TIMEOUT_MS)
		{
			cycleYaw(allowedYaws);
		}
		else
		{
			alignYaw(allowedYaws);
		}

		lastHybridClickTimeMs = nowMs;
	}

	private void facingYaw()
	{
		if (client.getLocalPlayer() == null)
		{
			return;
		}

		// Multiply by 8 to get the yaw since below method returns range of 0 to 2047 and yaw was increased to 0 to 16383
		int playerOrientation = client.getLocalPlayer().getOrientation() * 8;
		int targetYaw;

		if (playerOrientation <= 8192) {
			targetYaw = 4096 * 2 - playerOrientation;
		} else {
			targetYaw = 12288 * 2 - playerOrientation;
		}

		client.setCameraYawTarget(targetYaw);
	}

	private static int degreesToYaw(int degrees) {
		return (int) Math.round(degrees * 16384.0 / 360.0);
	}

	private void rotateYaw(String s)
	{
		int currentYaw = client.getCameraYaw();
		int shift = s.equals("Flip") ? degreesToYaw(180) : degreesToYaw(config.rotateDegree());
		int targetYaw;

		if (config.rotateAfterSnap()) {
			alignYaw();
			// Above updates the yaw target in this tick, so use the target rather than the current yaw
			currentYaw = client.getCameraYawTarget();
		}

		if(s.equals("Clockwise")){
			targetYaw = currentYaw + shift;
		}
		else {
			targetYaw = currentYaw - shift;
		}

		// Wraps yaw into camera range [0,16384)
		targetYaw &= 16383;

		client.setCameraYawTarget(targetYaw);
	}

	private boolean shouldSuppressHotkeysForChat()
	{
		if (client.getVarcIntValue(VarClientID.MESLAYERMODE) != InputType.NONE.getType())
		{
			return true;
		}

		String chatInput = client.getVarcStrValue(VarClientID.CHATINPUT);
		if (chatInput != null && !chatInput.isEmpty())
		{
			return true;
		}

		return isChatboxCaretActive();
	}

	private boolean shouldSuppressHotkeysForInterfaces()
	{
		if (client.getFocusedInputFieldWidget() != null)
		{
			return true;
		}

		HashTable<WidgetNode> table = client.getComponentTable();
		try
		{
			if (table != null && (table.get(InterfaceID.ToplevelOsrsStretch.MAINMODAL) != null
				|| table.get(InterfaceID.ToplevelOsrsStretch.FLOATER) != null
				|| table.get(InterfaceID.ToplevelPreEoc.MAINMODAL) != null
				|| table.get(InterfaceID.ToplevelPreEoc.FLOATER) != null
				|| table.get(InterfaceID.Toplevel.MAINMODAL) != null
				|| table.get(InterfaceID.Toplevel.FLOATER) != null))
			{
				return true;
			}
		}
		catch (Exception e)
		{
			return false;
		}


		if (isWidgetHidden(InterfaceID.Chatbox.MES_LAYER_HIDE) || isWidgetHidden(InterfaceID.Chatbox.CHATDISPLAY))
		{
			return true;
		}

		return !isWidgetHidden(InterfaceID.Chatmenu.OPTIONS);
	}

	private boolean isWidgetHidden(int component)
	{
		Widget widget = client.getWidget(component);
		return widget == null || widget.isSelfHidden();
	}

	private boolean isChatboxCaretActive()
	{
		Widget chatboxInput = client.getWidget(InterfaceID.Chatbox.INPUT);
		if (chatboxInput != null && !chatboxInput.isSelfHidden())
		{
			String text = chatboxInput.getText();
			return text != null && text.contains("*");
		}

		return false;
	}

	
	private final KeyListener keyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent event) { }

		@Override
		public void keyReleased(KeyEvent event) { }

		@Override
		public void keyPressed(KeyEvent event) {
			if ((config.deprioritizeInChat() && shouldSuppressHotkeysForChat()) || (config.deprioritizeInInterfaces() && shouldSuppressHotkeysForInterfaces()))
			{
				return;
			}

			boolean handledEvent = true;
			if (config.snapFacingKey().matches(event)) {
				facingYaw();
			} else if (config.snapCloseKey().matches(event)) {
				alignYaw();
			} else if (config.cycleCardinalKey().matches(event)) {
				cycleYaw();
			} else if (config.snapThenCycleKey().matches(event)) {
				hybridSnapThenCycle();
			} else if (config.lookNorthKey().matches(event)) {
				client.setCameraYawTarget(NORTH_YAW);
			} else if (config.lookSouthKey().matches(event)) {
				client.setCameraYawTarget(SOUTH_YAW);
			} else if (config.lookEastKey().matches(event)) {
				client.setCameraYawTarget(EAST_YAW);
			} else if (config.lookWestKey().matches(event)) {
				client.setCameraYawTarget(WEST_YAW);
			} else if (config.rotateFlipKey().matches(event)) {
				rotateYaw("Flip");
			} else if (config.rotateClockwiseKey().matches(event)) {
				rotateYaw("Clockwise");
			} else if (config.rotateCounterclockwiseKey().matches(event)) {
				rotateYaw("Counterclockwise");
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
