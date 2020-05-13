package dataflow.abs;

import static dataflow.abs.ZeroLattice.MAYBE_ZERO;

import java.util.HashMap;

public class VariableToLatticeMap extends HashMap<String, ZeroLattice> {

  @Override
  public ZeroLattice get(Object key) {
    return getOrDefault(key, MAYBE_ZERO);
  }
}
