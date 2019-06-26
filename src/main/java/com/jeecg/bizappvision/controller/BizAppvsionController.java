package com.jeecg.bizappvision.controller;
import com.jeecg.bizappvision.entity.BizAppvsionEntity;
import com.jeecg.bizappvision.service.BizAppvsionServiceI;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.TreeChildCount;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import org.jeecgframework.core.util.ExceptionUtil;


import org.jeecgframework.web.cgform.entity.upload.CgUploadEntity;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import java.util.HashMap;
/**   
 * @Title: Controller  
 * @Description: App版本管理
 * @author onlineGenerator
 * @date 2019-06-19 23:02:12
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/bizAppvsionController")
public class BizAppvsionController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BizAppvsionController.class);

	@Autowired
	private BizAppvsionServiceI bizAppvsionService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;
	


	/**
	 * App版本管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/bizappvision/bizAppvsionList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(BizAppvsionEntity bizAppvsion,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(BizAppvsionEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, bizAppvsion, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.bizAppvsionService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除App版本管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(BizAppvsionEntity bizAppvsion, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		bizAppvsion = systemService.getEntity(BizAppvsionEntity.class, bizAppvsion.getId());
		message = "App版本管理删除成功";
		try{
			bizAppvsionService.delete(bizAppvsion);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "App版本管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 根据app应用id获取最新版本信息 api
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getappbyid")
	@ResponseBody
	public AjaxJson getappbyid(String id, HttpServletRequest request) {
		String message = null;

		AjaxJson j = new AjaxJson();
		List<Map<String, Object>> a  = systemService.findForJdbc("select * from biz_appvsion where app_id = '"+id+"' and app_status = 1  order by create_date desc",null);
		if(a.size()==0){
			message = "为查询到发布版本";
		}else{
			message = "App版本信息获取成功";
			j.setAttributes(a.get(0));
		}
		j.setMsg(message);
		return j;
	}





	/**
	 * 批量删除App版本管理
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "App版本管理删除成功";
		try{
			for(String id:ids.split(",")){
				BizAppvsionEntity bizAppvsion = systemService.getEntity(BizAppvsionEntity.class, 
				id
				);
				bizAppvsionService.delete(bizAppvsion);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "App版本管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加App版本管理
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(BizAppvsionEntity bizAppvsion, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "App版本管理添加成功";
		try{
			bizAppvsionService.save(bizAppvsion);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "App版本管理添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		j.setObj(bizAppvsion);
		return j;
	}
	
	/**
	 * 更新App版本管理
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(BizAppvsionEntity bizAppvsion, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "App版本管理更新成功";
		BizAppvsionEntity t = bizAppvsionService.get(BizAppvsionEntity.class, bizAppvsion.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(bizAppvsion, t);
			bizAppvsionService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "App版本管理更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * App版本管理新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(BizAppvsionEntity bizAppvsion, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(bizAppvsion.getId())) {
			bizAppvsion = bizAppvsionService.getEntity(BizAppvsionEntity.class, bizAppvsion.getId());
			req.setAttribute("bizAppvsionPage", bizAppvsion);
		}
		return new ModelAndView("com/jeecg/bizappvision/bizAppvsion-add");
	}
	/**
	 * App版本管理编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(BizAppvsionEntity bizAppvsion, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(bizAppvsion.getId())) {
			bizAppvsion = bizAppvsionService.getEntity(BizAppvsionEntity.class, bizAppvsion.getId());
			req.setAttribute("bizAppvsionPage", bizAppvsion);
		}
		return new ModelAndView("com/jeecg/bizappvision/bizAppvsion-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","bizAppvsionController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(BizAppvsionEntity bizAppvsion,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(BizAppvsionEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, bizAppvsion, request.getParameterMap());
		List<BizAppvsionEntity> bizAppvsions = this.bizAppvsionService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"App版本管理");
		modelMap.put(NormalExcelConstants.CLASS,BizAppvsionEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("App版本管理列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,bizAppvsions);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(BizAppvsionEntity bizAppvsion,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"App版本管理");
    	modelMap.put(NormalExcelConstants.CLASS,BizAppvsionEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("App版本管理列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
    	"导出信息"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<BizAppvsionEntity> listBizAppvsionEntitys = ExcelImportUtil.importExcel(file.getInputStream(),BizAppvsionEntity.class,params);
				for (BizAppvsionEntity bizAppvsion : listBizAppvsionEntitys) {
					bizAppvsionService.save(bizAppvsion);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(e.getMessage());
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}
	
	/**
	 * 获取文件附件信息
	 * 
	 * @param id bizAppvsion主键id
	 */
	@RequestMapping(params = "getFiles")
	@ResponseBody
	public AjaxJson getFiles(String id){
		List<CgUploadEntity> uploadBeans = cgFormFieldService.findByProperty(CgUploadEntity.class, "cgformId", id);
		List<Map<String,Object>> files = new ArrayList<Map<String,Object>>(0);
		for(CgUploadEntity b:uploadBeans){
			String title = b.getAttachmenttitle();//附件名
			String fileKey = b.getId();//附件主键
			String path = b.getRealpath();//附件路径
			String field = b.getCgformField();//表单中作为附件控件的字段
			Map<String, Object> file = new HashMap<String, Object>();
			file.put("title", title);
			file.put("fileKey", fileKey);
			file.put("path", path);
			file.put("field", field==null?"":field);
			files.add(file);
		}
		AjaxJson j = new AjaxJson();
		j.setObj(files);
		return j;
	}
	
}
