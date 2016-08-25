package br.ufpe.eonsimulator.math;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.ufpe.simulator.math.functions.GNLIFactory;
import br.ufpe.simulator.math.functions.GNLIFactory.TSChannels;
import br.ufpe.simulator.math.functions.GWDM;
import br.ufpe.simulator.utils.ConvertUtils;

public class GWDMTest {

	@Test
	public void test() {
		double rollOff = 0.3;
		double channelsSpacing = 50 * Math.pow(10, 9);
		List<Integer> integers = new ArrayList<Integer>();
		integers.add(1);
		integers.add(1);
		integers.add(1);
		integers.add(2);
		integers.add(0);
		integers.add(3);
		integers.add(3);
		double slotWidth = 16e9;
		TSChannels tsChannels = GNLIFactory.createTsChannels(integers,
				slotWidth);
		GWDM channels = new GWDM(tsChannels.getnChannels(), channelsSpacing,
				tsChannels.getTsList(), rollOff);
		double pass = Math.pow(10, 9);
		for (double i = -0.9 * Math.pow(10, 12); i < 0.9 * Math.pow(10, 12); i = i
				+ pass) {
			System.out.println(ConvertUtils.convertToLocaleString(channels.value(i)));
		}
	}
}
