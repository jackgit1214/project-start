package com.project.core.common;

/**
 * layui dtree 数据格式
 * [ {"id":"001","title": "湖南省","checkArr": "0","parentId": "0"},
 * {"id":"002","title": "湖北省","checkArr": "0","parentId": "0"}]
 *
 * @author lilj
 * @date 2021/4/5 10:44
 * @return
 */
public interface DTreeData {

    String getId();

    String getParentId();

    String getTitle();

    Object getCheckArr();

}
