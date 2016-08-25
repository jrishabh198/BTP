package br.ufpe.eonsimulator.spectrumAssignment;

import br.ufpe.eonsimulator.domain.Connection;
import br.ufpe.eonsimulator.domain.Route;
import br.ufpe.simulatorkernel.domain.Link;
import br.ufpe.simulatorkernel.domain.SlotOccupancyCollection;

public abstract class IsSpectrumAssignmentAlgorithm {

	protected SlotOccupancyCollection calculateRouteSlotOccupancy(Route route) {

		SlotOccupancyCollection soc = new SlotOccupancyCollection(
				route.getNumberSlots());

		for (Link link : route.getLinks()) {
			soc.mergeOccupancy(link.getOcSpectrumCollection());
		}

		return soc;
	}

	public void trySpectrumAssignment(Connection connection, Route route) {
		route.clear();
	}
}
