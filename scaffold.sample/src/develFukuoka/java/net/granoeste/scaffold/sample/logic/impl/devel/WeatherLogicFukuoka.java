
package net.granoeste.scaffold.sample.logic.impl.devel;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import net.granoeste.scaffold.sample.logic.Weather;

public class WeatherLogicFukuoka implements Weather {
	@Inject
	@Named("forecast")
	private String forecast;

	@Override
	public String forecast() {
		return forecast;
	}

	@Override
	public void prev() {
		forecast = "モック 福岡の天気は晴れ時々雨";
	}

	@Override
	public void next() {
		forecast = "モック 福岡の天気は雨のち晴れ";
	}
}
