package com.controller.level;

import com.config.Config;
import com.dao.*;
import com.entity.*;
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
    @Autowired
    private PersonLevelDao personLevelDao;
    @Autowired
    private HuKouDimenDao huKouDimenDao;
    @Autowired
    private CaseDimenDao caseDimenDao;
    @Autowired
    private CodeAreaDao codeAreaDao;
    @Autowired
    private CodeCaseClassDao codeCaseClassDao;

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
                    throw new Exception("正在处理历史人员是否达标,请稍后设置");
                }else{
                    throw new Exception("正在统计历史数据，请稍后设置");
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
                    resultMap.put("success", true);
                    resultMap.put("message", "设置成功");
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


    @RequestMapping(value = "/getPersonLevel",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getPersonLevel(){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            PersonLevel personLevel = personLevelDao.findByFlag(1);
            List<HuKouDimen> huKouDimenList = huKouDimenDao.findByDimenId(personLevel.getHukouDimenId());
            List<CaseDimen> caseDimenList = caseDimenDao.findByDimenId(personLevel.getCaseDimenId());
            String addressCodes = "";
            String caseCodes = "";
            List<CodeArea> codeAreaList = new ArrayList();
            List<CodeCaseClass> codeCaseClassList = new ArrayList();
            for(HuKouDimen huKouDimen : huKouDimenList){
                String addressCode = huKouDimen.getAddressCode();
                CodeArea codeArea = codeAreaDao.findByCode(addressCode);
                codeAreaList.add(codeArea);
                if(codeArea != null) {
                    addressCodes += codeArea.getName() + ";";
                }
            }
            for(CaseDimen caseDimen : caseDimenList){
                String caseCode = caseDimen.getCaseCode();
                CodeCaseClass codeCaseClass = codeCaseClassDao.findByCode(caseCode);
                codeCaseClassList.add(codeCaseClass);
                if(codeCaseClass != null){
                    caseCodes += codeCaseClass.getName() + ";";
                }
            }

            resultMap.put("codeAreaList", codeAreaList);
            resultMap.put("codeCaseClassList", codeCaseClassList);
            resultMap.put("addressCodes", addressCodes);
            resultMap.put("caseCodes", caseCodes);
            resultMap.put("success",true);
            resultMap.put("message","ok");
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
        }
        return resultMap;
    }


}
