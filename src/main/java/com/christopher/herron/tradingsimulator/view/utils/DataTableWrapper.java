package com.christopher.herron.tradingsimulator.view.utils;

import java.util.ArrayList;
import java.util.List;

public class DataTableWrapper<T> {

    List<T> dataList = new ArrayList<>();

    public DataTableWrapper() {
    }

    public DataTableWrapper(List<T> dataList) {
        addData(dataList);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void addData(List<T> data) {
        dataList.addAll(data);
    }
}
