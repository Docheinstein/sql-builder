package com.docheinstein.sqlbuilder.models;

import com.docheinstein.sqlbuilder.SqlBindable;

import java.util.ArrayList;
import java.util.List;

public class BindableObject implements SqlBindable {
    private final List<Object> mObject = new ArrayList<>();

    public BindableObject(Object o) {
        mObject.add(o);
    }

    @Override
    public List<Object> getBindableObjects() {
        return mObject;
    }

    @Override
    public String toSql() {
        return "?";
    }
}
