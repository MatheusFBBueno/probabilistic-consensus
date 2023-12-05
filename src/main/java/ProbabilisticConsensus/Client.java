package ProbabilisticConsensus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client {
	public final List<String> resultsBuffer;
	int faultTolerance;

	public Client(int faultTolerance){
		this.resultsBuffer = new ArrayList<>();
		this.faultTolerance = faultTolerance;
	}

	public int getNumberOfRequests() {
		return 2 * faultTolerance + 1;
	}

	public String getMostCommonResult() {
		return this.resultsBuffer.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.groupingBy(String::toString, Collectors.counting()))
				.entrySet()
				.stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.orElse("-1");
	}

	public void getConsensus() {
		String result = getMostCommonResult();
		if (!result.equals("-1")) {
			System.out.println("Consenso atingido, valor: " + result);
		} else {
			System.out.println("Consenso não foi atingido");

		}
	}

	public void addResult(String result) {
		this.resultsBuffer.add(result);
	}

}
