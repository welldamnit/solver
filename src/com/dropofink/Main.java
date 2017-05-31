package com.dropofink;

import com.dropofink.constructive.DomWDegVariableHeuristic;
import com.dropofink.constructive.RestartSchedule;
import com.dropofink.constructive.Search;
import com.dropofink.constructive.SimpleHeuristics;
import com.dropofink.problems.MagicSquareBinaryNotEquals;
import com.dropofink.problems.NQueensBinary;

public class Main {
  public static void main(String[] args) {
//    nQueens();
    magicSquare();
  }

  private static void nQueens() {
    NQueensBinary problem = NQueensBinary.create(12);

    Search<Integer> search =
        new Search<>(problem, new SimpleHeuristics.FirstVariable<>(), new SimpleHeuristics.FirstValue<>());
    System.out.println("Starting search");
    int solutions = 0;
    while (search.hasNextSolution()) {
      System.out.println(search.nextSolution());
      solutions++;
    }
    System.out.println("Done. States visited " + search.getStatesVisited() + ", solutions found " + solutions);
  }

  private static void magicSquare() {
    MagicSquareBinaryNotEquals problem = MagicSquareBinaryNotEquals.create(7);
//    SimpleHeuristics.SmallestDomain<Integer> variableHeuristic = new SimpleHeuristics.SmallestDomain<>();
    DomWDegVariableHeuristic<Integer> variableHeuristic = new DomWDegVariableHeuristic<>(problem);
    Search<Integer> search = new Search<>(problem, variableHeuristic, new SimpleHeuristics.FirstValue<>());
    search.changeRestartSchedule(RestartSchedule.increasingRate(1000, 1.1));
    System.out.println("Starting search");
    if (search.hasNextSolution()) {
      System.out.println(problem.solutionToString(search.nextSolution()));
      System.out.println("Done. States visited " + search.getStatesVisited());
    } else {
      System.out.println("No solutions found. States visited " + search.getStatesVisited());
    }
  }
}
