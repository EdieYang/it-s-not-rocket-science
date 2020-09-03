package com.rocketscience.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回结果实体类
 *
 * @author Eddie
 * @since
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    private Integer code;

    private String message;

    private T data;


    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }

}
