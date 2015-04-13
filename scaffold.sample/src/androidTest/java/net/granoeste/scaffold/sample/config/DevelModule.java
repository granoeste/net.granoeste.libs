
package net.granoeste.scaffold.sample.config;

import android.util.Log;

import com.google.inject.name.Names;

import net.granoeste.commons.util.LogUtils;
import net.granoeste.scaffold.di.config.BaseModule;
import net.granoeste.scaffold.sample.devel.WeatherLogicFukuoka;
import net.granoeste.scaffold.sample.logic.Weather;

public class DevelModule extends BaseModule {
	private static final String TAG = LogUtils.makeLogTag(DevelModule.class);

	@Override
	protected void configure() {
		Log.v(TAG, "configure()");
		bind(Weather.class).to((Class<? extends Weather>) WeatherLogicFukuoka.class);
		bindConstant().annotatedWith(Names.named("forecast")).to("モック 大阪の天気は晴れのち曇り");
	}
}
