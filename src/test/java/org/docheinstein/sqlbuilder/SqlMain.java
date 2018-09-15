package org.docheinstein.sqlbuilder;

import org.docheinstein.sqlbuilder.commons.SqlBuilder;
import org.docheinstein.sqlbuilder.commons.SqlLanguage;
import org.docheinstein.sqlbuilder.models.Column;
import org.docheinstein.sqlbuilder.clauses.ForeignKey;
import org.docheinstein.sqlbuilder.models.Table;
import org.docheinstein.sqlbuilder.statements.mysql.CreateTriggerMySQL;
import org.docheinstein.sqlbuilder.types.Char;
import org.docheinstein.sqlbuilder.types.Date;
import org.docheinstein.sqlbuilder.types.Int;

import java.sql.SQLException;

/*

CREATE TABLE S
(   Matr            CHAR(9),
    SNome           CHAR(40),
    Citta           CHAR(20),
    ACorso          NUMERIC(2),
    PRIMARY KEY (Matr)
);

CREATE TABLE D
(   CD              CHAR(5),
    DNome           CHAR(40),
    Citta           CHAR(20),
    PRIMARY KEY (CD)
);

CREATE TABLE C
(   CC              CHAR(5),
    CNome           CHAR(40),
    CD              CHAR(5),
    PRIMARY KEY (CC),
    FOREIGN KEY (CD)
        REFERENCES D (CD)
        ON DELETE CASCADE
);

CREATE TABLE E
(   Matr            CHAR(9),
    CC              CHAR(5),
    Data            DATE,
    Voto            NUMERIC(3),
    PRIMARY KEY (Matr,CC),
    FOREIGN KEY (Matr)
        REFERENCES S (Matr)
        ON DELETE CASCADE,
    FOREIGN KEY (CC)
        REFERENCES C (CC)
        ON DELETE CASCADE,
    CHECK (((VOTO>=18) AND (VOTO<=30)) OR (VOTO=33))
);


INSERT INTO S VALUES ('M1','Lucia Quaranta','SA',1);
INSERT INTO S VALUES ('M2','Giacomo Tedesco','PA',2);
INSERT INTO S VALUES ('M3','Carla Longo','MO',1);
INSERT INTO S VALUES ('M4','Ugo Rossi','MO',1);
INSERT INTO S VALUES ('M5','Valeria Neri','MO',2);
INSERT INTO S VALUES ('M6','Giuseppe Verdi','BO',1);
INSERT INTO S VALUES ('M7','Maria Rossi',NULL,1);

INSERT INTO D VALUES ('D1','Paolo Rossi','MO');
INSERT INTO D VALUES ('D2','Maria Pastore','BO');
INSERT INTO D VALUES ('D3','Paola Caboni','FI');

INSERT INTO C VALUES ('C1','Fisica 1','D1');
INSERT INTO C VALUES ('C2','Analisi Matematica 1','D2');
INSERT INTO C VALUES ('C3','Fisica 2','D1');
INSERT INTO C VALUES ('C4','Analisi Matematica 2','D3');

INSERT INTO E VALUES ('M1','C1','2014-06-29',24);
INSERT INTO E VALUES ('M1','C2','2015-08-09',33);
INSERT INTO E VALUES ('M1','C3','2015-03-12',30);
INSERT INTO E VALUES ('M2','C1','2014-06-29',28);
INSERT INTO E VALUES ('M2','C2','2015-07-07',24);
INSERT INTO E VALUES ('M3','C2','2015-07-07',27);
INSERT INTO E VALUES ('M3','C3','2015-11-11',25);
INSERT INTO E VALUES ('M4','C3','2015-11-11',33);
INSERT INTO E VALUES ('M6','C2','2015-01-02',28);
INSERT INTO E VALUES ('M7','C1','2014-06-29',24);
INSERT INTO E VALUES ('M7','C2','2015-04-11',27);
INSERT INTO E VALUES ('M7','C3','2015-06-23',27);


*/

public class SqlMain {

    private static final Table _S = new S();
    private static final Table _D = new D();
    private static final Table _C = new C();
    private static final Table _E = new E();

    static class S extends Table {
        private static final String TABLE_NAME = "S";
        public static Column<String> MATR = new Column<>(TABLE_NAME, "Matr", new Char(9));
        public static Column<String> SNOME = new Column<>(TABLE_NAME, "SNome", new Char(40));
        public static Column<String> CITTA = new Column<>(TABLE_NAME, "Citta", new Char(20));
        public static Column<Integer> ACORSO = new Column<>(TABLE_NAME, "ACorso", new Int(2));

        public S() {
            super(TABLE_NAME);
            col(MATR);
            col(SNOME);
            col(CITTA);
            col(ACORSO);
            primaryKey(MATR);
        }
    }

    static class D extends Table {
        private static final String TABLE_NAME = "D";
        public static Column<String> CD = new Column<>(TABLE_NAME, "CD", new Char(5));
        public static Column<String> DNOME = new Column<>(TABLE_NAME, "DNome", new Char(40));
        public static Column<String> CITTA = new Column<>(TABLE_NAME, "Citta", new Char(20));

        public D() {
            super(TABLE_NAME);
            col(CD);
            col(DNOME);
            col(CITTA);
            primaryKey(CD);
        }
    }

    static class C extends Table {
        private static final String TABLE_NAME = "C";
        public static Column<String> CC = new Column<>(TABLE_NAME, "CC", new Char(5));
        public static Column<String> CNOME = new Column<>(TABLE_NAME, "CNome", new Char(40));
        public static Column<String> CD = new Column<>(TABLE_NAME, "CD", new Char(5));

        public C() {
            super(TABLE_NAME);
            col(CC);
            col(CNOME);
            col(CD);
            primaryKey(CC);
            foreignKey(CD, D.CD, ForeignKey.ReferenceOption.Cascade, null);
        }
    }

    static class E extends Table {
        private static final String TABLE_NAME = "E";
        public static Column<String> MATR = new Column<>(TABLE_NAME, "Matr", new Char(9));
        public static Column<String> CC = new Column<>(TABLE_NAME, "CC", new Char(5));
        public static Column<String> DATA = new Column<>(TABLE_NAME, "Data", new Date());
        public static Column<Integer> VOTO = new Column<>(TABLE_NAME, "Voto", new Int(3));

        public E() {
            super(TABLE_NAME);
            col(MATR);
            col(CC);
            col(DATA);
            col(VOTO);
            primaryKey(MATR, CC);
            foreignKey(MATR, S.MATR, ForeignKey.ReferenceOption.Cascade, null);
            foreignKey(CC, C.CC, ForeignKey.ReferenceOption.Cascade, null);
            check(VOTO.ge(18).and(VOTO.le(30)).or(VOTO.eq(33)));
        }
    }

    public static void main(String args[]) {
       try {
           //mainInternal();
           mainInternal2();
       } catch (Exception e ){
           e.printStackTrace();
       }
    }

    private static void mainInternal2() {
        SqlBuilder.setLanguage(SqlLanguage.MySQL);
        System.out.println(
            new Table("PinSchedule").createTrigger(
                "PinScheduleDateCheck",
                CreateTriggerMySQL.ActionTime.Before,
                CreateTriggerMySQL.ActionType.Insert,
                "IF NEW.Datetime < CURDATE() THEN " +
                    " SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'your error text'; " +
                "END IF;"
            ).toSql()
        );
    }

    private static void mainInternal() throws SQLException {
//        String s = new S().create().toSql();
//        String d = new D().create().toSql();
//        String c = new C().create().toSql();
//        String e = new E().create().toSql();
//
//        System.out.println(s);
//        System.out.println(d);
//        System.out.println(c);
//        System.out.println(e);
//
        System.out.println("\n\n---------------\n\n");

        String e2a_1st =
            _S.select(S.CITTA)
                .except(
            _S.select(D.CITTA)
                )
                .toSql();


        String e2a_2nd = _S.select(S.SNOME)
        .where(
            S.MATR.in(
                _E.select(E.MATR)
                .where(E.CC.eq("C1"))
            )
        ).toSql();

        System.out.println("\n\n------------------\n\n");

        String e2c = _S.select(_S.getColumns())
            .where(
                S.ACORSO.le(
                    _S.select(S.ACORSO)
                        .all()
                )
            ).toSql();

        String e2d_1st = _S.select(S.SNOME)
            .where(S.MATR.notIn(
                _E.select(E.MATR)
                .where(E.CC.eq("C1"))
            )).toSql();

        String e2d_3rd = _S.select(S.SNOME)
            .where(
                _E.select(S.MATR)
                .where(E.CC.eq("C1").and(E.MATR.eq(S.MATR))).exists()
            ).toSql();

        String joinTest = _S.select(S.MATR, S.SNOME)
            .innerJoin(E.MATR, S.MATR)
            .where(E.MATR.neq("M1"))
            .orderByAsc(E.MATR)
            .toSql();

        System.out.println(e2a_1st);
        System.out.println(e2a_2nd);
        System.out.println(e2c);
        System.out.println(e2d_1st);
        System.out.println(e2d_3rd);
        System.out.println(joinTest);

        System.out.println(
            _E.select(E.MATR)
            .where(E.MATR.eq("M1"))
            .groupBy(E.MATR)
            .having(E.VOTO.gt(24))

                .toSql()
        );

        System.out.println(
            _E.select(E.MATR)
                .where(E.MATR.and(E.MATR).and(E.MATR))

                .toSql()
        );

        System.out.println(
            _E.select(E.MATR)
                .where(E.MATR.isNotNull().and(E.MATR.ge("23")))

                .toSql()
        );

//        Expression e = E.MATR.ge(E.MATR);
//        String vs = "23";
//        Column<String> c = E.MATR;
//        Statement s = _E.select("*");

//        System.out.println("-- DEBUGGING CONSTRUCTORS --");
//        new Expression(e, e);
//        new Expression(e, c);
//        new Expression(e, vs);
//
//        new Expression(c, c);
//        new Expression(c, e);
//        new Expression(c, vs);
//        new Expression(c, s);
//
//        new Expression(vs, vs);
//        new Expression(s);
//        new Expression(s, s);

//        System.out.println(
//            _E.select(E.MATR)
//            .where(E.VOTO.between(23, 30))
//                .toSql()
//        );
//        System.out.println("-- DEBUGGING CONSTRUCTORS --");
//        System.out.println(E.VOTO.between(23, 30).toSql());

        System.out.println(
            _E.alter().add(S.ACORSO).add(S.CITTA).drop(E.MATR).toSql()
        );
    }
}
