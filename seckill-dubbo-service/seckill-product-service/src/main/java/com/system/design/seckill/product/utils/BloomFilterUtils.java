package com.system.design.seckill.product.utils;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * @description: Google布隆过滤器工具类
 * @author: 贾凯
 * @create: 2021-12-08 10:39
 */
public class BloomFilterUtils {

    public static void main(String[] args) {
        long expectedInsertions = 100000;
        double fpp = 0.00005;
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), expectedInsertions, fpp);
        for(int i=0;i<expectedInsertions;i++){
            bloomFilter.put("test"+i);
        }
        boolean containsValue1 = bloomFilter.mightContain("test99");
        boolean containsValue2 = bloomFilter.mightContain("test1000001");
        System.out.println(containsValue1);
        System.out.println(containsValue2);
//        Assert.notNull(containsValue1, "值为空");
//        Assert.isTrue(containsValue2, "不存在");
    }

}
