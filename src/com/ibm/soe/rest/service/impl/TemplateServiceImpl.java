package com.ibm.soe.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.soe.rest.dao.TemplateDao;
import com.ibm.soe.rest.service.TemplateService;
@Service
public class TemplateServiceImpl implements  TemplateService{

	@Autowired
	private TemplateDao templateDao;
	
	@Override
	public List<Map<String, String>> getIndustry(Map<String, String> param)
			throws Exception {
		// TODO Auto-generated method stub	
	//	List<Map<String, String>> industryList=	templateDao.getIndustry();
		String industryById=param.get("industryById");
		List<Map<String, String>> industryList=	templateDao.getIndustry(industryById);
		List<Map<String, String>> industrySortList=new ArrayList<Map<String, String>>();
		Map<String, String> firstOne=null;
		for(Map<String,String> item :industryList)
		{
			String idIndustry=item.get("industryId");
			if("20".equals(idIndustry))
			{
				firstOne=item;
			}
		}
		industrySortList.add(firstOne);
		industryList.remove(firstOne);
		industrySortList.addAll(industryList);
		
		return industryList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getTemplate(Map<String, String> param)
			throws Exception {
		// TODO Auto-generated method stub
		String industryById=param.get("industryId");
		if(industryById==null ||"".equals(industryById))
		{
			industryById=null;
		}
		Map<String,Object> templateMap=new HashMap<String,Object>();
		List<Map<String, String>> industryList=	templateDao.getIndustry(industryById);
		List<Map<String, String>> industrySortList=new ArrayList<Map<String, String>>();
		Map<String, String> firstOne=null;
		for(Map<String,String> item :industryList)
		{
			String idIndustry=item.get("industryId");
			templateMap.put(idIndustry, null);
			if("20".equals(idIndustry))
			{
				firstOne=item;
			}
		}
		industrySortList.add(firstOne);
		industryList.remove(firstOne);
		industrySortList.addAll(industryList);
		
		
		
		Map<String,Object> map=new HashMap<String,Object>();
		
		List<Map<String, String>> templateList=	templateDao.getTemplate(industryById);
		for(Map<String,String> item : templateList)
		{
			String industryId= item.get("industryId");
			Map<String,Object> templte= (Map<String, Object>) templateMap.get(industryId);
			
			List<Map<String, Object>> result= (List<Map<String, Object>>) map.get(industryId);
			 
			if(result==null)
			{
				result=new ArrayList<Map<String, Object>>();
				map.put(industryId, result);
			}
			
			String level1Id= item.get("level1Id");
			String level1Seq = item.get("level1Seq");
			if (level1Id != null) {
				if (templte == null) {
					templte = new HashMap<String, Object>();
					templateMap.put(industryId, templte);
				}

				Map<String, Object> level1Item = (Map<String, Object>) templte
						.get(level1Id);
				HashMap<String, Object> level1ListItem=null;
				
				if (level1Item == null) {
					
					String level1Title = item.get("level1Title");
					String level1Content = item.get("level1Content")==null?"":item.get("level1Content");
					level1Item = new HashMap<String, Object>();
					level1Item.put("id", level1Id);
					templte.put(level1Id, level1Item);
					level1ListItem= new HashMap<String, Object>();
					level1ListItem.put("id", level1Id);
					level1ListItem.put("seq", level1Seq);
					level1ListItem.put("title", level1Title);
					level1ListItem.put("content", level1Content);
					result.add(level1ListItem);
				}

				String level2Id = item.get("level2Id");
				String level2Seq = item.get("level2Seq");
				List<Map<String, Object>> level2ChildList = (List<Map<String, Object>>) result.get(result.size()-1)
						.get("child");
				if (level2Id != null) {
					Map<String, Object> level2ChildMap = (Map<String, Object>) level1Item
							.get("child");
				
					if(level2ChildList==null)
					{
						level2ChildList=new ArrayList<Map<String, Object>>();
						result.get(result.size()-1).put("child", level2ChildList);
					}
					if (level2ChildMap == null) {
						level2ChildMap = new HashMap<String, Object>();
						level1Item.put("child", level2ChildMap);
						level1ListItem.put("child", level2ChildList);
					}

					Map<String, Object> level2Child = (Map<String, Object>) level2ChildMap
							.get(level2Id);
					HashMap<String, Object> level2ListChild=null;
					if (level2Child == null && level2Id != null) {
						String level2Title = item.get("level2Title");
						String level2Content = item.get("level2Content")==null?"":item.get("level2Content");
						level2Child = new HashMap<String, Object>();
						level2Child.put("id", level2Id);
						level2ChildMap.put(level2Id, level2Child);
						level2ListChild = new HashMap<String, Object>();
						level2ListChild.put("id", level2Id);
						level2ListChild.put("seq", level2Seq);
						level2ListChild.put("title", level2Title);
						level2ListChild.put("content", level2Content);
						level2ChildList.add(level2ListChild);
					}

					String level3Seq = item.get("level3Seq");
					String level3Id = item.get("level3Id");
					if (level3Id != null) {
						Map<String, Object> level3ChildMap = (Map<String, Object>) level2Child
								.get("child");
						List<Map<String, Object>> temp3 = (List<Map<String, Object>>)  result.get(result.size()-1)
								.get("child");
						List<Map<String, Object>> level3ChildList=(List<Map<String, Object>>) temp3.get(temp3.size()-1).get("child");
						
						if(level3ChildList==null)
						{
							level3ChildList=new ArrayList<Map<String, Object>>();
							List<Map<String, Object>> temp4=(List<Map<String, Object>>) result.get(result.size()-1).get("child");
							temp4.get(temp4.size()-1).put("child", level3ChildList);
						}
						if (level3ChildMap == null) {
							level3ChildMap = new HashMap<String, Object>();
							level2Child.put("child", level3ChildMap);
						}

						Map<String, Object> level3Child = (Map<String, Object>) level3ChildMap
								.get(level3Id);
						Map<String, Object> level3ListChild=null;
						if (level3Child == null && level3Id != null) {
							
							String level3Title = item.get("level3Title");
							String level3Content = item.get("level3Content")==null?"":item.get("level3Content");
							level3Child = new HashMap<String, Object>();
							level3Child.put("id", level3Id);
							level3ChildMap.put(level3Id, level3Child);
							level3ListChild = new HashMap<String, Object>();
							level3ListChild.put("id", level3Id);
							level3ListChild.put("seq", level3Seq);
							level3ListChild.put("title", level3Title);
							level3ListChild.put("content", level3Content);
							level3ChildList.add(level3ListChild);
						}

					}
				}
			}
		}
		
		templateMap=null;
		map.put("allIndustry", industrySortList);
		return map;
	}

}
