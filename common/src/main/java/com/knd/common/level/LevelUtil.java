package com.knd.common.level;

public class LevelUtil {
    public static String getLevel(int starCount) {
//        a. Legend（传说）
//        b. Beast（野兽）
//        c. Master（大师）
//        d. Elite（精英）
//        e. Advanced（高级）
//        f. High Intermediate（准高级）
//        g. Intermediate（中级）
//        h. Beginner（初级）
//        i. Freshman（新手）

        if (starCount < 5) {
            return "新手";
        } else if (starCount < 10) {
            return "初级";
        } else if (starCount < 15) {
            return "中级";
        } else if (starCount < 20) {
            return "准高级";
        } else if (starCount < 25) {
            return "高级";
        } else if (starCount < 30) {
            return "精英";
        } else if (starCount < 35) {
            return "大师";
        } else if (starCount < 40) {
            return "野兽";
        } else {
            return "传说";
        }

    }

}
