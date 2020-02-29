package com.kermi.shoppingcar.controller;

import com.kermi.shoppingcar.service.ShoppingcarService;
import com.kermi.shoppingcar.utils.ShoppingcarGetPrice;
import com.kermi.shoppingcar.utils.ShoppingcarRedisItemUtil;
import entity.ResResult;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/car")
public class ShoppingcarController {

    @Autowired
    private ShoppingcarService carservice;

    @RequestMapping(value = "/add/{id}/{nums}", method = RequestMethod.POST)
    public ResResult addShoppingcar(@PathVariable("id") Long id,
                                    @PathVariable("nums") int nums,
                                    HttpServletRequest req){
        if (carservice.caroperation(id, nums, getSessionId(req), 1)) {
            return new ResResult(true, StatusCode.OK, "添加成功");
        }
        return new ResResult(false, StatusCode.ERROR, "添加失败");
    }

    @RequestMapping(value = "/del/{id}/{nums}", method = RequestMethod.POST)
    public ResResult delShoppingcar(@PathVariable("id") Long id,
                                    @PathVariable("nums") int nums,
                                    HttpServletRequest req) {
        if (carservice.caroperation(id, nums, getSessionId(req), 0)) {
            return new ResResult(true, StatusCode.OK, "删除成功");
        }
        return new ResResult(false, StatusCode.ERROR, "删除失败");
    }

    @RequestMapping(value = "/getcars")
    public ResResult getcars(HttpServletRequest request) {
        List<ShoppingcarRedisItemUtil> data = carservice.getcars(getSessionId(request));
        return new ResResult(true, StatusCode.OK, "", data);
    }

    @RequestMapping(value = "/cleanall")
    public ResResult clearall(HttpServletRequest request) {
        carservice.clearall(getSessionId(request));
        return new ResResult(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value = "/getprice")
    public ResResult getprice(@RequestBody ShoppingcarGetPrice goodslist) {
        double price = carservice.getprice(goodslist);
        return new ResResult(true, StatusCode.OK, "", price);
    }

    public String getSessionId(HttpServletRequest req) {
        return req.getSession().getId();
    }
}
