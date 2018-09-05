package org.docheinstein.sqlbuilder;

import java.util.*;

public class MapMain {

    public static class Person {
        public String name;
        public String password;
        public String other;
    }

    public static void main(String args[]) {
        // Result: the map is really fast, even if the list contains
        // a few items, map finds the item quickly, more than the list.

        long scriptStartMillis = System.currentTimeMillis();
        List<Person> person_list = new ArrayList<>();
        Map<String, Person> person_map = new HashMap<>();

        final int BENCHMARK_COUNT = 1000000;
        final int PERSON_COUNT = 10;


        System.out.println("Filling persons...");

        for (int i = 0; i < PERSON_COUNT; i++) {
            Person p = new Person();
            p.name = p.password = p.other = "P" + i;
            person_list.add(p);
            person_map.put(p.name, p);
        }

        // LIST
        System.out.println("Benchmarking list...");

        long execTimeNano = 0;
        for (int i = 0; i < BENCHMARK_COUNT; i++) {

            String findPersonName = "P" + new Random().nextInt(PERSON_COUNT);

            long startTime = System.nanoTime();

            for (int j = 0; j < person_list.size(); j++) {
                if (person_list.get(j).name.equals(findPersonName))
                    break;
            }

            execTimeNano += System.nanoTime() - startTime;
        }

        System.out.println("[MAP] Finding " + BENCHMARK_COUNT + " times among " + PERSON_COUNT + " persons" +
            " a random person took " + execTimeNano + "ns" + " -> " + execTimeNano / 1000000 + "ms");

        // MAP
        System.out.println("Benchmarking list...");

        execTimeNano = 0;
        for (int i = 0; i < BENCHMARK_COUNT; i++) {

            String findPersonName = "P" + new Random().nextInt(PERSON_COUNT);

            long startTime = System.nanoTime();

            person_map.get(findPersonName);

            execTimeNano += System.nanoTime() - startTime;
        }

        System.out.println("[MAP] Finding " + BENCHMARK_COUNT + " times among " + PERSON_COUNT + " persons" +
            " a random person took " + execTimeNano + "ns" + " -> " + execTimeNano / 1000000 + "ms");

        System.out.println("Script finished in " +
            String.valueOf(System.currentTimeMillis() - scriptStartMillis) + " ms");
    }
}
