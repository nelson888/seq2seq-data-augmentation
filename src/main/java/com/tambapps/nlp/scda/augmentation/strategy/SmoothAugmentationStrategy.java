package com.tambapps.nlp.scda.augmentation.strategy;


import com.tambapps.nlp.scda.dictionary.StringUtils;
import com.tambapps.nlp.scda.dictionary.UnigramDistribution;
import com.tambapps.nlp.scda.exception.AugmentationException;
import com.tambapps.nlp.scda.exception.DistributionException;

import java.util.List;
import java.util.Random;

/**
 * Replace words with words with same frequency from the unigram diagram of the dataset
 */
public class SmoothAugmentationStrategy extends ReplacementAugmentationStrategy {

  private final Random random = new Random();
  private final UnigramDistribution unigramDistribution;

  public SmoothAugmentationStrategy(double gamma, UnigramDistribution unigramDistribution) {
    super(gamma);
    this.unigramDistribution = unigramDistribution;
  }

  @Override
  String replaceWord(String word) throws AugmentationException {
    if (StringUtils.isPunctuation(word)) {
      return word;
    }
    double prob;
    try {
      prob = unigramDistribution.getProbability(word);
    } catch (DistributionException e) {
      throw new AugmentationException(e.getMessage(), e);
    }
    List<String> replacements = unigramDistribution.getWordsWithProb(prob);
    if (replacements.size() == 1) { // meaning that the input word is the only one for this probability
      replacements = findClosestReplacements(prob);
    }
    String replacementWord;
    do {
      replacementWord = replacements.get(random.nextInt(replacements.size()));
    } while (word.equals(replacementWord));
    return replacementWord;
  }

  private List<String> findClosestReplacements(double probability) {
    List<Double> probabilities = unigramDistribution.getSortedProbabilities();
    int index = probabilities.indexOf(probability);
    double closestProb;
    if (index == 0) {
      closestProb = probabilities.get(1);
    } else if (index == probabilities.size() - 1) {
      closestProb = probabilities.get(index - 1);
    } else {
      double p1 = probabilities.get(index - 1);
      double p2 = probabilities.get(index + 1);
      if (Math.abs(probability - p1) < Math.abs(probability - p2)) {
        closestProb = p1;
      } else {
        closestProb = p2;
      }
    }
    return unigramDistribution.getWordsWithProb(closestProb);
  }
}
