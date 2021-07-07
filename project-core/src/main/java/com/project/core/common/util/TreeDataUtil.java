/**
 *
 */
package com.project.core.common.util;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.core.common.TreeData;

/**
 * @author lilj
 *
 */
public class TreeDataUtil {

    public static JsonNode ConvertToJsonData(TreeData treeData)
            throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonnode = objectMapper.readTree(objectMapper
                .writeValueAsString(treeData));
        return jsonnode;
    }

    public static JsonNode ConvertToJsonData(List<?> treeDatas)
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonnode = objectMapper.readTree(objectMapper
                .writeValueAsString(treeDatas));
        return jsonnode;
    }

    public static String ConvertToString(TreeData treeData) throws Exception {
        return ConvertToJsonData(treeData).toString();
    }

    public static String ConvertToString(List<?> treeDatas) throws Exception {
        return ConvertToJsonData(treeDatas).toString();
    }

}
