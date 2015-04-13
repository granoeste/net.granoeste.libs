
package net.granoeste.scaffold.sample.logic.impl.devel;

import net.granoeste.scaffold.sample.logic.Weather;

public class WeatherLogicSaitama implements Weather {
	private String forecast = "モック 埼玉の天気は晴れ";

	@Override
	public String forecast() {
		return forecast;
	}

	@Override
	public void prev() {
		forecast = "モック 埼玉の天気は晴れ";
	}

	@Override
	public void next() {
		forecast = "モック 埼玉の天気は曇り";
	}
}
