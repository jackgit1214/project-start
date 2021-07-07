package com.project.core.web.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.core.common.SysConstant;
import com.project.core.common.TreeData;
import com.project.core.mybatis.service.ITreeService;
import com.project.core.web.config.ProjectConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

public abstract class BaseController {

	protected final Log log = LogFactory.getLog(this.getClass());

	protected ITreeService treeService;

	@Autowired
	protected ProjectConfig projectConfig;

	protected HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return sra.getRequest();
	}

	/**
	 * 根据错误代码值，取得信息
	 */
	protected String getMessageSourceInfo(String codekey) {
		WebApplicationContext ac = RequestContextUtils.findWebApplicationContext(this.getRequest());
		String messageinfo = ac.getMessage(codekey, null, this.getRequest()
				.getLocale());
		return messageinfo;
	}


	protected boolean checkAttachMentSize(HttpServletRequest request) {

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = multiRequest.getFiles("files");
			Iterator<MultipartFile> iter = files.iterator();
			long totalfilesize = 0;
			while (iter.hasNext()) {
				MultipartFile file = iter.next();
				if (file.getSize() > SysConstant.SINGLEFILESIZE) {
					return false;
				}
				else
					totalfilesize = totalfilesize + file.getSize();
			}
			return totalfilesize <= SysConstant.TOTALFILESIZE;
		}
		return true;
	}

	@ResponseBody
	@RequestMapping("/getTreeData")
	public List<TreeData> getTreeData(String superid) {
		// this.getTreeService();
		if (this instanceof ITreeController) {
			ITreeController treeC = (ITreeController) this;
			treeC.setTreeService();
		}
		else {
			System.out.println("没有实现树的接口");
		}
		return this.treeService.getTreeData(superid);
	}
}
