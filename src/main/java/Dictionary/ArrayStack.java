package Dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayStack<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;

    public ArrayStack() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < a.length; i++) {
            copy[i] = a[i];
        }
        a = copy;
    }

    public void push(Item item) {
        if (n == a.length) {
            resize(a.length * 2);
        }
        a[n++] = item;
    }

    public Item pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = a[n - 1];
        a[n - 1] = null;
        n--;
        if (n > 0 && n == a.length / 4) {
            resize(n / 2);
        }
        return item;
    }

    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return a[n - 1];
    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int i;

        public ArrayIterator() {
            i = 0;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[i++];
        }

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public static void main(String[] args) {

    }
}
