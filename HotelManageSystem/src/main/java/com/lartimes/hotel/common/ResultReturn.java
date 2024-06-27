package com.lartimes.hotel.common;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/19 10:27
 */
public class ResultReturn {
    public static final int OK = 200;
    public static final Result RESULT = new Result(OK , "");

    public static Result success(){
        return RESULT;
    }


    public static Result success(Object ret){
        return new Result(OK , "" , ret);
    }



    public static Result success(String info){
        return new Result(OK , info);
    }

    public static Result failure(int code , String info){
        return new Result(code , info);
    }

}
