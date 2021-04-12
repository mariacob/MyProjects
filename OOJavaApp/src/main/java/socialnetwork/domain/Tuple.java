package socialnetwork.domain;

import java.util.Objects;


/**
 * Define a Tuple o generic type entities
 * @param <E1> - tuple first entity type
 * @param <E2> - tuple second entity type
 */
public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    /**
     * Constructor
     * @param e1
     * @param e2
     */
    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     *
     * @return left side
     */
    public E1 getLeft() {
        return e1;
    }

    /**
     *
     * @param e1
     */
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    /**
     *
     * @return right side
     */
    public E2 getRight() {
        return e2;
    }

    /**
     *
     * @param e2
     */
    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "" + e1 + ";" + e2;

    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return (this.e1.equals(((Tuple) obj).e1) && this.e2.equals(((Tuple) obj).e2) || (this.e2.equals(((Tuple) obj).e1) && (this.e1.equals(((Tuple) obj).e2))));
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return e1.hashCode() + e2.hashCode();
    }
}