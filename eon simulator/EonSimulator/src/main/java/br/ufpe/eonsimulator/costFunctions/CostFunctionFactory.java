package br.ufpe.eonsimulator.costFunctions;

import java.util.Properties;

import br.ufpe.simulator.utils.ConvertUtils;

public class CostFunctionFactory {

	private static final String COST_FUNCTION_KEY = "simulation.costFunction";

	public static IsCostFunction createCostFunction(Properties properties) {
		IsCostFunction costFunction = null;
		int key = ConvertUtils.convertToInteger(properties
				.getProperty(COST_FUNCTION_KEY));
		switch (key) {
		case 0:
			costFunction = new HopCostFunction();
			break;
		case 1:
			costFunction = new LengthCostFunction();
			break;
		case 2:
			costFunction = new CostFunction();
			break;
		default:
			break;
		}
		return costFunction;
	}
}
