package com.controller.level;

import com.config.Config;
import com.entity.WorkQueue;
import com.service.PersonLevelSetService;
import com.service.WorkQueueService;
import com.support.HttpComponent;
import com.support.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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
    @Autowired
    private WorkQueueService workQueueService;

    @RequestMapping(value = "/personLevelSet",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> personLevelSet(@RequestParam("addressCode") String addressCode
                                            ,@RequestParam("caseCode") String caseCode
                                            ,@RequestParam("repeat") int repeat){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            int[] stateArr = {1,2,3};
            List<WorkQueue> workQueueList = workQueueService.getWorkQueueWorkState(stateArr);
            if(workQueueList.size()>0){
                if(workQueueList.get(0).getWorkState()== 1){
                    throw new Exception("设置失败，正在处理历史人员是否达标");
                }else{
                    throw new Exception("设置失败，正在统计历史数据");
                }
            }else {
                String[] addCodeArr = addressCode.split(",");
                String[] caseCodeArr = caseCode.split(",");
                personLevelSetService.personLevelSave(addCodeArr, caseCodeArr);

                if (repeat == 1) {
                    //修改personinfo的人员等级字段为空
                    personLevelSetService.updatePersonLevel();
                    //添加work_queue表数据
                    WorkQueue workQueue = new WorkQueue();
                    workQueue.setInsertTime(new Date());
                    workQueue.setBeginTime(new Date());
                    workQueue.setWorkType(workQueue.TYPE_IMG_LEVEL);
                    workQueue.setWorkState(workQueue.STATE_SERVICE_RUNNING);
                    workQueueService.save(workQueue);
                    String result = HttpComponent.html_get("http://" +config.getWebConfig().getBind() + "/pages/statistics/changeWorkQueueState/1");
                    if(result.equals("success")){
                        resultMap.put("success",true);
                        resultMap.put("message","设置成功，并计算历史数据后重新统计");
                    }else{
                        throw new Exception("发送changeWorkQueueState消息1失败");
                    }
                } else {
                    resultMap.put("success", true);
                    resultMap.put("message", "设置成功");
                }
            }
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
            ex.printStackTrace();
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
