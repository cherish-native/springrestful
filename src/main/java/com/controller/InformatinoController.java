package com.controller;


import com.constant.Constant;
import com.dao.InformationDao;
import com.entity.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class InformatinoController {

    @Autowired
    private InformationDao informationDao;


    @RequestMapping(value = "/addInformation",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addInformation(HttpServletRequest request){

        Map<String,Object> resultMap = new HashMap<>();
        try{
            Information information = new Information();
            information.setName(request.getParameter("name"));
            information.setCode(request.getParameter("code"));
            information.setIp(request.getParameter("ip"));
            information.setHost(request.getParameter("host"));
            information.setInitTime(new Date());
            information.setIsStart(1);
            informationDao.save(information);
            resultMap.put("success",true);
            resultMap.put("message","添加成功");
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/getInformationList")
    @ResponseBody
    public List<Information> getInformationList(){
        List<Information> informationList = informationDao.findByIsStart(1);
        return informationList;
    }

    @RequestMapping(value = "/getInformation")
    @ResponseBody
    public Information getInformation(String[] ids){
        String id = ids[0];
        Information information = informationDao.findById(id);
        return information;
    }

    @RequestMapping(value = "/deleteInformation")
    public void deleteInformation(String[] ids){
        for(String id :ids){
            informationDao.deleteInformationById(id);
        }
    }

}