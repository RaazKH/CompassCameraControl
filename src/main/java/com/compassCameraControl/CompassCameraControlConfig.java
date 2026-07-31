package com.compassCameraControl;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;
import java.awt.event.KeyEvent;

@ConfigGroup("compasscameracontrol")
public interface CompassCameraControlConfig extends Config
{
	@ConfigItem(
		keyName = "controlMode",
		name = "Mode",
		position = 1,
		description = "Cycle: North -> South -> East -> West (default)<br/>" +
			"Snap to Closest: Snaps the camera to the nearest cardinal direction<br/>" +
			"Snap to Facing: Snaps the camera to the player's facing direction<br/>" +
			"Snap then Cycle: Snaps to cardinal; re-click within 2 seconds to cycle"
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
		keyName = "snapFacingKey",
		name = "Snap Facing",
		description = "Snaps the camera to the player's facing direction on key press",
		position = 5,
		section = cardinalKeybindingSnap
	)
	default Keybind snapFacingKey() {
		return new Keybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "snapCloseKey",
		name = "Snap to Closest",
		description = "Snaps the camera to the nearest cardinal direction on key press",
		position = 6,
		section = cardinalKeybindingSnap
	)
	default Keybind snapCloseKey() {
		return new Keybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "cycleCardinalKey",
		name = "Cycle Cardinal",
		description = "Cycles through cardinal directions on key press",
		position = 7,
		section = cardinalKeybindingSnap
	)
	default Keybind cycleCardinalKey() {
		return new Keybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "snapThenCycleKey",
		name = "Snap then Cycle",
		position = 8,
		section = cardinalKeybindingSnap,
		description = "Snap to the closest allowed direction (within cycle order)<br/>" +
			"Quick re-press within 2 seconds cycles if already on an allowed direction"
	)
	default Keybind snapThenCycleKey() {
		return new Keybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "lookNorthKey",
		name = "Look North Key",
		description = "Face camera North on key press",
		position = 9,
		section = cardinalKeybindingSnap
	)
	default Keybind lookNorthKey() {
		return new Keybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "lookSouthKey",
		name = "Look South Key",
		description = "Face camera South on key press",
		position = 10,
		section = cardinalKeybindingSnap
	)
	default Keybind lookSouthKey() {
		return new Keybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "lookEastKey",
		name = "Look East Key",
		description = "Face camera East on key press",
		position = 11,
		section = cardinalKeybindingSnap
	)
	default Keybind lookEastKey() {
		return new Keybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "lookWestKey",
		name = "Look West Key",
		description = "Face camera West on key press",
		position = 12,
		section = cardinalKeybindingSnap
	)
	default Keybind lookWestKey() {
		return new Keybind(KeyEvent.VK_UNDEFINED, 0);
	}

	@ConfigItem(
		keyName = "rotateFlipKey",
		name = "Rotate 180° Key",
		description = "Rotates the camera to opposite side (always 180°)",
		position = 13,
		section = cardinalKeybindingSnap
	)
	default Keybind rotateFlipKey() { return new Keybind(KeyEvent.VK_UNDEFINED, 0);}


	@ConfigItem(
		keyName = "rotateClockwiseKey",
		name = "Rotate Clockwise Key",
		position = 14,
		section = cardinalKeybindingSnap,
		description = "Rotate camera a user defined number of degrees clockwise<br/>" +
				"Based on Rotation Degree"
	)
	default Keybind rotateClockwiseKey() { return new Keybind(KeyEvent.VK_UNDEFINED, 0);}

	@ConfigItem(
		keyName = "rotateCounterclockwiseKey",
		name = "Rotate Counterclockwise Key",
		position = 15,
		section = cardinalKeybindingSnap,
		description = "Rotate camera a user defined number of degrees counterclockwise<br/>" +
				"Based on Rotation Degree"
	)
	default Keybind rotateCounterclockwiseKey() { return new Keybind(KeyEvent.VK_UNDEFINED, 0);}

	@ConfigItem(
		keyName = "rotateAfterSnap",
		name = "Rotate After Snap",
		position = 16,
		description = "Snap to the closest cardinal direction before rotation<br/>" +
				"Applies to all rotation keybindings"
	)
	default boolean rotateAfterSnap()
	{
		return false;
	}

	@ConfigItem(
		keyName = "rotateDegree",
		name = "Rotation Degree",
		position = 17,
		description = "Rotation value for clockwise and counterclockwise rotation keybindings"
	)
	default int rotateDegree()
	{
		return 90;
	}

	@ConfigItem(
		keyName = "deprioritizeInInputContexts",
		name = "Disable Keybindings in Dialog/Input",
		position = 18,
		description = "Disable plugin keybindings while chat/dialog/interface input is focused<br/>" +
				"If the \"Key Remapping\" plugin is off, chat is focused and keybindings will not trigger"
	)
	default boolean deprioritizeInInputContexts()
	{
		return false;
	}
}
