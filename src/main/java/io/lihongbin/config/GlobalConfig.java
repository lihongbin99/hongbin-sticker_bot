package io.lihongbin.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IdentityHashMap;

public class GlobalConfig {

    public final static SerializeConfig SERIALIZE_CONFIG = new SerializeConfig(IdentityHashMap.DEFAULT_SIZE, true);

}
