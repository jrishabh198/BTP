package br.ufpe.eonsimulator.spectrumAssignment;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.eonsimulator.domain.Connection;
import br.ufpe.eonsimulator.domain.Route;
import br.ufpe.simulatorkernel.domain.SlotOccupancyCollection;

public class CapacityLossAssignmentAlgorithm extends
		IsSpectrumAssignmentAlgorithm {

	@Override
	public void trySpectrumAssignment(Connection connection, Route route) {
		super.trySpectrumAssignment(connection, route);
		SlotOccupancyCollection soc = calculateRouteSlotOccupancy(route);
		tryToEstabilishConnection(connection, soc, route,
				connection.getMaxSlotsExpandUnits());
	}

	private void tryToEstabilishConnection(Connection connection,
			SlotOccupancyCollection soc, Route route, int maxNumberOfSlots) {
		int requiredSlots = connection.getNumberSlotRequired();
		Format bestFormat = calculateBestFormat(soc, requiredSlots,
				maxNumberOfSlots);
		if (bestFormat != null) {
			route.setInitialSlot(bestFormat.initialIndex);
			route.setFinalSlot(bestFormat.initialIndex + requiredSlots - 1);
		}

	}

	public Format calculateBestFormat(SlotOccupancyCollection collection,
			int requiredSlots, int maxNumberOfSlots) {
		List<Format> formats = geFormats(collection, requiredSlots);
		SlotOccupancyCollection clone = null;
		for (Format format : formats) {
			clone = collection.clone();
			clone.setSlotsAsOccupied(format.initialIndex, format.initialIndex
					+ requiredSlots - 1);
			format.capacities = getCapacities(clone, maxNumberOfSlots);
		}
		return getBestFormat(formats);
	}

	private Format getBestFormat(List<Format> formats) {
		Format bestFormat = null;
		int maxSum = 0;
		int sum = 0;
		for (Format format : formats) {
			sum = 0;
			for (Integer capacity : format.capacities) {
				sum += capacity;
			}
			if (sum > maxSum) {
				maxSum = sum;
				bestFormat = format;
			}
		}
		return bestFormat;
	}

	private List<Integer> getCapacities(SlotOccupancyCollection soc,
			int maxNumberOfSlots) {
		List<Integer> integers = new ArrayList<Integer>();
		for (int numberOfSlots = 1; numberOfSlots <= maxNumberOfSlots; numberOfSlots++) {
			integers.add(this.geFormats(soc, numberOfSlots).size());
		}
		return integers;
	}

	public List<Format> geFormats(SlotOccupancyCollection occupancyCollection,
			int numberOfSlots) {
		List<Format> formats = new ArrayList<Format>();
		int count = 0;
		byte[] slots = null;
		if (slots != null) {
			for (int i = 0; i < slots.length; i++) {
				if (slots[i] == 0) {
					count++;
				} else {
					count = 0;
				}
				if (count >= numberOfSlots) {
					Format format = new Format();
					format.initialIndex = i - numberOfSlots + 1;
					formats.add(format);
				}
			}
		}
		return formats;
	}
}

class Format {
	int initialIndex;
	List<Integer> capacities;
}