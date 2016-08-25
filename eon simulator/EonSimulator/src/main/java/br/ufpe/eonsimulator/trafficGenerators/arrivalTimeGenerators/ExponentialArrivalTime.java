package br.ufpe.eonsimulator.trafficGenerators.arrivalTimeGenerators;

import br.ufpe.eonsimulator.domain.Simulation;
import br.ufpe.simulator.math.IsRandomVariable;
import br.ufpe.simulator.math.IsRandomVariable.RandomVariableType;
import br.ufpe.simulator.math.RandomVariableFactory;

public class ExponentialArrivalTime implements IsArrivalTimeGenerator {

	public double getArrivalTime(Simulation simulation) {
		IsRandomVariable randomVariable = RandomVariableFactory
				.createRandomVariable(
						RandomVariableType.EXPONENTIALDISTRIBUTION,
						1 / simulation.getSimulationParameters()
								.getConnectionCurrentArrivalRate());
		return simulation.getSimulationTime() + randomVariable.sample();
	}

}
