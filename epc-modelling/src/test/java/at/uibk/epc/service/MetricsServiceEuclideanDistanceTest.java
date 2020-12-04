package at.uibk.epc.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MetricsServiceEuclideanDistanceTest {
	
	@Test
	public void testEuclideanDistance() throws Exception {
		List<Double> A = newArray("77", "766");
		List<Double> B = newArray("82", "837");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeEuclideanDistance(A, B) + " test euclidean distance");
	}
	
	@Test
	public void testEuclideanDistance2() throws Exception {
		List<Double> A = newArray("27", "266");
		List<Double> B = newArray("82", "837");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeEuclideanDistance(A, B) + " test euclidean distance2");
	}
	
	@Test
	public void testEuclideanDistance3() throws Exception {
		List<Double> A = newArray("27", "266");
		List<Double> B = newArray("120", "183");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeEuclideanDistance(A, B) + " test euclidean distance3");
	}
	
	
	@Test
	public void testEuclideanDistance4() throws Exception {
		List<Double> A = newArray("27", "266");
		List<Double> B = newArray("28", "266");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeEuclideanDistance(A, B) + " QuiteNear");
	}
	
	@Test
	public void testEuclideanDistanceFarApart() throws Exception {
		List<Double> A = newArray("27", "266");
		List<Double> B = newArray("120", "888");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeEuclideanDistance(A, B) + " EuclideanDistanceFarApart");
	}
	
	@Test
	public void testEuclideanDistanceIdentical() throws Exception {
		List<Double> A = newArray("27", "443");
		List<Double> B = newArray("27", "443");
		
		MetricsService metricsService = new MetricsService();
		System.out.println(metricsService.computeEuclideanDistance(A, B) + " testEuclideanDistanceIdentical");
	}

	private List<Double> newArray(String floorArea, String energy) {
		List<Double> A = new ArrayList<Double>();
		A.add(new Double(floorArea));
		A.add(new Double(energy));
		return A;
	}
}
