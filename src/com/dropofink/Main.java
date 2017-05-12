package com.dropofink;

import com.dropofink.constructive.Search;
import com.dropofink.constructive.SimpleHeuristics;
import com.dropofink.problems.NQueensBinary;

public class Main {
  public static void main(String[] args) {
    NQueensBinary problem = NQueensBinary.create(10);
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
}
