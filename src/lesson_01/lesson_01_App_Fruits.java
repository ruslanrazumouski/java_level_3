package lesson_01;

public class lesson_01_App_Fruits {
    public static void main(String[] args) {
        Apple apple = new Apple(1.0f);
        Orange orange = new Orange(1.5f);

        Box<Apple> boxForApple = new Box<>(apple, 30);
        Box<Orange> boxForOrange = new Box<>(orange, 20);

        System.out.println( "Кол-во яблок в коробке: " + boxForApple.getCount() );
        System.out.println( "Вес коробки с яблоками: " + boxForApple.getWeight() );

        System.out.println();

        System.out.println( "Кол-во апельсинов в коробке: " + boxForOrange.getCount() );
        System.out.println( "Вес коробки с апельсинами: " + boxForOrange.getWeight() );

        System.out.println();

        if ( boxForApple.compareBox(boxForOrange) ) {
            System.out.println( "Коробки с яблоками и апельсинами имеют равный вес");
        } else {
            System.out.println( "Коробки с яблоками и апельсинами имеют НЕ равный вес");
        }

        System.out.println();

        boxForApple.putInBox(2);
        System.out.println( "Кол-во яблок в коробке стало: " + boxForApple.getCount() );
        System.out.println( "Вес коробки с яблоками стал: " + boxForApple.getWeight() );
        if ( boxForApple.compareBox(boxForOrange) ) {
            System.out.println( "Коробки с яблоками и апельсинами имеют равный вес");
        } else {
            System.out.println( "Коробки с яблоками и апельсинами имеют НЕ равный вес");
        }

        System.out.println();

        boxForApple.outFromBox(2);
        System.out.println( "Кол-во яблок в коробке стало: " + boxForApple.getCount() );
        System.out.println( "Вес коробки с яблоками стал: " + boxForApple.getWeight() );
        if ( boxForApple.compareBox(boxForOrange) ) {
            System.out.println( "Коробки с яблоками и апельсинами имеют равный вес");
        } else {
            System.out.println( "Коробки с яблоками и апельсинами имеют НЕ равный вес");
        }

        System.out.println();

        Box<Apple> boxForAppleAnother = new Box<>(apple, 0);
        boxForApple.pullInAnotherBox(boxForAppleAnother, 2);
        System.out.println( "Кол-во яблок в первой коробке стало " + boxForApple.getCount() );
        System.out.println( "Кол-во яблок во второй коробке стало " + boxForAppleAnother.getCount() );
    }
}
