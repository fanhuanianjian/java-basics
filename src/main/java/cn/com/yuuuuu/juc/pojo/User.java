package cn.com.yuuuuu.juc.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: bhl
 * @Date: 2020/10/9
 * @Description: cn.com.yuuuuu.juc.pojo
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;

    private String name;

    private Integer age;

}
