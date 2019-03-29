package com;

import com.config.Config;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/2/22
 */
public class XMLTest {

    @Test
    public void test_parseXML() throws IOException {
        Config config = JAXB.unmarshal(getClass().getResourceAsStream("/config/config.xml")
                , Config.class);
        Assert.assertNotNull(config.getDataBaseConfig());
        Assert.assertNotNull(config.getZookeeperConfig());
    }
}
