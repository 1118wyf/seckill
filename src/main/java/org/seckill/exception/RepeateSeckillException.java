package org.seckill.exception;

/**
 * 重复秒杀异常（运行期异常）
 * Java中异常包括运行期异常和编译期异常
 * Created by wyf on 2021/12/18 9:53
 */
public class RepeateSeckillException extends SeckillException {

    public RepeateSeckillException(String message) {
        super(message);
    }

    public RepeateSeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
