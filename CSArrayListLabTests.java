import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Assertions;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class CSArrayListLabTests {

    @Test
    public void edgeIndexCases() {
        CSArrayList<Integer> list = new CSArrayList<>();
        list.add(0, 10);
        list.add(0, 5); // [5,10]
        list.add(2, 20); // [5,10,20]
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals(5, list.get(0));
        Assertions.assertEquals(20, list.get(list.size()-1));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.add(5, 1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.remove(3));
    }

    @Test
    public void multipleResizes() {
        CSArrayList<Integer> list = new CSArrayList<>();
        int N = 10_500;
        for (int i = 0; i < N; i++) list.add(i);
        Assertions.assertEquals(N, list.size());
        Assertions.assertEquals(0, list.get(0));
        Assertions.assertEquals(N-1, list.get(N-1));
        Assertions.assertEquals(1234, list.get(1234));
    }

    @Test
    public void searchesWithDuplicatesAndNulls() {
        CSArrayList<String> list = new CSArrayList<>();
        list.add(null);
        list.add("A");
        list.add("B");
        list.add(null);
        list.add("B");

        Assertions.assertEquals(0, list.indexOf(null));
        Assertions.assertEquals(2, list.indexOf("B"));
        Assertions.assertEquals(-1, list.indexOf("Z"));
    }

    @Test
    public void removeObjectBehavior() {
        CSArrayList<String> list = new CSArrayList<>();
        list.add("A"); list.add("B"); list.add("C");
        boolean removed = list.remove("B");
        Assertions.assertTrue(removed);
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("C", list.get(1));
        Assertions.assertFalse(list.remove("Z"));
        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void failFastIterator() {
        CSArrayList<Integer> list = new CSArrayList<>();
        list.add(1); list.add(2); list.add(3);
        Iterator<Integer> it = list.iterator();
        Assertions.assertTrue(it.hasNext());
        list.add(4); // modify outside iterator
        Assertions.assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Disabled("Enable locally to collect timing numbers for the report")
    @Test
    public void microbenchmarkAppendAndGet() {
        int N = 200_000;

        // CSArrayList benchmark
        CSArrayList<Integer> cs = new CSArrayList<>();
        long csStartAdd = System.nanoTime();
        for (int i = 0; i < N; i++) cs.add(i);
        long csEndAdd = System.nanoTime();
        long csStartGet = System.nanoTime();
        long csSum = 0;
        for (int i = 0; i < N; i++) csSum += cs.get(i);
        long csEndGet = System.nanoTime();
        System.out.println("CSArrayList add(ns): " + (csEndAdd - csStartAdd));
        System.out.println("CSArrayList get(ns): " + (csEndGet - csStartGet));
        Assertions.assertEquals((long)N*(N-1)/2, csSum);

        //java.util.Arraylist benchmark
        java.util.ArrayList<Integer> jl = new java.util.ArrayList<>();
        long jlStartAdd = System.nanoTime();
        for (int i = 0; i < N; i++) jl.add(i);
        long jlEndAdd = System.nanoTime();
        long jlStartGet = System.nanoTime();
        long jlSum = 0;
        for (int i = 0; i < N;i++) jlSum += jl.get(i);
        long jlEndGet = System.nanoTime();
        System.out.println("ArrayList add(ns): " + (jlEndAdd - jlStartAdd));
        System.out.println("ArrayList get(ns): " + (jlEndGet - jlStartGet));

        Assertions.assertEquals(csSum, jlSum);
    }
}
