package com.extr.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.extr.domain.user.Org;

public interface OrgMapper {

	public List<Org> getOrgListByIsallAndIswin(@Param("isall") String isall,@Param("iswin") String iswin);	
	
	public List<Org> getOrgListByIsallAndIswinAndWinlevel(@Param("isall") String isall,@Param("iswin") String iswin,@Param("winlevel") String winlevel);	

	public void updateIswin(Org org);
	
	public int countWinOrg(Map<String, Object> map);
}
