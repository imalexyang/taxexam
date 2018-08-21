package com.extr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extr.domain.user.Org;
import com.extr.persistence.OrgMapper;

/**
 * @author Ocelot
 * @date 2014年6月8日 下午8:21:31
 */
@Service
public class OrgServiceImpl implements OrgService {

	@Autowired
	public OrgMapper orgMapper;	
	
	@Override
	public List<Org> getOrgListByIsallAndIswin(String isall,String iswin){
		List<Org> userList = orgMapper.getOrgListByIsallAndIswin(isall, iswin);
		return userList;
	}
	
	@Override
	public List<Org> getOrgListByIsallAndIswinAndWinlevel(String isall,String iswin,String winlevel){
		List<Org> userList = orgMapper.getOrgListByIsallAndIswinAndWinlevel(isall, iswin,winlevel);
		return userList;
	}
	
	@Override
	public void updateIswin(Org user){
		orgMapper.updateIswin(user);
	}

	@Override
	public int countWinOrg(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return orgMapper.countWinOrg(map);
	}
	
	
}
