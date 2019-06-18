package com.controller.level;

import com.config.Config;
import com.dao.PersonLevelScoreDao;
import com.dao.QualityScoreRangeDao;
import com.entity.PersonLevelScore;
import com.entity.QualityScoreRange;
import com.entity.WorkQueue;
import com.google.common.collect.Lists;
import com.service.PersonLevelSetService;
import com.service.QualityScoreService;
import com.service.WorkQueueService;
import com.support.HttpComponent;
import com.support.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value = "/pages/image")
public class ImageReachSetController {

    @Autowired
    private QualityScoreRangeDao qualityScoreRangeDao;
    @Autowired
    private PersonLevelScoreDao personLevelScoreDao;
    @Autowired
    private Config config;
    @Autowired
    private WorkQueueService workQueueService;
    @Autowired
    private PersonLevelSetService personLevelSetService;
    @Autowired
    private QualityScoreService qualityScoreService;

    @RequestMapping(value = "/getLevelScore",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getLevelScore(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,int[]> scoreMap = new HashMap<>();
        try{
            List<QualityScoreRange> qualityScoreRangeList = Lists.newArrayList(qualityScoreRangeDao.findAll());
            for(QualityScoreRange qualityScoreRange : qualityScoreRangeList){
                scoreMap.put(String.valueOf(qualityScoreRange.getLevel()),new int[]{qualityScoreRange.getMinScore(),qualityScoreRange.getMaxScore()});
            }
            if(scoreMap.size() >0 ){
                resultMap.put("scoreMap",scoreMap);
            }
            resultMap.put("success",true);
            resultMap.put("message","ok");

        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
            ex.printStackTrace();
        }
        return resultMap;
    }

    @RequestMapping(value ="/getFingerLevel",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object>  getFingerLevel(){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,PersonLevelScore> levelMap = new HashMap<>();
        try{
            List<PersonLevelScore> personLevelScoreList =  Lists.newArrayList(personLevelScoreDao.findAll());
            for(PersonLevelScore personLevelScore :personLevelScoreList){
                levelMap.put(personLevelScore.getLevel(),personLevelScore);
            }

            resultMap.put("success",true);
            resultMap.put("message","ok");
            resultMap.put("levelMap",levelMap);
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
            ex.printStackTrace();
        }

        return resultMap;
    }

    @RequestMapping(value = "/levelScore",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> LevelScoreSet(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,int[]> scoreMap = new HashMap<>();
        try{
            int repeat = Integer.parseInt(request.getParameter("repeat"));
            int[] stateArr = {1,2,3};
            List<WorkQueue> workQueueList = workQueueService.getWorkQueueWorkState(stateArr);
            if(repeat ==1 && workQueueList.size()>0){
                if(workQueueList.get(0).getWorkState()== 1){
                    throw new Exception("正在处理历史人员是否达标,请稍后设置");
                }else{
                    throw new Exception("正在统计历史数据,请稍后设置");
                }
            }else {
                int yx_min = Integer.parseInt(request.getParameter("yx_min"));
                int yx_max = Integer.parseInt(request.getParameter("yx_max"));
                scoreMap.put("1", new int[]{yx_min, yx_max});
                int lh_min = Integer.parseInt(request.getParameter("lh_min"));
                int lh_max = Integer.parseInt(request.getParameter("lh_max"));
                scoreMap.put("2", new int[]{lh_min, lh_max});
                int yb_min = Integer.parseInt(request.getParameter("yb_min"));
                int yb_max = Integer.parseInt(request.getParameter("yb_max"));
                scoreMap.put("3", new int[]{yb_min, yb_max});
                int jc_min = Integer.parseInt(request.getParameter("jc_min"));
                int jc_max = Integer.parseInt(request.getParameter("jc_max"));
                scoreMap.put("4", new int[]{jc_min, jc_max});
                int hc_min = Integer.parseInt(request.getParameter("hc_min"));
                int hc_max = Integer.parseInt(request.getParameter("hc_max"));
                scoreMap.put("5", new int[]{hc_min, hc_max});

                for (int i = 1; i < 6; i++) {
                    QualityScoreRange qualityScoreRange = qualityScoreRangeDao.findByLevel(i);
                    if (null == qualityScoreRange) qualityScoreRange = new QualityScoreRange();
                    qualityScoreRange.setLevel(i);
                    qualityScoreRange.setMinScore(scoreMap.get(String.valueOf(i))[0]);
                    qualityScoreRange.setMaxScore(scoreMap.get(String.valueOf(i))[1]);
                    qualityScoreRange.setInputTime(new Date());

                    String name = null;
                    switch (i) {
                        case 1:
                            name = "优秀";
                            break;
                        case 2:
                            name = "良好";
                            break;
                        case 3:
                            name = "一般";
                            break;
                        case 4:
                            name = "较差";
                            break;
                        case 5:
                            name = "很差";
                            break;
                    }
                    qualityScoreRange.setName(name);
                    qualityScoreRangeDao.save(qualityScoreRange);
                }

                resultMap.put("success", true);
                resultMap.put("message", "图像质量等级范围设置成功");
            }
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
            ex.printStackTrace();
        }

        return resultMap;
    }

    @RequestMapping(value ="/fingerLevel",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object>  fingerLevelSet(@RequestParam("value_a[]") int[] array_a,
                                              @RequestParam("value_b[]") int[] array_b,
                                              @RequestParam("value_c[]") int[] array_c,
                                              @RequestParam("repeat") int repeat){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,int[]> levelMap = new HashMap<>();
        try{
            levelMap.put("A",array_a);
            levelMap.put("B",array_b);
            levelMap.put("C",array_c);

            if(repeat == 1){
                int[] stateArr = {1,2,3};
                List<WorkQueue> workQueueList = workQueueService.getWorkQueueWorkState(stateArr);
                if(workQueueList.size()>0){
                    if(workQueueList.get(0).getWorkState()== 1){
                        throw new Exception("正在处理历史人员是否达标，请稍后设置");
                    }else{
                        throw new Exception("正在统计历史数据，请稍后设置");
                    }
                }else {
                    levelScoreSet(levelMap);
                    // 按照新规则判断人员指纹是否达标
                    HttpResult httpResult = HttpComponent.rpc_get(config.getApiUrl() + "/FingerGradePage");
                    if (httpResult.getSuccess()) {
                        resultMap.put("success", true);
                        resultMap.put("message", "人员指位等级设置成功,开始重新判定历史人员指纹采集是否达标");
                    } else {
                        throw new Exception("判定历史人员指纹采集是否达标");
                    }
                }
            }else{
                levelScoreSet(levelMap);
                resultMap.put("success",true);
                resultMap.put("message","人员指位等级设置成功");
            }

        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
            ex.printStackTrace();
        }

        return resultMap;
    }


    private void levelScoreSet(Map<String,int[]> levelMap){
        for(Map.Entry<String,int[]> map : levelMap.entrySet()){
            PersonLevelScore personLevelScore = personLevelScoreDao.findByLevel(map.getKey());
            if(personLevelScore == null) personLevelScore = new PersonLevelScore();
            personLevelScore.setLevel(map.getKey());
            personLevelScore.setRm(map.getValue()[0]);
            personLevelScore.setRs(map.getValue()[1]);
            personLevelScore.setRz(map.getValue()[2]);
            personLevelScore.setRh(map.getValue()[3]);
            personLevelScore.setRx(map.getValue()[4]);
            personLevelScore.setLm(map.getValue()[5]);
            personLevelScore.setLs(map.getValue()[6]);
            personLevelScore.setLz(map.getValue()[7]);
            personLevelScore.setLh(map.getValue()[8]);
            personLevelScore.setLx(map.getValue()[9]);
            personLevelScore.setInputTime(new Date());
            personLevelScoreDao.save(personLevelScore);
        }
    }

    /**
     * 获取所有等级相关设置
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAllLevelSet")
    public Map<String, String> getAllLevelSet(){
        Map<String, String> result = new HashMap<>();
        //人员等级中的每个指位等级设置
        Map<String,PersonLevelScore> map = personLevelSetService.getPersonLevelScoreSet();
        for(String key : map.keySet()){
            PersonLevelScore personLevelScore = map.get(key);
            result.put(key+1,personLevelScore.getRm()+"");
            result.put(key+2,personLevelScore.getRs()+"");
            result.put(key+3,personLevelScore.getRz()+"");
            result.put(key+4,personLevelScore.getRh()+"");
            result.put(key+5,personLevelScore.getRx()+"");
            result.put(key+6,personLevelScore.getLm()+"");
            result.put(key+7,personLevelScore.getLs()+"");
            result.put(key+8,personLevelScore.getLz()+"");
            result.put(key+9,personLevelScore.getLh()+"");
            result.put(key+10,personLevelScore.getLx()+"");
        }
        //获取每个人员级别的分数设置
        Iterable<QualityScoreRange> qualityScoreRanges = qualityScoreService.getAllQualityScoreRange();
        for(QualityScoreRange qualityScoreRange : qualityScoreRanges){
            result.put(qualityScoreRange.getLevel()+"", qualityScoreRange.getMinScore()+"");
        }
        return result;
    }


}