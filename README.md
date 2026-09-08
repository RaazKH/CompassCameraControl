# Compass Camera Control
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/RaazKH/CompassCameraControl?include_prereleases&logo=github)
[![Active Installs](http://img.shields.io/endpoint?url=https://api.runelite.net/pluginhub/shields/installs/plugin/compass-camera-control)](https://runelite.net/plugin-hub/show/compass-camera-control)

A RuneLite plugin that allows you to control the camera direction by either clicking the compass orb or using customizable keybindings.

## Compass Orb Control
You can cycle through cardinal directions, snap to the closest one, or snap to your character's facing direction by clicking on the compass orb.

### Cycle Mode (default)
1. If facing North, the camera will rotate to South.
2. If facing South, it will rotate to East.
3. If facing East, it will rotate to West.
4. If facing any other direction, it will rotate North.

The process repeats when clicking the compass orb.

### Snap to Closest Mode
When clicking the compass, the camera will snap to the closest cardinal direction.
Useful for spinning the camera manually, then aligning to the grid.

### Snap to Facing Mode
When clicking the compass, the camera will snap to face the same direction as your character.

### Snap Then Cycle Mode
When clicking the compass, the camera will snap to the closest **allowed** direction (from cycle order field).
If you click again within 2 seconds while already on an allowed direction, it will cycle through allowed directions.
If you wait longer than 2 seconds, the next click re-snaps instead of cycling.
For example, with cycle order "N,S": First click from South-East snaps to South. A quick second click cycles to North. If you wait a few seconds, a click re-centers instead of cycling.

### Shift-Click Option
You can configure how the plugin responds to clicks on the compass:
- **Off**: Always active, ignoring the Shift key.
- **On Shift**: Only works when Shift is held down.
- **Off Shift**: Works only when Shift is not held down.

## Keybindings
You can set up shortcuts for quick camera control. To prevent shortcuts from appearing in the chat, enable the "Key Remapping" plugin (included with RuneLite).

- **Snap to Facing**: Snap camera to your character's facing direction when pressed
- **Snap to Closest**: Snap camera to the closest cardinal direction when pressed
- **Cycle Cardinal**: Cycle through your custom cycle order of directions when pressed
- **Snap Then Cycle**: Snap to closest allowed direction; quick re-press (within 2 seconds) cycles
- **Look North Key**: Face camera North when pressed
- **Look South Key**: Face camera South when pressed
- **Look East Key**: Face camera East when pressed
- **Look West Key**: Face camera West when pressed
- **Rotate 180°**: Rotates the camera to opposite side (always 180°)
- **Rotate Clockwise**: Rotates camera clockwise
- **Rotate Counterclockwise**: Rotates camera counterclockwise

### Keybind Options
- **Rotate After Snap**: Snap to the closest cardinal direction before rotation, applies to all rotation keybindings
- **Rotation Value**: Set rotation value for clockwise and counterclockwise rotation keybindings
- **Disable Keybindings in Chat**: Disables plugin keybinds while typing in chat. If "Disable Keybindings in Chat" is enabled and the "Key Remapping" plugin is off, chat is focused by default so plugin keybinds will not trigger.
- **Disable Keybindings in Interfaces**: Disables plugin keybinds while non-chat interfaces are capturing keyboard input
---
### Custom Cycle Order
You can specify a custom order for cycling through directions. For example, if you prefer to cycle between North and South, you can set the cycle order to `N,S`. Only `N`, `S`, `W`, and `E` are valid characters for this field, all other characters will be ignored.

This field is **not** case-sensitive. Non-unique cardinal directions will be ignored. So `N,S,E,S` would cycle through `N,S,E`, the final `S` being a duplicate and thus is ignored.

---
#### Issues and Feedback
If you encounter any bugs, have suggestions for improvements, or would like to give feedback, please feel free to <a href="https://github.com/RaazKH/CompassCameraControl/issues">submit an issue</a>.

---
#### Contributors
Thanks to [LlemonDuck](https://github.com/LlemonDuck), [insizhen](https://github.com/insizhen), and [Scarcy](https://github.com/Scarcy) for their valuable code contributions.
