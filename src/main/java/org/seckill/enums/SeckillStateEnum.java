package org.seckill.enums;

/**
 * 使用枚举表示常量数据字段
 * Created by wyf on 2021/12/18 19:19
 */
public enum SeckillStateEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEATE_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "内部错误"),

    DATA_REWRITE(-3, "数据被改写");

    private int state;
    private String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStateEnum stateOf(int index){
        for(SeckillStateEnum state : values() ){
            if(state.getState() == index){
                return state;
            }
        }
        return null;
    }
}
