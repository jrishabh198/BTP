package br.ufpe.eonsimulator.rsa;

import java.util.Comparator;

import br.ufpe.eonsimulator.domain.Connection;
import br.ufpe.eonsimulator.domain.OSNRUtils;
import br.ufpe.eonsimulator.domain.Route;
import br.ufpe.eonsimulator.domain.Simulation;
import br.ufpe.eonsimulator.modulation.IsModulationFormat;
import br.ufpe.eonsimulator.modulation.ModulationFormatBitRateWrapper;

public class RSAMinHopMaxOSNRAlgorithm extends RSAAlgorithm {

	public RSAMinHopMaxOSNRAlgorithm(
			Comparator<ModulationFormatBitRateWrapper> modulationFormatComparator,
			int kFilter, boolean qotFilter) {
		super(modulationFormatComparator, kFilter, qotFilter);
	}

	private class RSAMinHopMaxOSNRWrapper extends RSAWrapper {

		public RSAMinHopMaxOSNRWrapper(String index, boolean isPathValid,
				boolean isOSNRValid, Route route, double requiredOSNR,
				int nSlots) {
			super(index, isPathValid, isOSNRValid, route, requiredOSNR, nSlots);
		}

		@Override
		protected int compareToOtherEquals(RSAWrapper other) {
			int thisHop = getRoute().getPath().getLinks().size();
			int otherHop = other.getRoute().getPath().getLinks().size();
			double thisOSNR = getRoute().getOSNR();
			double otherOSNR = other.getRoute().getOSNR();
			if (thisHop < otherHop) {
				return -1;
			} else if (thisHop > otherHop) {
				return 1;
			} else if (thisOSNR > otherOSNR) {
				return -1;
			} else if (thisOSNR < otherOSNR) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	@Override
	protected RSAWrapper createRSAWrapper(String index, Route route,
			Simulation simulation, Connection connection,
			IsModulationFormat modulationFormat, int nSlots) {
		return new RSAMinHopMaxOSNRWrapper(index, route.isPathValid(),
				OSNRUtils.isValidOSNR(simulation, connection, route,
						modulationFormat.getNSymbol()), route,
				connection.getRequiredOSNR(), nSlots);
	}

}
