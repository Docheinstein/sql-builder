package org.docheinstein.sqlbuilder.example;

import org.docheinstein.sqlbuilder.commons.SqlBuilder;
import org.docheinstein.sqlbuilder.commons.SqlBuilderLogger;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.example.dog.DogTable;
import org.docheinstein.sqlbuilder.example.person.PersonTable;
import org.docheinstein.sqlbuilder.example.person.PersonTuple;
import org.docheinstein.sqlbuilder.models.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.docheinstein.sqlbuilder.example.dog.DogTable.NICKNAME;
import static org.docheinstein.sqlbuilder.example.dog.DogTable.OWNER;
import static org.docheinstein.sqlbuilder.example.person.PersonTable.AGE;
import static org.docheinstein.sqlbuilder.example.person.PersonTable.NAME;
import static org.docheinstein.sqlbuilder.example.person.PersonTable.SURNAME;

public class SqlBuilderExecExample {

    // Connection stuff
    private static final String DB_NAME = "sql_builder_example";

    private static final String MYSQL_CONNECTION =
        "jdbc:mysql://localhost/" + DB_NAME + "?" +
            "useUnicode=true" +
            "&useJDBCCompliantTimezoneShift=true" +
            "&useTimezone=true" +
            "&useLegacyDatetimeCode=false" +
            "&serverTimezone=UTC";

    private static final String USERNAME = "user";
    private static final String PASSWORD = "pass";

    // DB Tables
    private final Table PERSON_TABLE = new PersonTable();
    private final Table DOG_TABLE = new DogTable();

    private Connection mConnection;

    public static void main(String[] args)
        throws  ClassNotFoundException, IllegalAccessException,
                SQLException, InstantiationException {

        SqlBuilder.setLanguage(SqlLanguage.MySQL);
        SqlBuilderLogger.enable(true);
        SqlBuilderLogger.addListener(message -> System.out.println(">> " + message));

        SqlBuilderExecExample sqlEx = new SqlBuilderExecExample();
        sqlEx.initConnection();
        sqlEx.exampleExec();
    }

    public void initConnection() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        mConnection = DriverManager.getConnection(MYSQL_CONNECTION, USERNAME, PASSWORD);
    }

    public void exampleExec() throws SQLException {
        int stmtCounter = 0;

        // ----- CREATE TABLE -------

        /*
            CREATE TABLE IF NOT EXISTS Person (
                Id INTEGER PRIMARY KEY AUTO_INCREMENT,
                Name VARCHAR(32) NOT NULL,
                Surname VARCHAR(32) NOT NULL,
                Age INTEGER,
                CHECK (Person.Age > 0);
            );
         */

        printBegin(++stmtCounter);
        PERSON_TABLE
            .create()
            .ifNotExists()
            .exec(mConnection);
        printEnd(stmtCounter);


        /*
            CREATE TABLE IF NOT EXISTS Dog (
                Owner INTEGER,
                Number INTEGER,
                Name VARCHAR(32) NOT NULL,
                PRIMARY KEY (Owner, Number),
                FOREIGN KEY (Owner) REFERENCES Person(Id)
                    ON DELETE CASCADE ON UPDATE CASCADE,
                CHECK (Dog.Number > 0)
            );
         */
        printBegin(++stmtCounter);
        DOG_TABLE
            .create()
            .ifNotExists()
            .exec(mConnection);
        printEnd(stmtCounter);

        // ----- INSERT -------
        // |___  FROM VALUES

        /*
            INSERT INTO Person VALUES (null, 'John', 'Smith', 15);
         */

        printBegin(++stmtCounter);
        int johnSmithId =
            PERSON_TABLE
                .insert()
                .values(null, "John", "Smith", 15)
                .exec(mConnection, true /* return auto increment key */);
        print("John Smith Id: " + johnSmithId);
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
        int johnWhiteId =
            PERSON_TABLE
                .insert()
                .valuesFromTuples(johnWhite) // assign the VALUES from the tuple
                .exec(mConnection, true /* return auto increment key */);
        print("John White Id: " + johnWhiteId);
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

        int affectedRows = PERSON_TABLE
            .update()
            .setFromTuple(olderJohnWhite)
            .where(
                NAME.eq("John").and(SURNAME.eq("White"))
            )
            .exec(mConnection);
        print("Affected rows: " + affectedRows);
        printEnd(stmtCounter);

        // ----- SELECT -------

        /*
            SELECT * FROM Person
         */

        // Option 1 (meeh)
        // Retrieves the result set with exec() and do whatever you want on it

        ResultSet rs;

        printBegin(++stmtCounter);
        rs =
            PERSON_TABLE
                .select("*")
                .exec(mConnection);
        while (rs.next()) {
            print("Retrieved a person");
            print("|_ Id: " + rs.getString(PersonTable.ID_COL_NAME));
            print("|_ Name: " + rs.getString(PersonTable.NAME_COL_NAME));
            print("|_ Name: " + rs.getString(PersonTable.NAME_COL_NAME));
            print("|_ Age: " + rs.getString(PersonTable.AGE_COL_NAME));
        }
        printEnd(stmtCounter);


        /*
            SELECT DISTINCT Person.Name
            FROM Person
            WHERE ((Person.Age <= 18))
         */

        // Option 2 :)
        // Directly fetch the tuples with fetch() and treat them as java object

        List<PersonTuple> persons;

        printBegin(++stmtCounter);
        persons =
            PERSON_TABLE
                .select(NAME)
                .distinct()
                .where(AGE.le(18))
                .fetch(mConnection, PersonTuple.class);
        print("Retrieved " + persons.size() + " persons...");

        printEnd(stmtCounter);

         /*
            SELECT Person.Id, Person.Name, Person.Surname, Person.Age
            FROM Person
            ORDER BY Person.Age ASC
         */

        // Option 3 :DD
        // Perform an action over each fetched tuple with forEach()

        printBegin(++stmtCounter);
        PERSON_TABLE
            .select(PERSON_TABLE.getColumns()) // Use getColumns() instead of * when fetch tuples
            .orderByAsc(AGE)
            .forEach(mConnection, PersonTuple.class, personTuple -> {
                // Action to perform over each tuple
                print("Retrieved person: " + personTuple);
            });
        printEnd(stmtCounter);

        /*
            SELECT Dog.Owner, Dog.NickName
            FROM Dog
            INNER JOIN Person ON Person.Id = Dog.Owner
         */

        printBegin(++stmtCounter);
        rs =
            DOG_TABLE
                .select(OWNER, NICKNAME)
                .innerJoin(PersonTable.ID, DogTable.OWNER)
                .exec(mConnection);
        while (rs.next()) {
            print("Retrieved a dog");
            print("|_ Owner: " + rs.getString(DogTable.OWNER_COL_NAME));
            print("|_ Number: " + rs.getString(DogTable.NUMBER_COL_NAME));
            print("|_ Nickname: " + rs.getString(DogTable.NICKNAME_COL_NAME));
        }
        printEnd(stmtCounter);

        /*
            SELECT COUNT(*)
            FROM Dog
            INNER JOIN Person ON Person.Id = Dog.Owner
            WHERE ((Person.Age >= ?))
            GROUP BY Person.Id
         */

        printBegin(++stmtCounter);
        rs =
            DOG_TABLE
                .select("COUNT(*)")
                .innerJoin(PersonTable.ID, DogTable.OWNER)
                .where(PersonTable.AGE.ge(18))
                .groupBy(PersonTable.ID)
                .exec(mConnection);
        while (rs.next()) {
            print("Retrieved a dog");
            print("|_ Owner: " + rs.getString(DogTable.OWNER_COL_NAME));
            print("|_ Number: " + rs.getString(DogTable.NUMBER_COL_NAME));
            print("|_ Nickname: " + rs.getString(DogTable.NICKNAME_COL_NAME));
        }
        printEnd(stmtCounter);

        /*
            SELECT Dog.NickName
            FROM Dog
            INNER JOIN Person ON Person.Id = Dog.Owner
            WHERE ((Person.Age > ?)))
            UNION (
                SELECT Dog.NickName
                FROM Dog
                INNER JOIN Person ON Person.Id = Dog.Owner
                WHERE ((Person.Surname = ?))
            )

         */

        printBegin(++stmtCounter);
        rs =
            DOG_TABLE
                .select(NICKNAME)
                .innerJoin(PersonTable.ID, DogTable.OWNER)
                .where(PersonTable.AGE.gt(18))
                .union(
                    DOG_TABLE
                        .select(NICKNAME)
                        .innerJoin(PersonTable.ID, DogTable.OWNER)
                        .where(PersonTable.SURNAME.eq("Smith"))
                ).exec(mConnection);
        while (rs.next()) {
            print("Retrieved a dog");
            print("|_ Owner: " + rs.getString(DogTable.OWNER_COL_NAME));
            print("|_ Number: " + rs.getString(DogTable.NUMBER_COL_NAME));
            print("|_ Nickname: " + rs.getString(DogTable.NICKNAME_COL_NAME));
        }
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
        rs =
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
                .exec(mConnection);
        while (rs.next()) {
            print("Retrieved a dog");
            print("|_ Owner: " + rs.getString(DogTable.OWNER_COL_NAME));
            print("|_ Number: " + rs.getString(DogTable.NUMBER_COL_NAME));
            print("|_ Nickname: " + rs.getString(DogTable.NICKNAME_COL_NAME));
        }
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
        persons =
            PERSON_TABLE
                .select(SURNAME)
                .where(
                    DOG_TABLE
                        .select("*")
                        .where(DogTable.OWNER.eq(PersonTable.ID))
                        .notExists()
                )
                .limit(10)
                .fetch(mConnection, PersonTuple.class);
        print("Retrieved " + persons.size() + " persons...");
        printEnd(stmtCounter);
    }

    private static void printBegin(int id) { print("BEGIN Statement [" + (id) +"]"); }
    private static void printEnd(int id) { print("END Statement [" + (id) +"]\n\n"); }
    private static void print(String s) { System.out.println(s); }
}
