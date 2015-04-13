
package net.granoeste.scaffold.sample.di.config;

import net.granoeste.commons.util.LogUtils;
import net.granoeste.scaffold.di.config.BaseModule;
import net.granoeste.scaffold.sample.logic.Weather;
import net.granoeste.scaffold.sample.logic.impl.devel.WeatherLogicFukuoka;

import android.util.Log;

import com.google.inject.name.Names;

public class DevelModule extends BaseModule {
	private static final String TAG = LogUtils.makeLogTag(DevelModule.class);

	@Override
	protected void configure() {
		Log.v(TAG, "configure()");
		bind(Weather.class).to(WeatherLogicFukuoka.class);
		bindConstant().annotatedWith(Names.named("forecast")).to("福岡の天気は雨");
	}
}
