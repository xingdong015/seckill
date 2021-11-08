package com.system.design.seckil.business.controller;

import com.system.design.seckil.business.service.KillBuzService;
import com.system.design.seckill.product.common.bean.ApiErrorCodeEnum;
import com.system.design.seckill.product.common.bean.Resp;
import com.system.design.seckill.product.common.enums.SeckillStatusEnum;
import com.system.design.seckill.product.common.exception.SeckillException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author chengzhengzheng
 * @date 2021/10/30
 */
@RestController
@RequestMapping("/business")
public class IndexController {
    @Resource
    private KillBuzService killBuzService;

    @RequestMapping("/kill")
    @ResponseBody
    public Resp<String> kill(HttpServletRequest request) {
        String killId = request.getParameter("killId");
        String userId = request.getParameter("userId");
        String md5    = request.getParameter("md5");
        try {
            killBuzService.executeKill(killId, userId, md5);
        } catch (SeckillException e) {
            SeckillStatusEnum statusEnum = e.getStatusEnum();
            return new Resp<String>().failed(statusEnum.getState(),statusEnum.getInfo());
        } catch (Exception e) {
            return new Resp<String>().failed(ApiErrorCodeEnum.FAIL);
        }
        return new Resp<String>().success(killId);
    }
}
