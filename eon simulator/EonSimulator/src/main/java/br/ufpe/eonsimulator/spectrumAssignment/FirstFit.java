package br.ufpe.eonsimulator.spectrumAssignment;

import br.ufpe.eonsimulator.domain.Connection;
import br.ufpe.eonsimulator.domain.Route;
import br.ufpe.simulatorkernel.domain.SlotOccupancyCollection;

public class FirstFit extends IsSpectrumAssignmentAlgorithm {

	@Override
	public void trySpectrumAssignment(Connection connection, Route route) {
		super.trySpectrumAssignment(connection, route);
		SlotOccupancyCollection soc = calculateRouteSlotOccupancy(route);
		tryToEstabilishConnection(connection, soc, route);
	}

	private void tryToEstabilishConnection(Connection connection,
			SlotOccupancyCollection soc, Route route) {
		int connectionWidth = connection.getNumberSlotRequired();
		int count = 0;
		int initialSlot = -1;
		for (int s = 0; s < soc.getNumberSlots(); s++) {
			if (!soc.getSlot(s)) {
				if (initialSlot == -1) {
					initialSlot = s;
				}
				count++;
			} else {
				count = 0;
				initialSlot = -1;
			}
			if (count == connectionWidth) {
				route.setInitialSlot(initialSlot);
				route.setFinalSlot(initialSlot + count - 1);
				break;
			}
		}

	}

}
