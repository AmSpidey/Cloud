package chmura;

import java.util.Collection;
import java.util.function.BiPredicate;

public class Chmura {

    private BiPredicate<Integer, Integer> stan;

    public Chmura() {
        stan = ((x, y) -> false);
    }

    public Chmura(BiPredicate<Integer, Integer> stan) {
        this.stan = stan;
    }

    public synchronized Byt ustaw(int x, int y) throws InterruptedException {
        Byt byt = new Byt(x, y, this);
        while (this.stan.test(x, y)) {
            wait();
        }
        this.stan = stan.or((a, b) -> a == x && b == y);
        notifyAll();
        return byt;
    }

    public synchronized int[] miejsce(Byt byt) {
        if (!stan.test(byt.getX(), byt.getY()) || byt.getMojaChmura() != this) return null;
        return new int[]{byt.getX(), byt.getY()};
    }

    public synchronized void kasuj(Byt byt) throws NiebytException {
        int x = byt.getX();
        int y = byt.getY();
        if (!stan.test(x, y) || byt.getMojaChmura() != this) throw new NiebytException();

        byt.samobójstwo();
        this.stan = stan.and((a, b) -> a != x || b != y);
        notifyAll();
    }

    private synchronized boolean można(Collection<Byt> byty, int dx, int dy) throws NiebytException {
        boolean można = true;
        for (Byt i : byty) {
            if (i.getMojaChmura() != this || !this.stan.test(i.getX(), i.getY())) {
                throw new NiebytException();
            }
        }
        for (Byt j : byty) {
            if (this.stan.test(j.getX() + dx, j.getY() + dy)
                    && !zawiera(byty, j.getX() + dx, j.getY() + dy)) {
                można = false;
            }
        }
        return można;
    }

    public synchronized void przestaw(Collection<Byt> byty, int dx, int dy) throws NiebytException, InterruptedException {
        while (!można(byty, dx, dy)) {
            wait();
        }
        for (Byt k : byty) {
            int x = k.getX();
            int y = k.getY();
            this.stan = stan.and((a, b) -> a != x || b != y);
            k.wektor(dx, dy);
        }
        for (Byt k : byty) {
            int x = k.getX();
            int y = k.getY();
            this.stan = stan.or((a, b) -> a == x && b == y);
        }
        notifyAll();
    }

    private synchronized boolean zawiera(Collection<Byt> byty, int x, int y) {
        for (Byt j : byty) {
            if (j.getY() == y && j.getX() == x) {
                return true;
            }
        }
        return false;
    }
}



