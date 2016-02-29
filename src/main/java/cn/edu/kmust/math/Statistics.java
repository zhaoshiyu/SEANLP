package cn.edu.kmust.math;

/**
 * 数理统计相关量计算，如期望、方差、协方差、相关系数等
 * 
 * @author  Zhao Shiyu
 *
 */
public class Statistics {
	
	/**
	 * 
	 * @Title: sum 
	 * @Description: 求和 sum(X)
	 * @param doubleArray
	 * @return double
	 * @throws 
	 *
	 */
	public static double sum(double[] doubleArray) {
		double sum = 0;
		for (int i = 0; i < doubleArray.length; i++) {
			sum += doubleArray[i];
		}
		return sum;
	}
	
	/**
	 * 
	 * @Title: average 
	 * @Description: 平均值 avg(X)
	 * @param doubleArray
	 * @return double
	 * @throws 
	 *
	 */
	public static double average(double[] doubleArray) {
		return sum(doubleArray)/doubleArray.length;
	}
	
	/**
	 * 
	 * @Title: squareSum 
	 * @Description: 每个数据平方后在求和 sum(X^2)
	 * @param doubleArray
	 * @return double
	 * @throws 
	 *
	 */
	public static double squareSum(double[] doubleArray) {
		double squareSum = 0;
		for (int i = 0; i < doubleArray.length; i++) {
			squareSum += Math.pow(doubleArray[i], 2);
		}
		return squareSum;
	}
	
	/**
	 * 
	 * @Title: expectation 
	 * @Description: 求数学期望 E(X) = sum(X)/n
	 * @param doubleArray
	 * @return double
	 * @throws 
	 *
	 */
	public static double expectation(double[] doubleArray) {
		
		return sum(doubleArray)/doubleArray.length;
	}
	
	/**
	 * 
	 * @Title: variance
	 * @Description: 求方差: D(X) = sum([X-E(X)]^2)/n = {[sum(X^2)]-n*[E(X)]^2}/n =  [sum(X^2)]/n-[E(X)]^2
	 * @param doubleArray
	 * @return double
	 * @throws 
	 *
	 */
	public static double variance(double[] doubleArray) {
		
		return squareSum(doubleArray) / doubleArray.length - (Math.pow(expectation(doubleArray), 2));
	}
	
//	public static double variance(double[] doubleArray) {
//		double deviationSquareSum = 0;
//		double e = expectation(doubleArray);
//		for (int i = 0; i < doubleArray.length; i++) {
//			deviationSquareSum += Math.pow(doubleArray[i] - e, 2);
//		}
//		
//		return deviationSquareSum / doubleArray.length;
//	}
	
	/**
	 * 
	 * @Title: standardDeviation 
	 * @Description: 求标准差  sqrt(D(X))
	 * @param doubleArray
	 * @return double
	 * @throws 
	 *
	 */
	public static double standardDeviation(double[] doubleArray) {
		
		return Math.sqrt(Math.abs(variance(doubleArray))); //注意取绝对值
	}
	
	/**
	 * 
	 * @Title: sumCrossedProduct 
	 * @Description: 两个数组交叉积求和 sum(X*Y)
	 * @param doubleArray1
	 * @param doubleArray2
	 * @throws Exception
	 * @return double
	 *
	 */
	public static double sumCrossedProduct(double[] doubleArray1 ,double[] doubleArray2) throws Exception {
		double sumCrossedProduct = 0;
		if (doubleArray1.length != doubleArray2.length) {
			throw new Exception("样本维度不等，请保证两个样本的维度相同！");
		} else {
			for (int i = 0; i < doubleArray1.length; i++) {
				sumCrossedProduct += doubleArray1[i] * doubleArray2[i];
			}
		}
		return sumCrossedProduct;
	}
	
	/**
	 *
	 * @Title: covariance 
	 * @Description: 求两个维数相同样本的协方差  Cov(X,Y) = sum[(X-E(X)) * (Y-E(Y))]/n = sum(X*Y)/n - [E(X) * E(Y)]
	 * @param doubleArray1
	 * @param doubleArray2
	 * @return double
	 * @throws Exception
	 *
	 */
	public static double covariance(double[] doubleArray1 ,double[] doubleArray2) throws Exception {
		
		return sumCrossedProduct(doubleArray1, doubleArray2) / doubleArray1.length - expectation(doubleArray1) * expectation(doubleArray2);
	}
	
//	public static double covariance1(double[] doubleArray1 ,double[] doubleArray2) throws Exception {
//		double e1 = expectation(doubleArray1);
//		double e2 = expectation(doubleArray2);
//		double sum = 0;
//		for (int i = 0; i < doubleArray1.length; i++) {
//			sum += ((doubleArray1[i]-e1) * (doubleArray2[i]-e2));
//		}
//		
//		return sum / doubleArray1.length;
//	}
	
	/**
	 * 
	 * @Title: correlation 
	 * @Description: 求相关系数 r = Cov(X,Y) / [sqrt(D(X)) * sqrt(D(Y))] = [sum(X*Y) - sum(x)*sum(Y)/n] / [sqrt(sum(X^2)-sum(X)^2/n) * sqrt(sum(Y^2)-sum(Y)^2/n)]
	 * @param doubleArray1
	 * @param doubleArray2
	 * @throws Exception
	 * @return double 返回结果范围[-1,1]
	 *
	 */
	public static double correlation(double[] doubleArray1 ,double[] doubleArray2) throws Exception {
		
		return covariance(doubleArray1, doubleArray2) / (standardDeviation(doubleArray1) * standardDeviation(doubleArray2));
	}
	
	/**
	 * 
	 * @Title: includedAngleCosine 
	 * @Description: 求夹角余弦 sum(X*Y)/sqrt(sum(X^2) * sum(Y^2))
	 * @param doubleArray1
	 * @param doubleArray2
	 * @throws Exception
	 * @return double
	 *
	 */
	public static double includedAngleCosine(double[] doubleArray1, double[] doubleArray2) throws Exception {
		
		return sumCrossedProduct(doubleArray1, doubleArray2) / (Math.sqrt(squareSum(doubleArray1) * squareSum(doubleArray2)));
	}
	
	/**
	 * 
	 * @Title: kPowerSum 
	 * @Description: 每个数据k次方后在求和 sum(X^k)
	 * @param doubleArray
	 * @param k
	 * @return double
	 * @throws 
	 *
	 */
	public static double kPowerSum(double[] doubleArray, int k) {
		double kPowerSum = 0;
		for (int i = 0; i < doubleArray.length; i++) {
			kPowerSum += Math.pow(doubleArray[i], k);
		}
		
		return kPowerSum;
	}
	
	/**
	 * 
	 * @Title: kOriginMoment 
	 * @Description: k阶原点矩 sum(X^k)/n
	 * @param doubleArray
	 * @param k
	 * @return double
	 * @throws 
	 *
	 */
	public static double kOriginMoment(double[] doubleArray, int k) {
		
		return kPowerSum(doubleArray, k) / doubleArray.length;
	}
	
	/**
	 * 
	 * @Title: kCentralMoment 
	 * @Description: k阶中心矩 sum([X-E(X)]^k)/n
	 * @param doubleArray
	 * @param k
	 * @return double
	 * @throws 
	 *
	 */
	public static double kCentralMoment(double[] doubleArray, int k) {
		double deviationSum = 0;
		for (int i = 0; i < doubleArray.length; i++) {
			deviationSum += Math.pow(doubleArray[i] - expectation(doubleArray), k);
		}
		
		return deviationSum / doubleArray.length;
	}
	

}
