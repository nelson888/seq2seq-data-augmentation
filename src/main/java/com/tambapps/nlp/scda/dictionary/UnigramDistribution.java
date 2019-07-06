package com.tambapps.nlp.scda.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnigramDistribution {
  private final Map<String, Double> distribution;
  private final Map<Double, List<String>> reversedDistribution;
  private final List<Double> probabilities;

  private UnigramDistribution(Map<String, Double> distribution, Map<Double, List<String>> reversedDistribution, List<Double> probabilities) {
    this.distribution = distribution;
    this.reversedDistribution = reversedDistribution;
    this.probabilities = probabilities;
  }

  public double getProbability(String word) {
    return distribution.get(word);
  }

  public List<String> getWordsWithProb(double probability) {
    return reversedDistribution.get(probability);
  }

  public List<Double> getSortedProbabilities() {
    return probabilities;
  }

  private static Map<String, Double> loadDistributionMap(BufferedReader reader) throws IOException {
    final Map<String, Double> distribution = new HashMap<>();
    String line;
    while ((line = reader.readLine()) != null) {
      String[] splitLine = line.split("\\s+");
      String word = StringUtils.trimPunctuation(splitLine[0]);
      double occurrence = Double.parseDouble(splitLine[1]);
      distribution.put(word, occurrence);
    }
    return distribution;
  }

  private static UnigramDistribution createUnigramDistribution(Map<String, Double> distribution) {
    final Map<Double, List<String>> reversedDistribution = new HashMap<>();
    for (Map.Entry<String, Double> entry : distribution.entrySet()) {
      double probability = entry.getValue();
      distribution.put(entry.getKey(), probability);
      reversedDistribution.computeIfAbsent(probability, k -> new ArrayList<>())
        .add(entry.getKey());
    }
    final List<Double> probabilities = reversedDistribution.keySet()
      .stream()
      .sorted()
      .collect(Collectors.toList());

    return new UnigramDistribution(Collections.unmodifiableMap(distribution),
      Collections.unmodifiableMap(reversedDistribution),
      Collections.unmodifiableList(probabilities));
  }

  public static UnigramDistribution loadDictionary(BufferedReader dictionaryReader) throws IOException {
    final Map<String, Double> distribution = loadDistributionMap(dictionaryReader);
    double count = distribution.values()
      .stream()
      .reduce(0d, Double::sum);
    for (Map.Entry<String, Double> entry : distribution.entrySet()) {
      double probability = entry.getValue() / count;
      distribution.put(entry.getKey(), probability);
    }
    return createUnigramDistribution(distribution);
  }

  public static UnigramDistribution loadDistribution(BufferedReader distributionReader) throws IOException {
    final Map<String, Double> distribution = loadDistributionMap(distributionReader);
    return createUnigramDistribution(distribution);
  }
}
