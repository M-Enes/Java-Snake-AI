/***
 * A 2D vector class with integer coordinates {@code x, y}
 */
public class Vector2i implements Comparable<Vector2i> {
    public int x;
    public int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i(Vector2i other) {
        this.x = other.x;
        this.y = other.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2i other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2i getCopy() {
        return new Vector2i(x, y);
    }

    public Vector2i addLocal(Vector2i other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2i subLocal(Vector2i other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2i mulLocal(Vector2i other) {
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    /**
     * If other.x or other.y is 0, then does nothing.
     * 
     * @param other
     * @return this objects reference
     */
    public Vector2i divLocal(Vector2i other) {
        if (other.x == 0 || other.y == 0)
            return this;
        this.x /= other.x;
        this.y /= other.y;
        return this;
    }

    public Vector2i add(Vector2i other) {
        return new Vector2i(x + other.x, y + other.y);
    }

    public Vector2i sub(Vector2i other) {
        return new Vector2i(x - other.x, y - other.y);
    }

    public Vector2i mul(Vector2i other) {
        return new Vector2i(x * other.x, y * other.y);
    }

    /**
     * 
     * @param other
     * @return null if other.x or other.y is 0, otherwise a new Vector2i with the
     *         result
     */
    public Vector2i div(Vector2i other) {
        if (other.x == 0 || other.y == 0)
            return null;
        return new Vector2i(x / other.x, y / other.y);
    }

    @Override
    public String toString() {
        return "Vector2i(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Vector2i == false)
            return false;

        Vector2i other = (Vector2i) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        int sum = x + y;
        return sum * (sum + 1)/2 + x;
    }

    @Override
    public int compareTo(Vector2i other) {
        int result = Integer.compare(this.x, other.x);
        if (result == 0) {
            result = Integer.compare(this.y, other.y);
        }
        return result;
    }



}
