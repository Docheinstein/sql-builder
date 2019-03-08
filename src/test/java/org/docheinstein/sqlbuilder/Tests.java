package org.docheinstein.sqlbuilder;

import org.docheinstein.sqlbuilder.expressions.Operators;
import org.docheinstein.sqlbuilder.models.AnonymousColumn;

public class Tests {
    public static void main(String[] args) {
        String dog = "dog";
        String cat = "cat";
        System.out.println(
            Operators.and(dog, cat).toSql()
        );
        System.out.println(
            new AnonymousColumn("C1").ge(dog).and(
                new AnonymousColumn("C2").and(Operators.and(dog, cat))
            ).toSql()
        );
    }
}
