package com.compassCameraControl;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CompassCameraControlPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CompassCameraControlPlugin.class);
		RuneLite.main(args);
	}
}