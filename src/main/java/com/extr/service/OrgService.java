package com.extr.service;

import java.util.List;
import java.util.Map;

import com.extr.domain.user.Org;

/**
 * @author Ocelot
 * @date 2014年6月8日 下午5:52:55
 */
public interface OrgService {
	
	public List<Org> getOrgListByIsallAndIswin(String isall,String iswin);
	
	public List<Org> getOrgListByIsallAndIswinAndWinlevel(String isall,String iswin,String winlevel);	
	
	public void updateIswin(Org org);
	
	public int countWinOrg(Map<String,Object> map);
}
