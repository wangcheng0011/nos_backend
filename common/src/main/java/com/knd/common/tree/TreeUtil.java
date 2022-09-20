package com.knd.common.tree;

import com.knd.common.basic.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TreeUtil {

    public static List toTree(List<Map> entitys) {
        Map<Object, Map> map = new HashMap<>();
        for (Map entity : entitys) {
            List list = new ArrayList<>();
            entity.put("children", list);
            map.put(entity.get("value"), entity);
        }


        for (Object key : map.keySet()) {
            Map ma = map.get(key);
            Object parentValue = ma.get("parentValue");
            if (!parentValue.equals("0")) {
                Map parent = map.get(parentValue);
                if (StringUtils.isEmpty(parent)) {
                    log.info("有节点为空node:{}", parentValue);
                    continue;
                }
                List children = (List) parent.get("children");
                children.add(ma);

            } else {

            }
        }
        List<Object> list = new ArrayList<>();
        for (Object key : map.keySet()) {
            Map ma = map.get(key);
            Object parentValue = ma.get("parentValue");
            if (parentValue.equals("0")) {
                list.add(ma);
            }

        }

        return list;

    }


}
