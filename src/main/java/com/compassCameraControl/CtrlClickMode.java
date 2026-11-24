package com.compassCameraControl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CtrlClickMode
{
	OFF("Off"),
	ONCONTROL("On Ctrl"),
	OFFCONTROL("Off Ctrl"),
	ONCONTROL_REVERSE("On Ctrl Reverse Cycle"),
	;
	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}