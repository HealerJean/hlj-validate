---
title: 自定义工具类实现validate参数校验
date: 2019-04-18 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: 自定义工具类实现validate参数校验
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    


相信项目中做一些htttp接口，避免不了要对参数进行校验，大多数情况下，其实我们只是校验是否为NULL就可以了

## 1.1、简单判断是否为null

```java

import java.lang.reflect.Field;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/1/24  下午4:30.
 * 类描述：判断是否为null 工具
 */
public class JudgeNullUtils {

    public static boolean isNull(Object object,String ... fieldName){
        try {
            for (int i = 0; i < fieldName.length; i++) {
                Field field = null;

                    field = object.getClass().getDeclaredField(fieldName[i]);

                field.setAccessible(true);//暴力反射，获取获取数据
                if(field.get(object)==null){
                    //返回flase或者直接抛出异常，根据我们的情况而定
                    throw  new AppException(fieldName[i]+"不能为空");
                }
            }
            return true ;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false ;
    }

}

```


## 2、通过注解实现各种状态的字段

### 2.1、引入依赖

```xml

        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>validation-api</artifactId>
                    <groupId>javax.validation</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <artifactId>validation-api</artifactId>
            <groupId>javax.validation</groupId>
        </dependency>

```

### 2.2、解释一些注解

```
空检查
@Null       验证对象是否为null
@NotNull    验证对象是否不为null, 无法查检长度为0的字符串
Hibernate 
@NotEmpty   检查约束元素是否为NULL或者是EMPTY.
@NotBlank 检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格.

Booelan检查
@AssertTrue     验证 Boolean 对象是否为 true  
@AssertFalse    验证 Boolean 对象是否为 false  
 
长度检查
@Size(min=, max=) 验证对象（Array,Collection,Map,String）长度是否在给定的范围之内  
@Length(min=, max=) 验证字符串的长度
 
日期检查
@Past           验证 Date 和 Calendar 对象是否在当前时间之前  
@Future     验证 Date 和 Calendar 对象是否在当前时间之后  
@Pattern(regexp="[1-9]{1,3}", message="数量X: 必须为正整数，并且0<X<1000")   验证 String 对象是否符合正则表达式的规则
 
数值检查，建议使用在Stirng,Integer类型，不建议使用在int类型上，因为表单值为“”时无法转换为int，但可以转换为Stirng为"",Integer为null
@Min            验证 Number 和 String 对象是否大等于指定的值  
@Max            验证 Number 和 String 对象是否小等于指定的值  
@DecimalMax 被标注的值必须不大于约束中指定的最大值. 这个约束的参数是一个通过BigDecimal定义的最大值的字符串表示.小数存在精度
@DecimalMin 被标注的值必须不小于约束中指定的最小值. 这个约束的参数是一个通过BigDecimal定义的最小值的字符串表示.小数存在精度
@Digits(integer=,fraction=) 验证字符串是否是符合指定格式的数字，interger指定整数精度，fraction指定小数精度。

Hibernate  
@Range(min=10000,max=50000,message="range.bean.wage")
 
 
@Valid 对象传递参数的时候用到
public String doAdd(Model model, @Valid AnimalForm form, BindingResult result){}

其中 	Hibernate Validator 附加的 constraint  （也就是说如果下面的内容中，不引入hibernate包就不会起作用）
@NotBlank 检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格.
@Email  验证是否是邮件地址，如果为null,不进行验证，算通过验证。
@Length(min=, max=) 验证字符串的长度
@NotEmpty   被注释的字符串的必须非空  
@Range(min=,max=,message=)  被注释的元素必须在合适的范围内 


```



### 2.3、DTO类

```java
package data.vialidate;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author HealerJean
 * @version 1.0v
 * @Description
 * @ClassName JavaBean
 * @date 2019/4/17  14:08.
 */
@Data
public class JavaBean {

    //字符串长度校验 {"name":"123456"}
    @NotBlank(message = "name 为空 ",groups = ValidateGroup.Start.class)
    @Size(min = 1,max = 5,message = "name  @Size(min = 1,max = 5  字符串长度 最低为1 最大为5",groups = ValidateGroup.Start.class)
    private String name ;

    //集合大小校验 {"list":["one","two","three"]}
    @Size(min = 1,max = 2, message = "list @Size(min = 1,max = 2 集合大小 最低为1 最大为2",groups = ValidateGroup.Start.class)
    private List<String> list;

    //字符串长度校验 {"name":"1234","list":["one","two"],"strLength":"123456"}
    @Length(min = 1,max = 5,message = "@Length(min = 1,max = 5 字符串长度 最低为1 最大为5",groups = ValidateGroup.Start.class)
    private String strLength;

    // 字符串（数字的字符串大小判断） {"name":"1234","list":["one","two"],"strLength":"12345","strNum":"4"}
    @Min(value = 5,         message = "strNum  @Min(value = 5,message =  字符串（数字的字符串大小判断）【数字类型的变量都可以】",groups = ValidateGroup.Start.class)
    private String strNum ;

    //数值范围的判断（整数）  {"name":"1234","list":["one","two"],"strLength":"12345","strNum":"6","strRange":"20"}
    @Range(min = 1,max = 10 ,message = "strRange @Range(min = 1,max = 10  最小为1，最大为10  ",groups = ValidateGroup.Start.class)
    private String strRange ;

    //包含小数点的判断  {"name":"1234","list":["one","two"],"strLength":"12345","strNum":"6","strRange":"9","strDecimal":"100"}
    @DecimalMin(value = "100.1",message = "小数值的判断，最小为 100.1",groups = ValidateGroup.Start.class)
    private String strDecimal ;

    //整数位数和小数位数的判断  {"name":"1234","list":["one","two"],"strLength":"12345","strNum":"6","strRange":"9","strDecimal":"100.2","strDigts":"15.666"}
    @Digits(integer = 2,fraction = 2,message = "strDigts  @Digits(integer = 2,fraction = 2  整数最高2位，小数最高2位",groups = ValidateGroup.Start.class)
    private String strDigts;
}

```





### 2.4、校验工具类



```java
package com.hlj.utils;

import com.hlj.data.general.AppException;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Description 校验工具
 */
public class ValidateUtils {

    public static Validator validator;

    static {

        validator = Validation
                .byProvider(HibernateValidator.class)
                .configure()
                //快速返回模式，有一个验证失败立即返回错误信息
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
    }

    /**
     * 静态方法校验使用的
     *
     * @param object
     * @return
     */
    public static String validate(Object object) {
        if(object == null){
            throw new AppException("参数不完整");
        }
        Set<ConstraintViolation<Object>> validate = validator.validate(object);
        return resultValidate(validate);

    }

    /**
     * 静态方法校验使用，并且带分组的
     *
     * @param object
     * @param group
     * @return
     */
    public static String validate(Object object, Class group) {
        Set<ConstraintViolation<Object>> validate = validator.validate(object, group);
        return resultValidate(validate);
    }


    private static String resultValidate(Set<ConstraintViolation<Object>> validate) {
        if (!validate.isEmpty()) {
            final StringBuffer stringBuffer = new StringBuffer();
            validate.stream().forEach(
                    item -> stringBuffer.append(item.getMessage()).append(","));
            stringBuffer.setLength(stringBuffer.length() - 1);
            return stringBuffer.toString();
        }
        return "success";
    }

}

```



### 2.5、测试



```java
package com.hlj.moudle.validate;


import com.hlj.data.general.AppException;
import com.hlj.data.general.ResponseBean;
import com.hlj.utils.ExceptionLogUtils;
import com.hlj.utils.ValidateUtils;
import data.vialidate.JavaBean;
import data.vialidate.ValidateGroup;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  上午10:22.
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "demo控制器")
@Controller
@RequestMapping("validate")
@Slf4j
public class VialidateController {


    @ApiOperation(value = "Post接口",
            notes = "Post接口",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseBean.class
    )
    @PostMapping( value = "start",produces="application/json;charset=utf-8")
    @ResponseBody
    public ResponseBean post(@RequestBody JavaBean JavaBean){
        String validate = ValidateUtils.validate(JavaBean, ValidateGroup.Start.class);
        if(!"success".equals(validate)){
            log.info("错误信息：{}", validate);
        }
        return ResponseBean.buildSuccess(validate);
    }

}

```









<br/>
<br/>

<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>

<br/>

哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|                             微信                             |                          微信公众号                          |                            支付宝                            |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg) | ![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg) | ![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |




## [代码下载](https://github.com/HealerJean/hlj-validate)