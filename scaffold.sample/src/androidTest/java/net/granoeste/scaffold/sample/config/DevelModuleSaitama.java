
package net.granoeste.scaffold.sample.config;

import net.granoeste.commons.util.LogUtils;
import net.granoeste.scaffold.di.config.BaseModule;
import net.granoeste.scaffold.sample.devel.WeatherLogicSaitama;
import net.granoeste.scaffold.sample.logic.Weather;

public class DevelModuleSaitama extends BaseModule {
	private static final String TAG = LogUtils.makeLogTag(DevelModuleSaitama.class);

	@Override
	protected void configure() {
		bind(Weather.class).to((Class<? extends Weather>) WeatherLogicSaitama.class);
	}
}
