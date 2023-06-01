package ru.timur.lab4.calculation;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.interval.ConfidenceInterval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatCalculations {

    //геометрическое среднее
    public static double calcGeomMean(double[] sample) {
        return StatUtils.geometricMean(sample);
    }

    //среднее
    public static double calcMean(double[] sample) {
        return StatUtils.mean(sample);
    }

    //стандартное отклонение
    public static double calcSD(double[] sample) {
        StandardDeviation sd = new StandardDeviation();
        return sd.evaluate(sample);
    }

    //размах выборки
    public static double calcR(double[] sample) {
        return StatUtils.max(sample) - StatUtils.min(sample);
    }

    //ковариация междку двумя выборками
    public static double calcCov(double[] sample1, double[] sample2) {
        Covariance covariance = new Covariance();
        return covariance.covariance(sample1, sample2);
    }

    //размер
    public static double calcN(double[] sample) {
        return sample.length;
    }

    //коеф вариации конкретной выборки
    public static double calcCoeffVar(double[] sample) {
        StandardDeviation sd = new StandardDeviation();
        double mean = StatUtils.mean(sample);
        return sd.evaluate(sample) / mean;
    }

    //доверительный интервал
    public static ConfidenceInterval calcConfInterval(double[] sample, double alpha) {
        StandardDeviation SD = new StandardDeviation();
        double mean = StatUtils.mean(sample);
        double sd = SD.evaluate(sample);
        NormalDistribution normalDistribution = new NormalDistribution();
        double quantile = normalDistribution.inverseCumulativeProbability(1.0 - alpha / 2.0);
        return new ConfidenceInterval(mean - quantile * sd / Math.sqrt(sample.length), mean + quantile * sd / Math.sqrt(sample.length), alpha);
    }

    //дисперсия для выборки
    public static double calcVar(double[] sample) {
        Variance variance = new Variance();
        return variance.evaluate(sample);
    }

    //минимум
    public static double calcMin(double[] sample) {
        return StatUtils.min(sample);
    }

    //максимум
    public static double calcMax(double[] sample) {
        return StatUtils.max(sample);
    }

    public static Map<String, Double> getCalculations(double[] sample) {
        Map<String, Double> result = new HashMap<>();
        result.put("кол-во элементов", calcN(sample));
        result.put("Максимум", calcMax(sample));
        result.put("Минимум", calcMin(sample));
        result.put("Размах", calcR(sample));
        result.put("среднее", calcMean(sample));
        result.put("среднее геом.", calcGeomMean(sample));
        result.put("стандартное отколнение", calcSD(sample));
        result.put("Дисперсия", calcVar(sample));
        result.put("коэф. вариации", calcCoeffVar(sample));
        ConfidenceInterval interval = calcConfInterval(sample, 0.05);
        result.put("нижняя граница дов. интервала", interval.getLowerBound());
        result.put("нижняя граница дов.интервала", interval.getUpperBound());
        result.put("confidenceLevel", interval.getConfidenceLevel());
        return result;
    }

    public static double[][] getCovMatrix(List<double[]> samples) {
        double[][] covarianceC = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                covarianceC[i][j] = calcCov(samples.get(i), samples.get(j));
            }
        }
        return covarianceC;
    }
}
