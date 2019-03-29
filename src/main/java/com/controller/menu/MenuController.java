package com.controller.menu;

import com.constant.Constant;
import com.dao.SysModuleDao;
import com.entity.SysModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/3/18
 */
@Controller
@RequestMapping(value = "/pages/main/menu")
public class MenuController {

    @Autowired
    private SysModuleDao sysModuleDao;

    @RequestMapping(value = "/getMenuList",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getMenuList(HttpServletRequest request){

        Map<String,Object> resultMap = new HashMap<>();
        try{
            List<SysModule> sysModules = sysModuleDao.findByFlag(Constant.USING);
            resultMap.put("success",true);
            resultMap.put("message","ok");
            resultMap.put("rows",sysModules);
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
        }
        return resultMap;
    }
}
