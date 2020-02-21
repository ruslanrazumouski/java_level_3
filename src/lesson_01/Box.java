package lesson_01;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {
    private T obj;
    private List<T> list;
    private int count;

    Box(T obj, int count) {
        this.obj = obj;
        list = new ArrayList<>();
        putInBox(count);
    }

    public void putInBox(int count) {
        this.count += count;
        for (int i=0; i<count; i++) {
            list.add(obj);
        }
    }

    public void outFromBox(int count) {
        this.count -= count;
        for (int i=0; i<count; i++) {
            list.remove(obj);
        }
    }

    public float getWeight() {
        float sum = 0.0f;
        for (T item : list) {
            sum += item.getWeight();
        }
        return sum;
    }

    public boolean compareBox(Box<?> anotherBox) {
        return this.getWeight() == anotherBox.getWeight();
    }

    public void pullInAnotherBox(Box<T> anotherBox, int count) {
        this.outFromBox(count);
        anotherBox.putInBox(count);
    }

    public int getCount() {
        return count;
    }

}
