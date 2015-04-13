
package net.granoeste.scaffold.sample.logic.impl.devel;

import net.granoeste.scaffold.sample.logic.Weather;

public class WeatherLogicYokohama implements Weather {
	private String forecast = "モック 横浜の天気は晴れ";

	@Override
	public String forecast() {
		return forecast;
	}

	@Override
	public void prev() {
		forecast = "モック 横浜の天気は晴れ";
	}

	@Override
	public void next() {
		forecast = "モック 横浜の天気は曇り";
	}
}
