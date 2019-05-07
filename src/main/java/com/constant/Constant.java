package com.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/2/24
 */
public class Constant {

    public static final int USING = 1;
    public static final int NOT_USING = 0;

    public static final String SA = "$yc@";

    /**
     * 单位代码与首页地图单位名称map
     */
    public static Map<String, String> departCodeNameMap = new HashMap<>();
    static {
        departCodeNameMap.put("310107410000", "长寿所");
        departCodeNameMap.put("310107420000", "甘泉所");
        departCodeNameMap.put("310107430000", "石泉所");
        departCodeNameMap.put("310107440000", "真如所");
        departCodeNameMap.put("310107510000", "白玉所");
        departCodeNameMap.put("310107520000", "东新所");
        departCodeNameMap.put("310107530000", "宜川所");
        departCodeNameMap.put("310107540000", "中山所");
        departCodeNameMap.put("310107550000", "曹杨所");
        departCodeNameMap.put("310107560000", "万里所");
        departCodeNameMap.put("310107570000", "长风所");
        departCodeNameMap.put("310107580000", "真光所");
        departCodeNameMap.put("310107590000", "长征所");
        departCodeNameMap.put("310107600000", "桃浦所");
        departCodeNameMap.put("310107610000", "白丽所");
    }
}
