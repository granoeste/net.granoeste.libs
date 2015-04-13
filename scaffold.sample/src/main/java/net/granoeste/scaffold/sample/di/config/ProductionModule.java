
package net.granoeste.scaffold.sample.di.config;


import net.granoeste.scaffold.di.config.BaseModule;
import net.granoeste.scaffold.sample.logic.Weather;
import net.granoeste.scaffold.sample.logic.impl.WeatherLogic;

public class ProductionModule extends BaseModule {
	@SuppressWarnings("unused")
	private static final String TAG = ProductionModule.class.getSimpleName();
	private final ProductionModule self = this;

	@Override
	protected void configure() {
		bind(Weather.class).to(WeatherLogic.class);
	}
}
