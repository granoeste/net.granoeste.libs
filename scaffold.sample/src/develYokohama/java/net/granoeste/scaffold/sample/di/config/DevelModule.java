
package net.granoeste.scaffold.sample.di.config;

import net.granoeste.commons.util.LogUtils;
import net.granoeste.scaffold.di.config.BaseModule;
import net.granoeste.scaffold.sample.logic.Weather;
import net.granoeste.scaffold.sample.logic.impl.devel.WeatherLogicYokohama;

public class DevelModule extends BaseModule {
	private static final String TAG = LogUtils.makeLogTag(DevelModule.class);

	@Override
	protected void configure() {
		bind(Weather.class).to(WeatherLogicYokohama.class);
	}
}
