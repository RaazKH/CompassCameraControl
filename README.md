# Compass Camera Control
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/RaazKH/CompassCameraControl?include_prereleases&logo=github)
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
- **Look North Key**: Face camera North when pressed
- **Look South Key**: Face camera South when pressed
- **Look East Key**: Face camera East when pressed
- **Look West Key**: Face camera West when pressed

---
### Custom Cycle Order
You can specify a custom order for cycling through directions. For example, if you prefer to cycle between North and South, you can set the cycle order to `N,S`. Only `N`, `S`, `W`, and `E` are valid characters for this field, all other characters will be ignored.

This field is **not** case-sensitive. Non-unique cardinal directions will be ignored. So `N,S,E,S` would cycle through `N,S,E`, the final `S` being a duplicate and thus is ignored.

---
#### Issues and Feedback
If you encounter any bugs, have suggestions for improvements, or would like to give feedback, please feel free to <a href="https://github.com/RaazKH/CompassCameraControl/issues">submit an issue</a>.

---
#### Contributors
Thanks to [LlemonDuck](https://github.com/LlemonDuck) and [insizhen](https://github.com/insizhen) for their valuable code contributions.