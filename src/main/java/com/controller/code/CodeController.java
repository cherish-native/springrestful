package com.controller.code;

import com.entity.CodeArea;
import com.entity.CodeCaseClass;
import com.service.CodeAreaService;
import com.service.CodeCaseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/pages/code")
public class CodeController {

    @Autowired
    private CodeAreaService codeAreaService;
    @Autowired
    private CodeCaseClassService codeCaseClassService;

    @ResponseBody
    @RequestMapping("/listCodeAreaByParentCode")
    public List<CodeArea> listCodeByParentCode(String parentCode){
        List<CodeArea> codeAreas = codeAreaService.listCodeArea(parentCode);
        return codeAreas;
    }

    /**
     * 行政区划代码模糊检索
     * @return
     */
    @ResponseBody
    @RequestMapping("/fuzzySearchCodeArea")
    public List<Map<String, String>> fuzzySearchCodeArea(String q){
        List<CodeArea> codeAreas = codeAreaService.fuzzySearchCodeArea(q);
        List<Map<String, String>> result = new ArrayList<>();
        for(CodeArea codeArea : codeAreas){
            Map<String, String> map = new HashMap<>();
            map.put("id", codeArea.getCode());
            map.put("text", codeArea.getName());
            result.add(map);
        }
        return result;
    }


    /**
     * 查询案件类别代码
     * @param parentCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/listCodeCaseClass")
    public List<CodeCaseClass> listCodeCaseClass(String parentCode){
        return codeCaseClassService.listCodeCaseClass(parentCode);
    }

    /**
     * 案件类别代码模糊检索
     * @return
     */
    @ResponseBody
    @RequestMapping("/fuzzySearchCodeCaseClass")
    public List<Map<String, String>> fuzzySearchCodeCaseClass(String q){
        List<CodeCaseClass> codeCaseClasses = codeCaseClassService.fuzzySearchCaseClassCode(q);
        List<Map<String, String>> result = new ArrayList<>();
        for(CodeCaseClass codeCaseClass : codeCaseClasses){
            Map<String, String> map = new HashMap<>();
            map.put("id", codeCaseClass.getCode());
            map.put("text", codeCaseClass.getName());
            result.add(map);
        }
        return result;
    }

}
