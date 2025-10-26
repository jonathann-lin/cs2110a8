package cs2110;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A table of variables and their respective values
 */
public interface VarTable {
    /**
     * Assigns a value to a given variable `var`
     */
    void assign(String var, double value);

    /**
     * Check whether `var` has been assigned to a value yet
     */
    boolean contains(String var);

    /**
     * Get the value of `var`
     */
    Double getValue(String var);

    /**
     * Returns all entries in the variable table
     */
    Set<String> varSet();


    /**
     * Create an empty VarTable
     */
    static VarTable empty() {
        return new MapVarTable();
    }

    /**
     * Create a VarTable associating `value` with variable `name`.
     */
    static VarTable of(String name, double value) {
        assert name != null;

        VarTable ans = new MapVarTable();
        ans.assign(name, value);
        return ans;
    }

    // Below is an implementation of VarTable using a Map. Maps will be introduced in lecture 19.
    // For now, we'll be clients of the Map interface before we know how to implement them.

    /**
     * A VarTable implemented using a HashMap
     */
    class MapVarTable implements VarTable {

        /**
         * The map that contains all mappings between variables and values
         */
        private final Map<String, Double> varTable;

        /**
         * Initializes an empty MapVarTable
         */
        public MapVarTable() {
            varTable = new HashMap<>();
        }

        @Override
        public void assign(String var, double value) {
            varTable.put(var, value);
        }

        @Override
        public boolean contains(String var) {
            return varTable.containsKey(var);
        }

        @Override
        public Double getValue(String var) {
            return varTable.get(var);
        }

        @Override
        public Set<String> varSet() {
            return varTable.keySet();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MapVarTable that = (MapVarTable) o;
            return Objects.equals(varTable, that.varTable);
        }

        @Override
        public int hashCode() {
            return Objects.hash(varTable);
        }
    }

}