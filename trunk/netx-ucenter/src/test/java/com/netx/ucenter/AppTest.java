package com.netx.ucenter;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.netx.common.common.utils.Logcat;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.ucenter.model.user.User;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private PasswordEncoder encoder;

    @Test
    public void test(){
        Logcat.info("hhhh#0","wwwww");
    }
    @Test
    public void testJson() throws PinyinException {
        String str = "你好世界";
        PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_MARK); // nǐ,hǎo,shì,jiè
        PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_NUMBER); // ni3,hao3,shi4,jie4
        String s = PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
        PinyinHelper.getShortPinyin(str); // nhsj
        System.out.println(s);
    }

}
