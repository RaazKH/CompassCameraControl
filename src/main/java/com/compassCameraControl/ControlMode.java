package com.compassCameraControl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ControlMode
{

	CYCLE("Cycle"),
	SNAP_TO_CLOSEST("Snap to Closest"),
	SNAP_TO_FACING("Snap to Facing"),
	;

	private final String name;

	@Override
	public String toString()
	{
		// for config option dropdown to show proper capitalization
		return name;
	}
}
