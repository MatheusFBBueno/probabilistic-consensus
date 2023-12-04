package ProbabilisticConsensus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client {
	public List<Integer> resultsBuffer;
	int faultTolerance;

	public Client(int faultTolerance){
		this.resultsBuffer = new ArrayList<>();
		this.faultTolerance = faultTolerance;
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

	public void getConsensus() throws Exception {
		while (this.resultsBuffer.stream().filter(Objects::nonNull).count() < 2L*faultTolerance+1) {
			System.out.println("Cliente esperando resultado do consenso");
			Thread.sleep(1000);
		}
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
