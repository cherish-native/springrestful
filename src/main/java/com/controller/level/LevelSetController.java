package com.controller.level;

import com.config.Config;
import com.support.HttpComponent;
import com.support.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/3/28
 */
@Controller
@RequestMapping(value = "/pages/main/level")
public class LevelSetController {


    @Autowired
    private Config config;

    @RequestMapping(value = "/personLevelSet",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> personLevelSet(@RequestParam("addressCode") String addressCode
                                            ,@RequestParam("raceCode") String raceCode
                                            ,@RequestParam("caseCode") String caseCode
                                            ,@RequestParam("gender") String gender
                                            ,@RequestParam("minAge") String minAge
                                            ,@RequestParam("maxAge") String maxAge
                                            ,@RequestParam("criminalRecord") String criminalRecord){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            PersonLevelBean personLevelBean = new PersonLevelBean();
            personLevelBean.setAddressCode(addressCode);
            personLevelBean.setRaceCode(raceCode);
            personLevelBean.setCaseCode(caseCode);
            personLevelBean.setGender(gender);
            personLevelBean.setMinAge(minAge);
            personLevelBean.setMaxAge(maxAge);
            personLevelBean.setCriminalRecord(criminalRecord);
            HttpResult httpResult = HttpComponent.rpc_post(config.getApiUrl() + "/PersonLevelSetPage",HttpComponent.registerKryo(personLevelBean));
            if(httpResult.getSuccess()){
                resultMap.put("success",true);
                resultMap.put("message","设置成功");
                resultMap.put("result",httpResult.getResult());
            }else{
                throw new Exception(httpResult.getMessage());
            }

        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
        }
        return resultMap;
    }



    @RequestMapping(value = "/isSetPersonLevel",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> isSetPersonLevel(){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            HttpResult httpResult = HttpComponent.rpc_get(config.getApiUrl() + "/CheckSetPersonLevelPage");
            if(httpResult.getSuccess()){
                resultMap.put("success",true);
                resultMap.put("result",httpResult.getResult());
            }else{
                throw new Exception(httpResult.getMessage());
            }
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
        }
        return resultMap;
    }
}

class PersonLevelBean{

    private String addressCode;
    private String raceCode;
    private String caseCode;
    private String gender;
    private String minAge;
    private String maxAge;
    private String criminalRecord;

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getRaceCode() {
        return raceCode;
    }

    public void setRaceCode(String raceCode) {
        this.raceCode = raceCode;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getCriminalRecord() {
        return criminalRecord;
    }

    public void setCriminalRecord(String criminalRecord) {
        this.criminalRecord = criminalRecord;
    }
}
