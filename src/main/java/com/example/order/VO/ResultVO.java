package com.example.order.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * The outermost json returned by the http request
 * @param <T>
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 6018975295023672631L;

    private Integer code;

    private String msg;

    private T data;
}
