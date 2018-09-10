package com.netx.ucenter.util;

import org.springframework.stereotype.Component;

/**
 * <p>
 * 网号操作 工具类
 * </p>
 *
 * @author 黎子安
 * @since 2017-08-27
 */
public class UserNumberGenerator {
    private int numberLen;//网号长度
    private String[] luckNum;//幸运数段
    public UserNumberGenerator(){
        numberLen=10;
        this.luckNum=new String[]{"168","198","178","818","816",
                "618","616","788","688","988",
                "889","919","520","521","188",
                "166","668","686","886"};
    }

    public int getNumberLen() {
        return numberLen;
    }

    public void setNumberLen(int numberLen) {
        this.numberLen = numberLen;
    }

    public String[] getLuckNum() {
        return luckNum;
    }

    public void setLuckNum(String[] luckNum) {
        this.luckNum = luckNum;
    }

    /*
     * 检测网号是否含有幸运数段
     * @param 输入网号（long长整型）
     * @return {true:含有,false:不含有}
     */
    public boolean checkLuck(String num){
        int luckLen=luckNum.length;
        for(int i=0;i<luckLen;i++)
            if(num.indexOf(luckNum[i])!=-1)
                return true;
        return false;
    }
    /*
     * 检测网号是否含有顺号或重复数段
     * @param 输入网号（long长整型）
     * @return {true:含有,false:不含有}
     */
    public boolean checkOrder(String num,int n){
        int k=Integer.parseInt(num.charAt(0)+"")-Integer.parseInt(num.charAt(1)+"");//相邻数的差值
        //差值不为1或0的不是顺号或重复数字
        if(Math.abs(k)!=1 && k!=0) return false;
        //
        for(int i=1;i+1<n;i++)
            if(k!=Integer.parseInt(num.charAt(i)+"")-Integer.parseInt(num.charAt(i+1)+""))
                return false;
        return true;
    }
}
