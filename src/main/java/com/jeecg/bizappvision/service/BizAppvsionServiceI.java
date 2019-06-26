package com.jeecg.bizappvision.service;
import com.jeecg.bizappvision.entity.BizAppvsionEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface BizAppvsionServiceI extends CommonService{
	
 	public void delete(BizAppvsionEntity entity) throws Exception;
 	
 	public Serializable save(BizAppvsionEntity entity) throws Exception;
 	
 	public void saveOrUpdate(BizAppvsionEntity entity) throws Exception;
 	
}
