package daa.structures;

import daa.metrics.IntList;
import daa.metrics.Metrics;

public class MyLinkedList implements IntList {
    private static class Node {
        int val;
        Node prev;
        Node next;

        Node(int val) {
            this.val = val;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final Metrics metrics;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.metrics = new Metrics();
    }

    @Override
    public Metrics getMetrics() {
        return metrics;
    }

    @Override
    public int size() {
        return size;
    }

    private Node getNode(int index) {
        if (index < (size >> 1)) {
            Node curr = head;
            for (int i = 0; i < index; i++) {
                metrics.steps++;
                curr = curr.next;
            }
            metrics.steps++;
            return curr;
        } else {
            Node curr = tail;
            for (int i = size - 1; i > index; i--) {
                metrics.steps++;
                curr = curr.prev;
            }
            metrics.steps++;
            return curr;
        }
    }

    @Override
    public void add(int element) {
        Node newNode = new Node(element);
        if (tail == null) {
            head = tail = newNode;
            metrics.moves += 2;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
            metrics.moves += 3;
        }
        size++;
    }

    @Override
    public void add(int index, int element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(element);
            return;
        }
        Node newNode = new Node(element);
        if (index == 0) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            metrics.moves += 3;
        } else {
            Node target = getNode(index);
            Node prevNode = target.prev;

            newNode.next = target;
            newNode.prev = prevNode;
            prevNode.next = newNode;
            target.prev = newNode;
            metrics.moves += 4;
        }
        size++;
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node target;
        if (index == 0) {
            target = head;
            head = head.next;
            if (head != null) {
                head.prev = null;
                metrics.moves++;
            } else {
                tail = null;
            }
            metrics.moves++;
        } else if (index == size - 1) {
            target = tail;
            tail = tail.prev;
            if (tail != null) {
                tail.next = null;
                metrics.moves++;
            } else {
                head = null;
            }
            metrics.moves++;
        } else {
            target = getNode(index);
            Node prevNode = target.prev;
            Node nextNode = target.next;
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            metrics.moves += 2;
        }
        size--;
        return target.val;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNode(index).val;
    }

    @Override
    public boolean contains(int element) {
        Node curr = head;
        while (curr != null) {
            metrics.steps++;
            metrics.comparisons++;
            if (curr.val == element) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }
}