package org.docheinstein.sqlbuilder.example;

import org.docheinstein.sqlbuilder.commons.SqlBuilder;
import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.example.dog.DogTable;
import org.docheinstein.sqlbuilder.example.person.PersonTable;
import org.docheinstein.sqlbuilder.example.person.PersonTuple;
import org.docheinstein.sqlbuilder.models.Table;

import static org.docheinstein.sqlbuilder.example.dog.DogTable.NICKNAME;
import static org.docheinstein.sqlbuilder.example.dog.DogTable.OWNER;
import static org.docheinstein.sqlbuilder.example.person.PersonTable.AGE;
import static org.docheinstein.sqlbuilder.example.person.PersonTable.NAME;
import static org.docheinstein.sqlbuilder.example.person.PersonTable.SURNAME;

public class SqlBuilderPrintExample {

    // DB Tables
    private final Table PERSON_TABLE = new PersonTable();
    private final Table DOG_TABLE = new DogTable();

    public static void main(String[] args) {
        SqlBuilder.setLanguage(SqlLanguage.MySQL);
        SqlBuilderLogger.enable(true);
        SqlBuilderLogger.addListener(message -> System.out.println(">> " + message));

        SqlBuilderPrintExample sqlEx = new SqlBuilderPrintExample();

        sqlEx.examplePrint();
    }

    public void examplePrint() {
        int stmtCounter = 0;

        // ----- CREATE TABLE -------

        /*
            CREATE TABLE IF NOT EXISTS Person (
                Id INTEGER PRIMARY KEY AUTO_INCREMENT,
                Name VARCHAR(32) NOT NULL,
                Surname VARCHAR(32) NOT NULL,
                Age INTEGER,
                CHECK (Person.Age > ?)
            );
         */
        printBegin(++stmtCounter);
        print(
            PERSON_TABLE
                .create()
                .ifNotExists()
                .toSql()
        );
        printEnd(stmtCounter);

        /*
            CREATE TABLE IF NOT EXISTS Dog (
                Owner INTEGER,
                Number INTEGER,
                Name VARCHAR(32) NOT NULL,
                PRIMARY KEY (Owner, Number),
                FOREIGN KEY (Owner) REFERENCES Person(Id)
                    ON DELETE CASCADE ON UPDATE CASCADE,
                CHECK (Dog.Number > ?)
            );
         */
        printBegin(++stmtCounter);
        print(
            DOG_TABLE
                .create()
                .ifNotExists()
                .toSql()
        );
        printEnd(stmtCounter);

        // ----- INSERT -------
        // |___  FROM VALUES

        /*
            INSERT INTO Person VALUES (?, ?, ?, ?)
         */

        printBegin(++stmtCounter);
        print(
            PERSON_TABLE
                .insert()
                .values(null, "John", "Smith", 15)
                .toSql()
        );
        printEnd(stmtCounter);

        // ----- INSERT -------
        // |___  FROM TUPLE

        /*
            INSERT INTO Person VALUES (null, 'John', 'White', 19);
         */

        PersonTuple johnWhite = new PersonTuple();
        johnWhite.id = null;
        johnWhite.name = "John";
        johnWhite.surname = "White";
        johnWhite.age = 19;

        printBegin(++stmtCounter);
        print(
            PERSON_TABLE
                .insert()
                .valuesFromTuples(johnWhite) // assign the VALUES from the tuple
                .toSql()
        );
        printEnd(stmtCounter);

        // ----- UPDATE -------
        // |___  FROM TUPLE

        /*
            UPDATE Person
            SET Age = ?
            WHERE (((Person.Name = ?) AND (Person.Surname = ?)))
         */

        PersonTuple olderJohnWhite = new PersonTuple();
        olderJohnWhite.age = 20;

        printBegin(++stmtCounter);
        print(
            PERSON_TABLE
                .update()
                .setFromTuple(olderJohnWhite)
                .where(
                    NAME.eq("John").and(SURNAME.eq("White"))
                )
                .toSql()
        );
        printEnd(stmtCounter);

        // ----- SELECT -------

        /*
            SELECT * FROM Person
         */

        printBegin(++stmtCounter);
        print(
            PERSON_TABLE
                .select("*")
                .toSql()
        );
        printEnd(stmtCounter);


        /*
            SELECT DISTINCT Person.Name
            FROM Person
            WHERE ((Person.Age <= ?))
         */

        printBegin(++stmtCounter);
        print(
            PERSON_TABLE
                .select(NAME)
                .distinct()
                .where(AGE.le(18))
                .toSql()
        );
        printEnd(stmtCounter);

         /*
            SELECT *
            FROM Person
            ORDER BY Person.Age ASC
         */

        printBegin(++stmtCounter);
        print(
            PERSON_TABLE
                .select("*")
                .orderByAsc(AGE)
                .toSql()
        );
        printEnd(stmtCounter);

        /*
            SELECT Dog.Owner, Dog.NickName
            FROM Dog
            INNER JOIN Person ON Person.Id = Dog.Owner
         */

        printBegin(++stmtCounter);
        print(
            DOG_TABLE
                .select(OWNER, NICKNAME)
                .innerJoin(PersonTable.ID, DogTable.OWNER)
                .toSql()
        );
        printEnd(stmtCounter);

        /*
            SELECT COUNT(*)
            FROM Dog
            INNER JOIN Person ON Person.Id = Dog.Owner
            WHERE ((Person.Age >= ?))
            GROUP BY Person.Id
         */

        printBegin(++stmtCounter);
        print(
            DOG_TABLE
                .select("COUNT(*)")
                .innerJoin(PersonTable.ID, DogTable.OWNER)
                .where(PersonTable.AGE.ge(18))
                .groupBy(PersonTable.ID)
                .toSql()
        );
        printEnd(stmtCounter);

        /*
            SELECT Dog.NickName
            FROM Dog
            INNER JOIN Person ON Person.Id = Dog.Owner
            WHERE ((Person.Age > ?)))
            UNION (
                SELECT Dog.NickName
                FROM Dog INNER
                JOIN Person ON Person.Id = Dog.Owner
                WHERE ((Person.Surname = ?))
            )

         */

        printBegin(++stmtCounter);
        print(
            DOG_TABLE
                .select(NICKNAME)
                .innerJoin(PersonTable.ID, DogTable.OWNER)
                .where(PersonTable.AGE.gt(18))
                .union(
                    DOG_TABLE
                        .select(NICKNAME)
                        .innerJoin(PersonTable.ID, DogTable.OWNER)
                        .where(PersonTable.SURNAME.eq("Smith"))
                )
                .toSql()
        );
        printEnd(stmtCounter);

         /*
            SELECT Dog.NickName
            FROM Person
            INNER JOIN Dog ON Dog.Owner = Person.Id
            WHERE ((Person.Age >= ALL (
                SELECT Person.Age FROM Person))
            )
            LIMIT 0, 10

         */

        printBegin(++stmtCounter);
        print(
            PERSON_TABLE
                .select(NICKNAME)
                .innerJoin(DogTable.OWNER, PersonTable.ID)
                .where(
                    AGE.ge(
                        PERSON_TABLE
                            .select(AGE)
                            .all()
                    )
                )
                .limit(10)
                .toSql()
        );
        printEnd(stmtCounter);

        /*
            SELECT Person.Surname
            FROM Person
            WHERE (NOT EXISTS (
                SELECT *
                FROM Dog WHERE ((
                Dog.Owner = Person.Id))
            ))
            LIMIT 0, 10

         */

        printBegin(++stmtCounter);
        print(
            PERSON_TABLE
                .select(SURNAME)
                .where(
                    DOG_TABLE
                        .select("*")
                        .where(DogTable.OWNER.eq(PersonTable.ID))
                        .notExists()
                )
                .limit(10)
                .toSql()
        );
        printEnd(stmtCounter);
    }

    private static void printBegin(int id) { print("BEGIN Statement [" + (id) +"]"); }
    private static void printEnd(int id) { print("END Statement [" + (id) +"]\n\n"); }
    private static void print(String s) { System.out.println(s); }
}
