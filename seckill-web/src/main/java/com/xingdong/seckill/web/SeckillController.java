package com.xingdong.seckill.web;

import com.alibaba.fastjson.JSON;
import com.xingdong.seckill.common.api.IKillBuzService;
import com.xingdong.seckill.common.api.IProductService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengzhengzheng
 * @date 2022/1/29
 */
@RestController
@RequestMapping("/seckill")
@SuppressWarnings("all")
public class SeckillController {

    @DubboReference(version = "1.0.0")
    private IProductService productService;
    @DubboReference(version = "1.0.0")
    private IKillBuzService killBuzService;

    /**
     * 查询秒杀商品列表
     *
     * @return
     */
    @RequestMapping("/kill/list")
    public String kilList() {
        //TODO 改造为分页情况 @贾凯
        return JSON.toJSONString(killBuzService.getSeckillList());
    }

    /**
     * 查询秒杀商品详情信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/kill/detail")
    public String killDetail(@RequestParam("id") String id) {
        return JSON.toJSONString(killBuzService.getById(id));
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
    public String getSkillUrl(@RequestParam("id") Long productId, @RequestParam("id") Long id) {
        return JSON.toJSONString(killBuzService.exportKillUrl(productId, id));
    }

    /**
     * 秒杀
     *
     * @return
     */
    @RequestMapping("/execute/do")
    public String executeSeckill(@RequestParam("id") String productId, @RequestParam("id") String id, @RequestParam("md5") String md5) {
        try {
            killBuzService.executeKill(productId, id, md5);
        }catch (Exception e){
            e.printStackTrace();
            return "秒杀失败";
        }
        return "正在排队";
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
