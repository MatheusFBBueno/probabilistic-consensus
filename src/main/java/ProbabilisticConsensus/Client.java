package ProbabilisticConsensus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client {
	public final List<Integer> resultsBuffer;
	int faultTolerance;

	public Client(int faultTolerance){
		this.resultsBuffer = new ArrayList<>();
		this.faultTolerance = faultTolerance;
	}

	public int getNumberOfRequests() {
		return 2 * faultTolerance + 1;
	}

	public int getMostCommonResult() {
		return this.resultsBuffer.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()))
				.entrySet()
				.stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.orElse(-1);
	}

	public void getConsensus() {
		int result = getMostCommonResult();
		if (result != -1) {
			System.out.println("Consenso atingido, valor: " + result);
		} else {
			System.out.println("Consenso não foi atingido");

		}
	}

	public void addResult(Integer result) {
		this.resultsBuffer.add(result);
	}

}
