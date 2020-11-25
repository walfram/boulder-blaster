package test.boulder;

import com.simsilica.event.EventType;

interface Events {

	EventType<Object> toggleWireframe = EventType.create("toggleWireframe", Object.class);
	EventType<NoiseSettings> noiseSettingsChange = EventType.create("noiseSettingsChange", NoiseSettings.class);

}
