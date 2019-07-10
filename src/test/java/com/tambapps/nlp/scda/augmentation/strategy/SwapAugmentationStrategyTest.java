package com.tambapps.nlp.scda.augmentation.strategy;

import com.tambapps.utils.ioutils.IOUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class SwapAugmentationStrategyTest {

  @Test
  public void testSimpleSentence() throws IOException {
    List<String> lines;
    try (BufferedReader reader = IOUtils.newReader(SwapAugmentationStrategyTest.class, "/sentences.txt")) {
      lines = reader.lines()
        .collect(Collectors.toList());
    }
    for (int window = 0; window < 4; window++) {
      final SwapAugmentationStrategy swap = new SwapAugmentationStrategy(window);
      final int k = window;
      lines.forEach(l -> assertLine(k + 1, l, swap));
    }
  }

  private void assertLine(int k, String line, SwapAugmentationStrategy swap) {
    Set<String> expected = toWordsSet(line);
    assertEquals("Swapped sentence doesn't contains same words as the original (k=" + k + ")",
      expected, toWordsSet(swap.modifySource(line)));
  }

  private Set<String> toWordsSet(String line) {
    return Arrays.stream(line.split("\\s+"))
      .collect(Collectors.toSet());
  }
}
