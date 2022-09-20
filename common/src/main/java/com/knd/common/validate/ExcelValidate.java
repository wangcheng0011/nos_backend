package com.knd.common.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: libb
 * @date: 2020.03.31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelValidate {

    /**
     * 项目名
     *
     * @return
     */
    public String name();

    /**
     * 列
     *
     * @return
     */
    public int index();

    /**
     * 新增是否必填
     *
     * @return
     */
    public boolean addRequired() default true;

    /**
     * 编辑是否必填
     *
     * @return
     */
    public boolean editRequired() default true;

    /**
     * 类型
     *
     * @return
     */
    public DataType type();

    /**
     * 最大数字
     *
     * @return
     */
    public double max() default 9999999999999999.9999;

    /**
     * 最小数字
     *
     * @return
     */
    public double min() default -9999999999999999.9999;

    /**
     * 最大长度 0：不限
     *
     * @return
     */
    public int maxLength() default 0;

    /**
     * 默认日期格式
     *
     * @return
     */
    public String dateFormat() default "yyyyMMdd";

    /**
     * 静态字典比较字段
     *
     * @return
     */
    public DictionaryStaticType dictionaryStaticType() default DictionaryStaticType.VALUE;

    /**
     * 静态字典值   静态字典比较字段 = VALUE 时 必填
     *
     * @return
     */
    public String dictionaryStaticField() default "";

    /**
     * 指定属性有值时当前字段非空
     *
     * @return
     */
    public String[] relyRequired() default {};

    /**
     * 数据类型
     */
    public enum DataType {
        STRING("STRING"),
        INTEGER("INTEGER"),
        DECIMAL("DECIMAL"),
        DATE("DATE");
        private String type;

        DataType(String type) {
            this.type = type;
        }
    }

    /**
     * 字典比较类型
     */
    public enum DictionaryStaticType {
        VALUE("VALUE"),
        LABEL("LABEL");
        private String type;

        DictionaryStaticType(String type) {
            this.type = type;
        }
    }
}


