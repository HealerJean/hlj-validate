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
