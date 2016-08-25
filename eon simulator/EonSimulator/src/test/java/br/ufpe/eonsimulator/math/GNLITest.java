package br.ufpe.eonsimulator.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import br.ufpe.simulator.math.MathUtils;
import br.ufpe.simulator.math.functions.GNLI;
import br.ufpe.simulator.math.functions.GNLIFactory;
import br.ufpe.simulator.math.functions.GNLIFactory.GNLILinkProperties;
import br.ufpe.simulator.utils.ConvertUtils;
import br.ufpe.simulator.utils.PropertiesUtils;

public class GNLITest {
	private static final String SIMULATION_PROPERTIES = "src/main/resources/simulation.properties";

	@Test
	public void test() {
		Properties properties = PropertiesUtils
				.createPropertiesFromResource(SIMULATION_PROPERTIES);
		GNLIFactory.setGnliProperties(properties);
		double power = 1 * Math.pow(10, -3);
		List<Double> powerList = new ArrayList<Double>();
		powerList.add(power);
		GNLI gnli = GNLIFactory.createGnli(new GNLILinkProperties() {

			@Override
			public List<Integer> getOccupancyList() {
				List<Integer> integers = new ArrayList<Integer>();
				integers.add(1);
				integers.add(1);
				integers.add(1);
				integers.add(2);
				integers.add(0);
				integers.add(3);
				integers.add(3);
				return integers;
			}
		}, new GNLIFactory.GNLIPhysicalProperties() {

			@Override
			public double getLs() {
				return 50;
			}

			@Override
			public double getAlfa() {
				return MathUtils.convertToAlfaLinear(0.2);
			}
		}, powerList);

		double pass = Math.pow(10, 9);
		for (double i = -0.9 * Math.pow(10, 12); i < 0.9 * Math.pow(10, 12); i = i
				+ pass) {
			System.out.println(ConvertUtils.convertToLocaleString(gnli.value(i)));
		}
	}
}
