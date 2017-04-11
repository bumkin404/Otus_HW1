package main.java.ru;

import java.util.ArrayList;

public class Main {

    private static int SAMPLE_SIZE = 100;
    private static long SLEEP_INTERVAL = 100;

    public static void main(String... args) {
        objectSizeCalc(new BasicObjectFactory());

        objectSizeCalc(new ByteFactory());

        objectSizeCalc(new StringFactory());

        objectSizeCalc(new EmptyStringFactory());

        objectSizeCalc(new ArrayFactory());

    }

    public static void objectSizeCalc(ObjectFactory factory) {
        String message = new String(factory.getClass().getSimpleName() + " produced " + factory.makeObject().getClass().getSimpleName() + " which take ");
        long size1 = 0;
        long size2 = 0;
        Object handle = factory.makeObject();
        try {
            long startMemory = getMemoryUse();
            handle = null;
            long endMemory = getMemoryUse();
            float approximateSize1 = (startMemory - endMemory);
            size1 = Math.round(approximateSize1);

            Object[] objects = new Object[SAMPLE_SIZE];
            for (int i = 0; i < objects.length; ++i) {
                objects[i] = factory.makeObject();
            }
            startMemory = getMemoryUse();
            objects = null;
            endMemory = getMemoryUse();
            float approximateSize2 = (startMemory - endMemory) / (float)SAMPLE_SIZE;
            size2 = Math.round(approximateSize2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(message + size1 + " (single) :  " + size2 + " (in array)");
    }

    public static String getClassName(Object o) {
        return o.getClass().getName();
    }

    private static long getMemoryUse() {
        collectGarbage();
        collectGarbage();
        long totalMemory = Runtime.getRuntime().totalMemory();
        collectGarbage();
        collectGarbage();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return (totalMemory - freeMemory);
    }

    private static void collectGarbage() {
        try {
            System.gc();
            Thread.currentThread().sleep(SLEEP_INTERVAL);
            System.runFinalization();
            Thread.currentThread().sleep(SLEEP_INTERVAL);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static class BasicObjectFactory implements ObjectFactory{

        @Override
        public Object makeObject() {
            return new Object();
        }
    }

    public static class ByteFactory implements ObjectFactory{

        @Override
        public Object makeObject() {
            return new Byte((byte)21);
        }
    }

    public static class StringFactory implements ObjectFactory{

        @Override
        public Object makeObject() {
            return new String("aaaaaa");
        }
    }

    public static class EmptyStringFactory implements ObjectFactory{

        @Override
        public Object makeObject() {
            return new String("");
        }
    }

    public static class ArrayFactory implements ObjectFactory{

        @Override
        public Object makeObject() {
            ArrayList res = new ArrayList();
            return res;
        }
    }


}
