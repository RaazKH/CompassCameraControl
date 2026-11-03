package com.compassCameraControl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CtrlClickMode
{
	OFF("Off"),
	ONCONTROL("On Ctrl"),
	OFFCONTROL("Off Ctrl"),
	;
	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}