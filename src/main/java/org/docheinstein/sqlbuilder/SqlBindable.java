package org.docheinstein.sqlbuilder;

import java.util.List;

public interface SqlBindable extends Sqlable {
    List<Object> getBindableObjects();
}
