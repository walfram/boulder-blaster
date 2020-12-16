package test.boulder.noise;

import com.simsilica.event.EventType;

interface Events {

	EventType<Object> toggleWireframe = EventType.create("toggleWireframe", Object.class);
	EventType<SphereSettings> sphereSettings = EventType.create("sphereSettings", SphereSettings.class);

}
