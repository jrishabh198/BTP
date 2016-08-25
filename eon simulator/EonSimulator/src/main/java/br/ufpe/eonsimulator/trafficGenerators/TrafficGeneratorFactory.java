package br.ufpe.eonsimulator.trafficGenerators;

import java.util.List;
import java.util.Properties;

import br.ufpe.eonsimulator.trafficGenerators.arrivalTimeGenerators.ConstantArrivalTime;
import br.ufpe.eonsimulator.trafficGenerators.arrivalTimeGenerators.ExponentialArrivalTime;
import br.ufpe.eonsimulator.trafficGenerators.arrivalTimeGenerators.IsArrivalTimeGenerator;
import br.ufpe.eonsimulator.trafficGenerators.bitRateGenerators.ConstantTraffic;
import br.ufpe.eonsimulator.trafficGenerators.bitRateGenerators.IsBitRateGenerator;
import br.ufpe.eonsimulator.trafficGenerators.bitRateGenerators.RandomContinuosTrafficUniform;
import br.ufpe.eonsimulator.trafficGenerators.bitRateGenerators.RandomDiscreteTrafficUniform;
import br.ufpe.eonsimulator.trafficGenerators.deathTimeGenerators.ConstantDeathRate;
import br.ufpe.eonsimulator.trafficGenerators.deathTimeGenerators.ExponentialDeathRate;
import br.ufpe.eonsimulator.trafficGenerators.deathTimeGenerators.IsDeathRateGenerator;
import br.ufpe.eonsimulator.trafficGenerators.nodePairGenerators.AllNodePairGenerator;
import br.ufpe.eonsimulator.trafficGenerators.nodePairGenerators.IsNodePairGenerator;
import br.ufpe.eonsimulator.trafficGenerators.nodePairGenerators.RandomNodePairGenerator;
import br.ufpe.simulator.utils.ConvertUtils;
import br.ufpe.simulator.utils.StringUtil;

public class TrafficGeneratorFactory {

	private static final String NODEPAIR_GENERATOR_KEY = "simulation.nodePairGenerator";
	private static final String BITRATE_GENERATOR_KEY = "simulation.bitRateGenerator";
	private static final String ARRIVAL_TIME_GENERATOR_KEY = "simulation.arrivalTimeGenerator";
	private static final String DEATH_RATE_GENERATOR_KEY = "simulation.deathRateGenerator";
	private static final String BITRATE_CONSTANT_BITRATES_KEY = "simulation.constantTraffic.bitRates";
	private static final String BITRATE_DISCRETE_BITRATES_KEY = "simulation.discreteTraffic.bitRates";
	private static final String CONSTANTE_RATE_PARAM_KEY = "simulation.constantDeathRate";
	private static final String CONSTANT_ARRIVAL_TIME_PARAM_KEY = "simulation.constantArrivalTime";

	public static TrafficGenerator createTrafficGenerator(Properties properties) {
		IsNodePairGenerator nodePairGenerator = createNodePairGenerator(properties);
		IsBitRateGenerator bitRateGenerator = createBitRateGenerator(properties);
		IsArrivalTimeGenerator arrivalTimeGenerator = createArrivalTimeGenerator(properties);
		IsDeathRateGenerator deathRateGenerator = createDeathRateGenerator(properties);
		return new TrafficGenerator(nodePairGenerator, bitRateGenerator,
				arrivalTimeGenerator, deathRateGenerator);
	}

	private static IsNodePairGenerator createNodePairGenerator(
			Properties properties) {
		IsNodePairGenerator nodePairGenerator = null;
		int key = ConvertUtils.convertToInteger(properties
				.getProperty(NODEPAIR_GENERATOR_KEY));
		switch (key) {
		case 0:
			nodePairGenerator = new RandomNodePairGenerator();
			break;
		case 1:
			nodePairGenerator = new AllNodePairGenerator();
			break;
		default:
			break;
		}
		return nodePairGenerator;
	}

	private static IsBitRateGenerator createBitRateGenerator(
			Properties properties) {
		IsBitRateGenerator bitRateGenerator = null;
		int key = ConvertUtils.convertToInteger(properties
				.getProperty(BITRATE_GENERATOR_KEY));
		List<Double> constantBitRates = ConvertUtils.convertToDouble(StringUtil
				.split(properties.getProperty(BITRATE_CONSTANT_BITRATES_KEY),
						","));
		List<Double> bitRates = ConvertUtils.convertToDouble(StringUtil.split(
				properties.getProperty(BITRATE_DISCRETE_BITRATES_KEY), ","));
		switch (key) {
		case 0:
			bitRateGenerator = new RandomContinuosTrafficUniform();
			break;
		case 1:
			bitRateGenerator = new ConstantTraffic(constantBitRates);
			break;
		case 2:
			bitRateGenerator = new RandomDiscreteTrafficUniform(bitRates);
			break;
		default:
			break;
		}
		return bitRateGenerator;
	}

	private static IsArrivalTimeGenerator createArrivalTimeGenerator(
			Properties properties) {
		IsArrivalTimeGenerator arrivalTimeGenerator = null;
		int key = ConvertUtils.convertToInteger(properties
				.getProperty(ARRIVAL_TIME_GENERATOR_KEY));
		double arrivalTime = ConvertUtils.convertToDouble(properties
				.getProperty(CONSTANT_ARRIVAL_TIME_PARAM_KEY));
		switch (key) {
		case 0:
			arrivalTimeGenerator = new ExponentialArrivalTime();
			break;
		case 1:
			arrivalTimeGenerator = new ConstantArrivalTime(arrivalTime);
			break;

		default:
			break;
		}
		return arrivalTimeGenerator;
	}

	private static IsDeathRateGenerator createDeathRateGenerator(
			Properties properties) {
		IsDeathRateGenerator deathRateGenerator = null;
		int key = ConvertUtils.convertToInteger(properties
				.getProperty(DEATH_RATE_GENERATOR_KEY));
		double constantRate = ConvertUtils.convertToDouble(properties
				.getProperty(CONSTANTE_RATE_PARAM_KEY));
		switch (key) {
		case 0:
			deathRateGenerator = new ExponentialDeathRate();
			break;
		case 1:
			deathRateGenerator = new ConstantDeathRate(constantRate);
			break;

		default:
			break;
		}
		return deathRateGenerator;
	}
}
