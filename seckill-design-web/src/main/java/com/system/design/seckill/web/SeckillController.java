package com.system.design.seckill.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chengzhengzheng
 * @date 2022/1/29
 */
@Controller
@RequestMapping("/seckill")
@SuppressWarnings("all")
public class SeckillController {
    /**
     * 查询秒杀商品列表
     *
     * @return
     */
    @RequestMapping("/product/list")
    public String productList() {
        return "";
    }

    /**
     * 查询秒杀商品详情信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/product/detail")
    public String productDetail(@RequestParam("id") String id) {
        return "";
    }

    /**
     * 查询秒杀开始时间
     *
     * @param id
     * @return
     */
    @RequestMapping("/start_time/query")
    public Long queryStartTime(@RequestParam("id") String id) {
        return 1L;
    }

    /**
     * 查询秒杀地址url
     *
     * @param productId
     * @param id
     * @return
     */
    @RequestMapping("/url/query")
    public String getSkillUrl(@RequestParam("id") String productId, @RequestParam("id") String id) {
        return "";
    }

    /**
     * 秒杀
     *
     * @return
     */
    @RequestMapping("/execute/do")
    public String executeSeckill(@RequestParam("id") String productId, @RequestParam("id") String id) {
        return "";
    }

    /**
     * 秒杀结果查询
     *
     * @param productId
     * @param id
     * @return
     */
    @RequestMapping("/kill/result/query")
    public String queryKillResult(@RequestParam("id") String productId, @RequestParam("id") String id) {
        return "";
    }

}
