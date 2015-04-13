
package net.granoeste.scaffold.sample.logic.impl.devel;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import net.granoeste.scaffold.sample.logic.Weather;

public class WeatherLogic implements Weather {
	@Inject
	@Named("forecast")
	private String forecast;

	@Override
	public String forecast() {
		return forecast;
	}

	@Override
	public void prev() {
		forecast = "モック 大阪の天気は晴れ時々ブタ";
	}

	@Override
	public void next() {
		forecast = "モック 大阪の天気は曇りのち雨";
	}
}
