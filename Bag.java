import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bag<Element> implements Iterable<Element> {
    private Node<Element> first;
    private int n;

    private static class Node<Element> {
        private Element element;
        private Node<Element> next;
    }

    public Bag() {
        first = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void add(Element element) {
        Node<Element> oldfirst = first;
        first = new Node<Element>();
        first.element = element;
        first.next = oldfirst;
        n++;
    }

    public Element getElement() {
        return first.element;
    }

    public Iterator<Element> iterator() {
        return new LinkedIterator(first);
    }

    private class LinkedIterator implements Iterator<Element> {
        private Node<Element> current;

        public LinkedIterator(Node<Element> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }


        public Element next() {
            if (!hasNext()) throw new NoSuchElementException();
            Element element = current.element;
            current = current.next;
            return element;
        }
    }
}
