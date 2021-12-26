package org.seckill.exception;

/**
 * 秒杀相关业务的异常
 * Created by wyf on 2021/12/18 10:05
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
