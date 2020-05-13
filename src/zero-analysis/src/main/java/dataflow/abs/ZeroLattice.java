package dataflow.abs;

/**
 * Lattice used in the DivisionByZeroAnalysis.
 */
public enum ZeroLattice {

    BOTTOM("bottom") {
        @Override
        public ZeroLattice add(ZeroLattice another) {
            return another.addBottom();
        }

        @Override
        protected ZeroLattice addBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice addNotZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice addZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice addMaybeZero() {
            return BOTTOM;
        }

        @Override
        public ZeroLattice divideBy(ZeroLattice another) {
            return another.divideBottom();
        }

        @Override
        protected ZeroLattice divideBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice divideNotZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice divideZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice divideMaybeZero() {
            return BOTTOM;
        }

        @Override
        public ZeroLattice multiplyBy(ZeroLattice another) {
            return another.multiplyBottom();
        }

        @Override
        protected ZeroLattice multiplyBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice multiplyNotZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice multiplyZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice multiplyMaybeZero() {
            return BOTTOM;
        }

        @Override
        public ZeroLattice subtract(ZeroLattice another) {
            return another.subtractFromBottom();
        }

        @Override
        protected ZeroLattice subtractFromBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice subtractFromNotZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice subtractFromZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice subtractFromMaybeZero() {
            return BOTTOM;
        }

        @Override
        public ZeroLattice supreme(ZeroLattice another) {
            return another;
        }
    },
    NOT_ZERO("not-zero") {
        @Override
        public ZeroLattice add(ZeroLattice another) {
            return another.addNotZero();
        }

        @Override
        protected ZeroLattice addBottom() {
            return BOTTOM;
        }

        @Override
        public ZeroLattice addNotZero() {
            return MAYBE_ZERO;
        }

        @Override
        protected ZeroLattice addZero() {
            return NOT_ZERO;
        }

        @Override
        protected ZeroLattice addMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice divideBy(ZeroLattice another) {
            return another.divideNotZero();
        }

        @Override
        protected ZeroLattice divideBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice divideNotZero() {
            return NOT_ZERO;
        }

        @Override
        protected ZeroLattice divideZero() {
            return ZERO;
        }

        @Override
        protected ZeroLattice divideMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice multiplyBy(ZeroLattice another) {
            return another.multiplyNotZero();
        }

        @Override
        protected ZeroLattice multiplyBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice multiplyNotZero() {
            return NOT_ZERO;
        }

        @Override
        protected ZeroLattice multiplyZero() {
            return ZERO;
        }

        @Override
        protected ZeroLattice multiplyMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice subtract(ZeroLattice another) {
            return another.subtractFromNotZero();
        }

        @Override
        protected ZeroLattice subtractFromBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice subtractFromNotZero() {
            return MAYBE_ZERO;
        }

        @Override
        protected ZeroLattice subtractFromZero() {
            return NOT_ZERO;
        }

        @Override
        protected ZeroLattice subtractFromMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice supreme(ZeroLattice another) {
            return another == BOTTOM || another == this ? this : MAYBE_ZERO;
        }
    },
    ZERO("zero") {
        @Override
        public ZeroLattice add(ZeroLattice another) {
            return another.addZero();
        }

        @Override
        protected ZeroLattice addBottom() {
            return BOTTOM;
        }

        @Override
        public ZeroLattice addNotZero() {
            return NOT_ZERO;
        }

        @Override
        protected ZeroLattice addZero() {
            return ZERO;
        }

        @Override
        protected ZeroLattice addMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice divideBy(ZeroLattice another) {
            return another.divideZero();
        }

        @Override
        protected ZeroLattice divideBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice divideNotZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice divideZero() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice divideMaybeZero() {
            return BOTTOM;
        }

        @Override
        public ZeroLattice multiplyBy(ZeroLattice another) {
            return another.multiplyZero();
        }

        @Override
        protected ZeroLattice multiplyBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice multiplyNotZero() {
            return ZERO;
        }

        @Override
        protected ZeroLattice multiplyZero() {
            return ZERO;
        }

        @Override
        protected ZeroLattice multiplyMaybeZero() {
            return ZERO;
        }

        @Override
        public ZeroLattice subtract(ZeroLattice another) {
            return another.subtractFromZero();
        }

        @Override
        protected ZeroLattice subtractFromBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice subtractFromNotZero() {
            return NOT_ZERO;
        }

        @Override
        protected ZeroLattice subtractFromZero() {
            return ZERO;
        }

        @Override
        protected ZeroLattice subtractFromMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice supreme(ZeroLattice another) {
            return another == BOTTOM || another == this ? this : MAYBE_ZERO;
        }
    },
    MAYBE_ZERO("maybe-zero") {
        @Override
        public ZeroLattice add(ZeroLattice another) {
            return another.addMaybeZero();
        }

        @Override
        protected ZeroLattice addBottom() {
            return BOTTOM;
        }

        @Override
        public ZeroLattice addNotZero() {
            return MAYBE_ZERO;
        }

        @Override
        protected ZeroLattice addZero() {
            return MAYBE_ZERO;
        }

        @Override
        protected ZeroLattice addMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice divideBy(ZeroLattice another) {
            return another.divideMaybeZero();
        }

        @Override
        protected ZeroLattice divideBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice divideNotZero() {
            return NOT_ZERO;
        }

        @Override
        protected ZeroLattice divideZero() {
            return ZERO;
        }

        @Override
        protected ZeroLattice divideMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice multiplyBy(ZeroLattice another) {
            return another.multiplyMaybeZero();
        }

        @Override
        protected ZeroLattice multiplyBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice multiplyNotZero() {
            return MAYBE_ZERO;
        }

        @Override
        protected ZeroLattice multiplyZero() {
            return ZERO;
        }

        @Override
        protected ZeroLattice multiplyMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice subtract(ZeroLattice another) {
            return another.subtractFromMaybeZero();
        }

        @Override
        protected ZeroLattice subtractFromBottom() {
            return BOTTOM;
        }

        @Override
        protected ZeroLattice subtractFromNotZero() {
            return MAYBE_ZERO;
        }

        @Override
        protected ZeroLattice subtractFromZero() {
            return MAYBE_ZERO;
        }

        @Override
        protected ZeroLattice subtractFromMaybeZero() {
            return MAYBE_ZERO;
        }

        @Override
        public ZeroLattice supreme(ZeroLattice another) {
            return this;
        }
    };

    private final String name;

    @Override
    public String toString() {
        return this.name;
    }

    ZeroLattice(String name) {
        this.name = name;
    }

    abstract public ZeroLattice add(ZeroLattice another);

    abstract protected ZeroLattice addBottom();
    abstract protected ZeroLattice addNotZero();
    abstract protected ZeroLattice addZero();
    abstract protected ZeroLattice addMaybeZero();

    abstract public ZeroLattice divideBy(ZeroLattice another);

    abstract protected ZeroLattice divideBottom();
    abstract protected ZeroLattice divideNotZero();
    abstract protected ZeroLattice divideZero();
    abstract protected ZeroLattice divideMaybeZero();

    abstract public ZeroLattice multiplyBy(ZeroLattice another);

    abstract protected ZeroLattice multiplyBottom();
    abstract protected ZeroLattice multiplyNotZero();
    abstract protected ZeroLattice multiplyZero();
    abstract protected ZeroLattice multiplyMaybeZero();

    abstract public ZeroLattice subtract(ZeroLattice another);

    abstract protected ZeroLattice subtractFromBottom();
    abstract protected ZeroLattice subtractFromNotZero();
    abstract protected ZeroLattice subtractFromZero();
    abstract protected ZeroLattice subtractFromMaybeZero();

    abstract public ZeroLattice supreme(ZeroLattice another);
}
