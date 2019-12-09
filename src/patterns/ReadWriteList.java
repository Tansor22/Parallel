package patterns;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static patterns.Utils.*;

public class ReadWriteList<T> implements List<T> {
    private List<T> list = new ArrayList<>();
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public ReadWriteList(T... initialElements) {
        list.addAll(Arrays.asList(initialElements));
    }

    @Override
    public int size() {
        return capture(rwLock.readLock(), list::size);
    }

    @Override
    public boolean isEmpty() {
        return capture(rwLock.readLock(), list::isEmpty);
    }

    @Override
    public boolean contains(Object o) {
        return capture(rwLock.readLock(), () -> list.contains(o));
    }

    @Override
    public Iterator<T> iterator() {
        return capture(rwLock.readLock(), list::iterator);
    }

    @Override
    public Object[] toArray() {
        return capture(rwLock.readLock(), () -> list.toArray());
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return capture(rwLock.readLock(), () -> list.toArray(a));
    }

    @Override
    public boolean add(T t) {
        return capture(rwLock.writeLock(), () -> list.add(t));
    }

    @Override
    public boolean remove(Object o) {
        return capture(rwLock.writeLock(), () -> list.remove(o));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return capture(rwLock.writeLock(), () -> list.containsAll(c));
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return capture(rwLock.writeLock(), () -> list.addAll(c));
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return capture(rwLock.writeLock(), () -> list.addAll(index, c));
    }

    public boolean add(T... elements) {
        return capture(rwLock.writeLock(), () -> list.addAll(Arrays.asList(elements)));
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return capture(rwLock.writeLock(), () -> list.removeAll(c));
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return capture(rwLock.writeLock(), () -> list.retainAll(c));
    }

    @Override
    public void clear() {
        capture(rwLock.writeLock(), () -> {
            list.clear();
            return 0;
        });
    }

    @Override
    public T get(int index) {
        return capture(rwLock.readLock(), () -> list.get(index));
    }

    public T getRandom() {
        return capture(rwLock.readLock(), () -> {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return list.get(Utils.getRandomInt(size()));
        });
    }

    @Override
    public T set(int index, T element) {
        return capture(rwLock.writeLock(), () -> list.set(index, element));
    }

    @Override
    public void add(int index, T element) {
        capture(rwLock.writeLock(), () -> {
            list.add(index, element);
            return 0;
        });
    }

    @Override
    public T remove(int index) {
        return capture(rwLock.writeLock(), () -> list.remove(index));
    }

    @Override
    public int indexOf(Object o) {
        return capture(rwLock.writeLock(), () -> list.indexOf(o));
    }

    @Override
    public int lastIndexOf(Object o) {
        return capture(rwLock.writeLock(), () -> list.lastIndexOf(o));
    }

    @Override
    public ListIterator<T> listIterator() {
        return capture(rwLock.writeLock(), () -> list.listIterator());
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return capture(rwLock.writeLock(), () -> list.listIterator(index));
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return capture(rwLock.writeLock(), () -> list.subList(fromIndex, toIndex));
    }
}
