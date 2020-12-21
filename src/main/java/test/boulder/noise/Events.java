package test.boulder.noise;

import com.simsilica.event.EventType;

interface Events {

	EventType<Boolean> toggleWireframe = EventType.create("toggleWireframe", Boolean.class);
	EventType<SphereSettings> sphereSettings = EventType.create("sphereSettings", SphereSettings.class);

}
