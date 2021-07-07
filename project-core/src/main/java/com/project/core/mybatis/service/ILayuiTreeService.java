package com.project.core.mybatis.service;

import java.util.List;

import com.project.core.common.DTreeData;

public interface ILayuiTreeService {

    List<DTreeData> getTreeData();

    List<DTreeData> getTreeData(String superId);

}
