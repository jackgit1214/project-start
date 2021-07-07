package com.project.core.mybatis.service;

import java.util.List;

import com.project.core.common.TreeData;

public interface ITreeService {

    List<TreeData> getTreeData();

    List<TreeData> getTreeData(String superId);

}
