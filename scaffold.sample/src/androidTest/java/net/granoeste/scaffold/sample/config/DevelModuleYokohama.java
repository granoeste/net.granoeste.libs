
package net.granoeste.scaffold.sample.config;

import net.granoeste.commons.util.LogUtils;
import net.granoeste.scaffold.di.config.BaseModule;
import net.granoeste.scaffold.sample.devel.WeatherLogicYokohama;
import net.granoeste.scaffold.sample.logic.Weather;

public class DevelModuleYokohama extends BaseModule {
	private static final String TAG = LogUtils.makeLogTag(DevelModuleYokohama.class);

	@Override
	protected void configure() {
		bind(Weather.class).to((Class<? extends Weather>) WeatherLogicYokohama.class);
	}
}
