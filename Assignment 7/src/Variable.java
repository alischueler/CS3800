public class Variable {
    String var;

    public Variable(String s) {
        this.var = s;
    }

    @Override
    public String toString() {
        return this.var;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        Variable other2 = (Variable)other;
        return var.equals(other2.var);
    }

}
