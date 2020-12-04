package at.uibk.epc.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import at.uibk.epc.service.MetricsService;

public class MetricsServiceCosineSimilarityTest {
	
	@Test
	public void testCosineSimilarity() throws Exception {
		List<Double> A = newArray("77", "76662.98");
		List<Double> B = newArray("82", "83732.98");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeCosineSimilarty(A, B));
	}
	
	
	@Test
	public void testCosineSimilarity2() throws Exception {
		List<Double> A = newArray("27", "26662.98");
		List<Double> B = newArray("82", "83732.98");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeCosineSimilarty(A, B));
	}
	
	@Test
	public void testCosineSimilarity3() throws Exception {
		List<Double> A = newArray("27", "26662.98");
		List<Double> B = newArray("182", "183732.98");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeCosineSimilarty(A, B));
	}
	
	
	@Test
	public void testCosineSimilarity4() throws Exception {
		List<Double> A = newArray("27", "26662.98");
		List<Double> B = newArray("28", "26668.98");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeCosineSimilarty(A, B) + " QuiteNear");
	}
	
	@Test
	public void testCosineSimilarityFarApart() throws Exception {
		List<Double> A = newArray("27", "26662.98");
		List<Double> B = newArray("28888", "28886668.98");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeCosineSimilarty(A, B) + " CosineSimilarityFarApart");
	}
	
	@Test
	public void testCosineSimilarityIdentical() throws Exception {
		List<Double> A = newArray("27", "4432");
		List<Double> B = newArray("27", "4432");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeCosineSimilarty(A, B) + " testCosineSimilarityIdentical");
	}

	private List<Double> newArray(String floorArea, String energy) {
		List<Double> A = new ArrayList<Double>();
		A.add(new Double(floorArea));
		A.add(new Double(energy));
		return A;
	}
}
