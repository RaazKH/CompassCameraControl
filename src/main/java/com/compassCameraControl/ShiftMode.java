package com.compassCameraControl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShiftMode
{

	OFF("Off"),
	ONSHIFT("On Shift"),
	OFFSHIFT("Off Shift"),
	;

	private final String name;

	@Override
	public String toString()
	{
		// for config option dropdown to show proper capitalization
		return name;
	}
}
