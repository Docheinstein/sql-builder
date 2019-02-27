# SQL Builder

SQL Builder is small library for build and execute SQL statements using the 
Java's OO paradigm, avoiding frustrating syntax errors and making queries
easier to build and fancier to see.

## Installation

I recommend to use this utilities container through [JitPack](https://jitpack.io/).

In particular, configure the `build.gradle` file as follows.

```
repositories {
    mavenCentral()

    jcenter()

    /* JitPack */
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    compile 'com.github.docheinstein:sql-builder:master-SNAPSHOT'
}

```

### What does it looks like?

Before proceed to a step by step guide on how to use the library: this is how
your code will be.

```
// CREATE TABLE

PERSON_TABLE
    .create()
    .ifNotExists()
    .exec(mConnection);

// INSERT

PERSON_TABLE
    .insert()
    .values(1, "John", "Smith", 15)
    .exec(mConnection);
    
// SELECT

PERSON_TABLE
    .select(NAME)
    .distinct()
    .where(AGE.le(18))
    .fetch(mConnection, PersonTuple.class);
```

### Usage

As I promised, here a mini guide on how to use the library from A to Z.

1) Create a class that extends `Table`; this is needed for perform any kind of query

```java
public class PersonTable extends Table {

    private static final String TABLE_NAME =        "Person";

    // Columns name

    public static final String ID_COL_NAME =        "Id";
    public static final String NAME_COL_NAME =      "Name";
    public static final String SURNAME_COL_NAME =   "Surname";
    public static final String AGE_COL_NAME =       "Age";

    // Columns

    public static Column<Integer> ID =
        new Column<>(TABLE_NAME, ID_COL_NAME, new Int())
        .autoIncrement() // AUTO_INCREMENT
        .primaryKey();   // PRIMARY KEY

    public static Column<String> NAME =
        new Column<>(TABLE_NAME, NAME_COL_NAME, new Varchar(32))
        .notNull();

    public static Column<String> SURNAME =
        new Column<>(TABLE_NAME, SURNAME_COL_NAME, new Varchar(32))
        .notNull();

    public static Column<Integer> AGE =
        new Column<>(TABLE_NAME, AGE_COL_NAME, new Int());


    public PersonTable() {
        super(TABLE_NAME);

        // Add the columns to the table
        col(ID);
        col(NAME);
        col(SURNAME);
        col(AGE);

        // Eventually add the CHECK constraint
        check(AGE.gt(0)); // Age > 0
    }
}
```

The constructor of the class that extends `Table` we are creating have to build
itself with all the stuff needed for the table creation (columns, primary key, 
foreign keys, or check expression).

---

2) Create a class that implements the `Tuple` interface.

This is step not mandatory but is really recommended if there is need to retrieve
data from the table we are creating.

```java
public class PersonTuple implements Tuple {

    @ColumnField(PersonTable.ID_COL_NAME)
    public Integer id;

    @ColumnField(PersonTable.NAME_COL_NAME)
    public String name;

    @ColumnField(PersonTable.SURNAME_COL_NAME)
    public String surname;

    @ColumnField(PersonTable.AGE_COL_NAME)
    public Integer age;

    @Override
    public String toString() {
        return id + " | " + name + " | " + surname + " | " + age;
    }
}
```

The concept of the `Tuple` is that when a SELECT query is executed the retrieved
rows are automatically put into the class that implements `Tuple` we've just
created.     
For being able to do so it is needed to annotate each java variable with
the annotation `ColumnField` and use as parameter exactly the column name the field
has to be mapped to.

---

3) Before create any kind of statement be sure to set the SqlBuilder language
to the language you want to use (actually only MySQL and PostgreSQL are supported).

```
SqlBuilder.setLanguage(SqlLanguage.MySQL);
```

---

4) Ok we are ready: now you can use the `Table` for 
perform any kind of statement (SELECT, INSERT, UPDATE, ...)

The source code contains a bunch of valid example for the most common queries, 
but since I'm pretty sure you are quite lazy, a few of those will be reported here.

**CREATE TABLE**

```
/*
    CREATE TABLE IF NOT EXISTS Person (
        Id INTEGER PRIMARY KEY AUTO_INCREMENT,
        Name VARCHAR(32) NOT NULL,
        Surname VARCHAR(32) NOT NULL,
        Age INTEGER,
        CHECK (Person.Age > 0);
    );
 */

PERSON_TABLE
    .create()
    .ifNotExists()
    .exec(mConnection);
```

The definition of the table is obviously taken from the class we've just created
(the one that extends `Table`).

**INSERT INTO**

There are two different ways for insert values into a table, the most classic
way is specify each value to insert "manually", as follows:

```
/*
    INSERT INTO Person VALUES (null, 'John', 'Smith', 15);
 */

PERSON_TABLE
    .insert()
    .values(null, "John", "Smith", 15) // Id, Name, Surname, Age
                                       // Id is null because is AUTO_INCREMENT
    .exec(mConnection);
```

The smartier way of insert rows into a table is let the library takes the values
automatically from a `Tuple` object.

```
/*
    INSERT INTO Person VALUES (null, 'John', 'White', 19);
 */

PersonTuple johnWhite = new PersonTuple();
johnWhite.id = null;
johnWhite.name = "John";
johnWhite.surname = "White";
johnWhite.age = 19;
        
int johnWhiteId =
    PERSON_TABLE
        .insert()
        .valuesFromTuples(johnWhite) // Takes the VALUES from the tuple
        .exec(mConnection, true /* return auto increment key */);
```

**SELECT**

Okok, CREATE and INSERT were easy, now begins the tough stuff: SELECT statements.

Let's begin with a dummy-proof SELECT.

```
/*
    SELECT * FROM Person
 */

        
ResultSet rs = 
    PERSON_TABLE
        .select("*")
        .exec(mConnection);
```

Ok the example is easy, but this shows you how you can retrieve you dear ResultSet even with this library.    
With a ResultSet you can do whatever you want, but this actually is poor solution since you should retrieve all the field of a row manually.   
Why not automatically acquire those inside our `Tuple` object?

```
/*
    SELECT DISTINCT Person.Name
    FROM Person
    WHERE Person.Age <= 18
 */

        
List<PersonTuple> persons =
    PERSON_TABLE
        .select(NAME)
        .distinct()
        .where(AGE.le(18))
        .fetch(mConnection, PersonTuple.class);
```

By using `fetch(<Connection>, <Tuple>)` instead of `exec(<Connection>)` you are able
to retrieve each tuple that matches the SELECT statement and store these inside
a list of `Tuple` of the specified class; isn't this cool? That's not all...

```
/*
    SELECT Person.Id, Person.Name, Person.Surname, Person.Age
    FROM Person
    ORDER BY Person.Age ASC
 */

        
PERSON_TABLE
    .select(PERSON_TABLE.getColumns()) // Use getColumns() instead of * when fetch tuples
    .orderByAsc(AGE)
    .forEach(mConnection, PersonTuple.class, personTuple -> {
        // Action to perform over each tuple
        System.out.println("Retrieved person: " + personTuple);
    });
```

As shown you could also call `forEach()` instead of `fetch()`; in this way you
could perform an action over each retrieved tuple that matches the SELECT.

Remember that if you want retrieve a complete `Tuple` object you must add all 
the columns of the tuple to the SELECT statement; this must be done with `TABLE.getColumns()`.

Let's continue with some query less dummy-proof.

*Usage of INNER JOIN, GROUP BY, and COUNT(*)*

```
/*
    SELECT COUNT(*)
    FROM Dog
    INNER JOIN Person ON Person.Id = Dog.Owner
    WHERE Person.Age >= 18
    GROUP BY Person.Id
 */

ResultSet rs =
    DOG_TABLE
        .select("COUNT(*) as count")
        .innerJoin(PersonTable.ID, DogTable.OWNER)
        .where(PersonTable.AGE.ge(18))
        .groupBy(PersonTable.ID)
        .exec(mConnection);
int rows = rs.getInt("count");
```

*Usage of ALL (ANY | SOME) and LIMIT*

```
/*
    SELECT Dog.NickName
    FROM Person
    INNER JOIN Dog ON Dog.Owner = Person.Id
    WHERE (Person.Age >= ALL (
        SELECT Person.Age FROM Person)
    )
    LIMIT 0, 10
 */

PERSON_TABLE
    .select(DogTable.NICKNAME)
    .innerJoin(DogTable.OWNER, PersonTable.ID)
    .where(
        AGE.ge(             // Greater equals 
            PERSON_TABLE
                .select(AGE)
                .all()
        )
    )
    .limit(10)
    .exec(mConnection);
```

*Usage of UNION (INTERSECT | EXCEPT)*

```
/*
    SELECT Dog.NickName
    FROM Dog
    INNER JOIN Person ON Person.Id = Dog.Owner
    WHERE (Person.Age > 18)
    UNION (
        SELECT Dog.NickName
        FROM Dog 
        INNER JOIN Person ON Person.Id = Dog.Owner
        WHERE Person.Surname = 'Smith'
    )
 */

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
```

### Debug

For check what's going on behind the hood, you can enable the `SqlBuilderLogger`
and listen for new messages.

```
SqlBuilderLogger.enable(true);
SqlBuilderLogger.addListener(message -> {
        System.out.println(">> " + message)
    }
);
```

### How much complete is this library?

Eheh, unfortunately the SQL language is really wide and my time is little so 
this library grows when I need to use a new feature in my projects.

For now only MySQL syntax is really supported and tested (as soon as I can 
PostgreSQL will be supported to).

**Supported statements**   
* ALTER
* CREATE
* DELETE
* DROP
* INSERT
* REPLACE
* SELECT
* UPDATE
* CREATE TRIGGER
* DROP TRIGGER

**Supported types**   
* BOOL ( i.e. TINYINT(1) )
* CHAR
* DATE
* DATETIME
* ENUM
* INT
* TIME
* TIMESTAMP
* TINYINT
* VARCHAR

### TODO

* Test the whole library with PostgreSQL DBMS
* Implement CREATE/DROP PROCEDURE statements (which is 
needed for actually use PostgreSQL triggers)
* Add more possible options/conditions/clauses for some complex statement (e.g. CREATE TABLE)
* Add to the documentation the fact that the PreparedStatement can be cached 
from the library for avoid to recreate the SQL string each time (with
 `execCache`, `fetchCache` and `forEachCache`)
