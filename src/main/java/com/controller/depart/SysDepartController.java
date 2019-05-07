package com.controller.depart;

import com.entity.SysDepart;
import com.service.SysDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/pages/sysDepart")
public class SysDepartController {

    @Autowired
    private SysDepartService sysDepartService;

    /**
     * 系统部门模糊检索
     * @param fuzzyStr
     * @return
     */
    @ResponseBody
    @RequestMapping("/fuzzySearchDepart")
    public List<Map<String, String>> fuzzySearchDepart(String fuzzyStr){
        List<Map<String, String>> resultList = new ArrayList<>();
        List<SysDepart> sysDepartList = sysDepartService.fuzzySearchDepart(fuzzyStr);
        if(sysDepartList != null && sysDepartList.size() > 0){
            for(SysDepart sysDepart : sysDepartList){
                Map<String, String> map = new HashMap<>();
                map.put("id", sysDepart.getDepartCode());
                map.put("text", sysDepart.getDepartName());
                resultList.add(map);
            }
        }
        return resultList;
    }

}