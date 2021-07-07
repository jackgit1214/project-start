/**
 *
 */
package com.project.core.common;


/**
 * @author lilj
 * 建立树模型，与ZTREE树相对应，
 *
 */

public interface TreeData {

    String getId();

    String getPId();

    String getName();

//	public String getIconOpen() ;

//	public String getIconClose() ;

    Object getData();

    boolean getIsParent();

    String getUrl();

    boolean isChecked();


}
