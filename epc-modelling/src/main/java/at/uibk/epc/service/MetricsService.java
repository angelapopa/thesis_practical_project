package at.uibk.epc.service;

import java.util.List;

public class MetricsService {

	//not really helpful this metric, maybe take a look at ranked similarity
	public double computeCosineSimilarty(List<Double> A, List<Double> B) throws Exception {
		if (A == null || B == null || A.size() == 0 || B.size() == 0 || A.size() != B.size()) {
			throw new IndexOutOfBoundsException("The dimensions of the input vector are invalid!");
		}

		double dotProduct = 0;
		double sumASq = 0;
		double sumBSq = 0;
		for (int i = 0; i < A.size(); i++) {
			dotProduct += A.get(i) * B.get(i);
			sumASq += A.get(i) * A.get(i);
			sumBSq += B.get(i) * B.get(i);
		}
		if (sumASq == 0 && sumBSq == 0) {
			throw new Exception("Invalid vector values!");
		}
		return dotProduct / (Math.sqrt(sumASq) * Math.sqrt(sumBSq));
	}
	
	//looks much better
	public double computeEuclideanDistance(List<Double> A, List<Double> B) throws Exception{
		if (A == null || B == null || A.size() == 0 || B.size() == 0 || A.size() != B.size()) {
			throw new IndexOutOfBoundsException("The dimensions of the input vector are invalid!");
		}
		
		double sumSq=0;
		for (int i = 0; i < A.size(); i++) {
			double dif = 0;
			dif = A.get(i) - B.get(i);
			sumSq += dif * dif;
		}
		return Math.sqrt(sumSq);
	}
}
