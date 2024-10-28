# Compass Camera Control
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/RaazKH/CompassCameraControl?include_prereleases&logo=github)

A RuneLite plugin that allows you to control the camera direction by simply clicking the compass orb, cycling through cardinal directions or snapping to the closest one.

## Configuration

This plugin is simple and has two modes:

#### Cycle Mode (default)

1. If facing North, the camera will rotate to South.
2. If facing South, it will rotate to East.
3. If facing East, it will rotate to West.
4. If facing any other direction, it will rotate North.

The process repeats when clicking the compass orb.

#### Snap to Closest

When clicking the compass, the camera will snap to the closest cardinal direction. This is useful for spinning the camera manually and then aligning to the grid.

---

### Shift-Click Option

You can configure how the plugin responds to clicks on the compass:

- **Off**: Always active, ignoring the Shift key.
- **On Shift**: Only works when Shift is held down.
- **Off Shift**: Works only when Shift is not held down.

## Issues and Feedback

If you encounter any bugs, have suggestions for improvements, or would like to give feedback, please feel free to <a href="https://github.com/RaazKH/CompassCameraControl/issues">submit an issue</a>.
