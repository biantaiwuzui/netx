package com.netx.utils.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.netx.utils.money.Money;
import com.netx.utils.annotation.LongCurrency;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @author Closure.Yang
 * @since 2016/3/2
 */
public class LongCurrencySerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) {
            serializer.getWriter().writeNull();
            return;
        }
        String text = String.valueOf(object);
        try {
            Object ctxObject = serializer.getContext().getObject();
            Field field = ctxObject.getClass().getDeclaredField(String.valueOf(fieldName));
            Annotation annotation = field.getAnnotation(LongCurrency.class);
            if (annotation != null) {
                text =  Money.getMoneyString((Long) object);
            }
        } catch (Exception e) {
        }
        serializer.write(text);
    }
}
