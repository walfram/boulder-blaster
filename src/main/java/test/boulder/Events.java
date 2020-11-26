package test.boulder;

import com.simsilica.event.EventType;

interface Events {

	EventType<Object> toggleWireframe = EventType.create("toggleWireframe", Object.class);
	EventType<NoiseSettings> noiseChange = EventType.create("noiseChange", NoiseSettings.class);

}
