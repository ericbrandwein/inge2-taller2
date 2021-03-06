package dataflow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import dataflow.abs.VariableToLatticeMap;
import dataflow.abs.ZeroLattice;
import soot.Local;
import soot.Unit;
import soot.jimple.DefinitionStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

/**
 * Division by zero analysis
 */
public class DivisionByZeroAnalysis extends ForwardFlowAnalysis<Unit, VariableToLatticeMap> {

  private VariableToLatticeMap localsAsLattice = new VariableToLatticeMap();
  private HashMap<Unit, Boolean> possibleDivisionByZero = new HashMap<>();

  public DivisionByZeroAnalysis(UnitGraph graph) {
    super(graph);
    doAnalysis();
  }

  protected void flowThrough(VariableToLatticeMap in, Unit unit, VariableToLatticeMap out) {
    // Local all values from input
    out.clear();
    out.putAll(in);

    if (unit instanceof DefinitionStmt) {
      DefinitionStmt definition = (DefinitionStmt) unit;

      // Assume just local variables
      Local variable = (Local) definition.getLeftOp();
      ZeroLatticeValueVisitor visitor = new ZeroLatticeValueVisitor(in);
      ZeroLattice resolvedValue = visitor.visit(definition.getRightOp()).done();

      if (visitor.isPossiblyDividingByZero()) {
        possibleDivisionByZero.put(unit, true);
      }

      // Set in flowed values
      out.put(variable.getName(), resolvedValue);
    }
  }

  protected VariableToLatticeMap newInitialFlow() {
    VariableToLatticeMap newInstance = new VariableToLatticeMap();
    newInstance.putAll(localsAsLattice);
    return newInstance;
  }

  protected void merge(VariableToLatticeMap input1, VariableToLatticeMap input2, VariableToLatticeMap output) {
    Set<String> allKeys = new HashSet<>(input1.keySet());
    allKeys.addAll(input2.keySet());
    allKeys.forEach((key) -> {
      ZeroLattice firstZeroLattice = input1.get(key);
      ZeroLattice secondZeroLattice = input2.get(key);
      output.put(key, firstZeroLattice.supreme(secondZeroLattice));
    });
  }

  protected void copy(VariableToLatticeMap source, VariableToLatticeMap dest) {
    dest.clear();
    dest.putAll(source);
  }

  public boolean unitIsOffending(Unit unit) {
    return possibleDivisionByZero.getOrDefault(unit, false);
  }

}
