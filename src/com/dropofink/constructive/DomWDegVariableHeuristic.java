package com.dropofink.constructive;

import com.dropofink.constructive.ConstraintPropagator.ConstraintCausedDomainWipeoutListener;
import com.dropofink.model.Constraint;
import com.dropofink.model.Problem;
import com.dropofink.model.Variable;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This is one of the better variable ordering heuristics as it balances fail-first and frequency of commonly
 * domain-wipeout causing constraints. Full paper here:
 * http://www.cril.univ-artois.fr/~lecoutre/research/publications/2004/ECAI2004.pdf
 */
public class DomWDegVariableHeuristic<T> implements VariableHeuristic<T>, ConstraintCausedDomainWipeoutListener<T> {
  private final Problem<T> problem;
  private final Map<Constraint<T>, Double> constraintWeight;

  public DomWDegVariableHeuristic(Problem<T> problem) {
    this.problem = problem;
    constraintWeight = new HashMap<>();
  }

  @Override
  public Variable<T> nextVariable(
      Set<Variable<T>> unassignedVariables, Map<Variable<T>, LiveDomainWithContext<T>> liveDomains) {
    double bestMetric = Double.MAX_VALUE;
    Variable<T> bestVariable = null;
    for (Variable<T> variable : unassignedVariables) {
      int domainSize = liveDomains.get(variable).size();
      double weight = getWeight(variable, unassignedVariables);
      double metric = domainSize / weight;
      if (metric < bestMetric) {
        bestMetric = metric;
        bestVariable = variable;
      }
    }
    return bestVariable;
  }

  private double getWeight(Variable<T> variable, Set<Variable<T>> unassignedVariables) {
    double weight = 0.01;
    for (Constraint<T> constraint : problem.getConstraintsFor(variable)) {
      if (hasMoreThanOneItem(Sets.intersection(constraint.getVariables(), unassignedVariables))) {
        weight += constraintWeight.getOrDefault(constraint, 0d);
      }
    }
    return weight;
  }

  private boolean hasMoreThanOneItem(Sets.SetView<Variable<T>> unassignedConstraintVariables) {
    // This is cheaper than size() because we only care if there at least two items.
    Iterator<Variable<T>> itr = unassignedConstraintVariables.iterator();
    if (itr.hasNext()) {
      itr.next();
      return itr.hasNext();
    }
    return false;
  }

  @Override
  public void constraintCausedDomainWipeout(Constraint<T> constraint) {
    double weight = constraintWeight.getOrDefault(constraint, 0d);
    constraintWeight.put(constraint, weight + 1);
  }
}
