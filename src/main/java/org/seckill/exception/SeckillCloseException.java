package org.seckill.exception;

/**
 * 秒杀关闭异常
 * Created by wyf on 2021/12/18 10:02
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
