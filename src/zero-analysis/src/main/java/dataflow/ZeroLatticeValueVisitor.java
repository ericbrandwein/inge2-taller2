package dataflow;

import java.util.Map;

import dataflow.abs.ZeroLattice;
import dataflow.utils.AbstractValueVisitor;
import dataflow.utils.ValueVisitor;
import soot.Local;

public class ZeroLatticeValueVisitor extends AbstractValueVisitor<ZeroLattice> {

  private final Map<String, ZeroLattice> variables;
  private ZeroLattice resolvedValue = ZeroLattice.MAYBE_ZERO;
  private Boolean possibleDivisionByZero;

  public ZeroLatticeValueVisitor(Map<String, ZeroLattice> variables) {
    this.variables = variables;
    this.possibleDivisionByZero = false;
  }

  @Override
  public void visitLocal(Local variable) {
    resolvedValue = variables.get(variable.getName());
  }

  @Override
  public void visitDivExpression(ZeroLattice leftOperand, ZeroLattice rightOperand) {
    possibleDivisionByZero =
            rightOperand == ZeroLattice.ZERO || rightOperand == ZeroLattice.MAYBE_ZERO;
    resolvedValue = leftOperand.divideBy(rightOperand);
  }

  @Override
  public void visitMulExpression(ZeroLattice leftOperand, ZeroLattice rightOperand) {
    resolvedValue = leftOperand.multiplyBy(rightOperand);
  }

  @Override
  public void visitSubExpression(ZeroLattice leftOperand, ZeroLattice rightOperand) {
    resolvedValue = leftOperand.subtract(rightOperand);
  }

  @Override
  public void visitAddExpression(ZeroLattice leftOperand, ZeroLattice rightOperand) {
    resolvedValue = leftOperand.add(rightOperand);
  }

  @Override
  public void visitIntegerConstant(int value) {
    if (value == 0) {
      resolvedValue = ZeroLattice.ZERO;
    } else {
      resolvedValue = ZeroLattice.NOT_ZERO;
    }
  }

  @Override
  public ZeroLattice done() {
    return resolvedValue;
  }

  @Override
  public ValueVisitor<ZeroLattice> cloneVisitor() {
    return new ZeroLatticeValueVisitor(variables);
  }

  public Boolean isPossiblyDividingByZero() {
    return possibleDivisionByZero;
  }
}
