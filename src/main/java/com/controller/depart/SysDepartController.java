package com.controller.depart;

import com.constant.Constant;
import com.controller.BaseController;
import com.dao.SysDepartDao;
import com.entity.SysDepart;
import com.entity.SysUser;
import com.service.SysDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/pages/sysDepart")
public class SysDepartController extends BaseController {

    @Autowired
    private SysDepartService sysDepartService;
    @Autowired
    private SysDepartDao sysDepartDao;

    /**
     * 系统部门模糊检索
     * @param fuzzyStr
     * @return
     */
    @ResponseBody
    @RequestMapping("/fuzzySearchDepart")
    public List<Map<String, String>> fuzzySearchDepart(String fuzzyStr, HttpServletRequest request){
        List<Map<String, String>> resultList = new ArrayList<>();
        SysUser sysUser = findLoginUser(request);
        if(sysUser != null && !Constant.TOP_DEPARTCODE.equals(sysUser.getUnitCode())){
            fuzzyStr = sysUser.getUnitCode();
        }
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

    /**
     * 获取系统部门列表
     */
    @ResponseBody
    @RequestMapping(value = "/getDepart")
    public Map<String, String> getDepart(){
        Map<String, String> map = new HashMap<>();
        Iterable<SysDepart> sysDepartList = sysDepartDao.findAll();
       if(sysDepartList != null){
           Iterator<SysDepart> sysDeparts = sysDepartList.iterator();
           while(sysDeparts.hasNext()){
               SysDepart sysDepart = sysDeparts.next();
                map.put(sysDepart.getDepartCode(), sysDepart.getDepartName());
            }
        }
        return map;
    }

}