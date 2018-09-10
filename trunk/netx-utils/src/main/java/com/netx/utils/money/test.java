package com.netx.utils.money;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class test {

    @Test
    public void test(){
        BigDecimal decimal = new BigDecimal("1.12");
        System.out.println(new Money(decimal).getAmount());
        System.out.println(new Money(decimal).getCent());
        System.out.println(Money.getMoneyString(112l));
    }
}
