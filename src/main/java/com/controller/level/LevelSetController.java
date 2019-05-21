package com.controller.level;

import com.config.Config;
import com.service.PersonLevelSetService;
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
    @Autowired
    private PersonLevelSetService personLevelSetService;

    @RequestMapping(value = "/personLevelSet",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> personLevelSet(@RequestParam("addressCode") String addressCode
                                            ,@RequestParam("caseCode") String caseCode){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            String[] addCodeArr = addressCode.split(",");
            String[] caseCodeArr = caseCode.split(",");

            personLevelSetService.personLevelSave(addCodeArr,caseCodeArr);

            resultMap.put("success",true);
            resultMap.put("message","设置成功");

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
