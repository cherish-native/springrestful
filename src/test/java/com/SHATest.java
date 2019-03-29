package com;

import com.constant.Constant;
import org.junit.Test;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/3/19
 */
public class SHATest {

    @Test
    public void sha1_Test(){
        String str = DigestUtils.sha1Hex(("1" + Constant.SA).getBytes());
        System.out.println(str);
    }

    @Test
    public void uuid_Test(){
        System.out.println(UUID.randomUUID().toString().replace("-",""));
    }
}
