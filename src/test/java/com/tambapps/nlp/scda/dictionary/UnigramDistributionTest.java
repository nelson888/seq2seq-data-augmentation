package com.tambapps.nlp.scda.dictionary;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class UnigramDistributionTest {

  private static final String DICTIONARY_FILE = "/dictionary.txt";
  private static final String DISTRIBUTION_FILE = "/distribution.txt";

  @Test
  public void loadDictionaryTest() throws IOException {
    UnigramDistribution distribution = UnigramDistribution.loadDictionary(getReader(DICTIONARY_FILE));
    assertDistribution(distribution);
  }

  @Test
  public void loadDistributionTest() throws IOException {
    UnigramDistribution distribution = UnigramDistribution.loadDictionary(getReader(DISTRIBUTION_FILE));
    assertDistribution(distribution);
  }

  private void assertDistribution(UnigramDistribution distribution) {
    assertNotNull(distribution);
    List<Double> probabilities = distribution.getSortedProbabilities();
    assertEquals(3, probabilities.size());

    assertEquals(Stream.of(2d,3d,1d)
      .map(d-> d / 6d)
      .sorted()
      .collect(Collectors.toList()), probabilities);
  }

  private BufferedReader getReader(String resourcePath) {
    return new BufferedReader(new InputStreamReader(UnigramDistributionTest.class.getResourceAsStream(resourcePath)));
  }
}
