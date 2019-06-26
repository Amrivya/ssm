package com.jeecg.bizappmanager.service;
import com.jeecg.bizappmanager.entity.BizAppmanageEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface BizAppmanageServiceI extends CommonService{
	
 	public void delete(BizAppmanageEntity entity) throws Exception;
 	
 	public Serializable save(BizAppmanageEntity entity) throws Exception;
 	
 	public void saveOrUpdate(BizAppmanageEntity entity) throws Exception;
 	
}
