package lesson_01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class lesson_01_App {

    public static <T> void Change(T[] array, int ind1, int ind2) {
        T elem = array[ind1];
        array[ind1] = array[ind2];
        array[ind2] = elem;
    }

    public static <T> List<T> toList(T[] array) {
        List<T> list = new ArrayList<>();
        for (T elem : array) {
            list.add(elem);
        }
        return list;
    }

    public static void main(String[] args) {
        //Задача №1 - поменять местами два элемента массива
        Integer[] arrayInt = {1, 2, 3};
        Change( arrayInt, 0, 1);
        System.out.println( "результат 1.1: " + Arrays.toString( arrayInt ) );

        String[] arrayString = {"one", "two", "three"};
        Change( arrayString, 0, 1);
        System.out.println( "результат 1.2: " + Arrays.toString( arrayString ) );

        System.out.println();

        //Задача №2 - массив в ArrayList
        Integer[] arrayToListInt = {1, 2, 3};
        // библиотечный метод
        List<Integer> list = Arrays.asList(arrayToListInt);
        System.out.println("результат 2.1a: " + list);

        // с моим методом
        System.out.println( "результат 2.1b: " + toList(arrayToListInt).toString() );

        Character[] arrayToListChar = {'A', 'B', 'C'};
        System.out.println( "результат 2.2: " + toList(arrayToListChar) );
    }
}
