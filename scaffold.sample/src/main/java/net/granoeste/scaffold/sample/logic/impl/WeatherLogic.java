
package net.granoeste.scaffold.sample.logic.impl;

import net.granoeste.scaffold.sample.logic.Weather;

public class WeatherLogic implements Weather {
	private String forecast = "東京の天気は曇り";

	@Override
	public String forecast() {
		return forecast;
	}

	@Override
	public void prev() {
		forecast = "東京の天気は雨";
	}

	@Override
	public void next() {
		forecast = "東京の天気は雪";
	}
}
