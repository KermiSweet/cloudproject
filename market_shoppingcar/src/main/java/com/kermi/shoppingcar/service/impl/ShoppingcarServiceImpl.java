package com.kermi.shoppingcar.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kermi.common.mapper.GoodsMapper;
import com.kermi.common.mapper.ShoppingCarMapper;
import com.kermi.common.pojo.Goods;
import com.kermi.common.pojo.ShoppingCar;
import com.kermi.common.pojo.User;
import com.kermi.shoppingcar.service.ShoppingcarService;
import com.kermi.shoppingcar.utils.ShoppingcarRedisItemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;
import util.RedisUtil;

import java.util.ArrayList;
import java.util.List;


@Service
public class ShoppingcarServiceImpl implements ShoppingcarService {

    private Logger logger = LoggerFactory.getLogger(ShoppingcarService.class);

    @Autowired
    private ShoppingCarMapper carMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisUtil redisUtil;

    private final String REDIS_SESSION_PREFIX = "SESSION_";
    private final String REDIS_USER_HASH = "USER_HASH";
    private final String REDIS_SHOPPINGCAR_HASH = "SHOPPINGCAR_HASH";

    private final String REDIS_GET_INFO = "Redis取值:";
    private final String REDIS_SET_INFO = "Redis存值";
    private final String REDIS_GET_SUCCESS_INFO = "Redis取到值:";

    @Override
    public boolean caroperation(Long id, int nums, String sessionid, int code) {
        //先查询用户是否为登录状态
        User user = null;
        String thismethodName = new Exception().getStackTrace()[0].getMethodName();
        String sessionredis = redisUtil.get(REDIS_SESSION_PREFIX + sessionid);
        if (sessionid == null) {
            //用户没有登录，取值失败
            return false;
        } else {
            logger.info(thismethodName + REDIS_GET_SUCCESS_INFO + REDIS_SESSION_PREFIX + sessionid);
        }
        Long userid = JSONObject.parseObject(sessionredis, User.class).getId();

        //先查询数据库中是否存在该用户的购物车
        String carinredis = (String) redisUtil.hGet(REDIS_SHOPPINGCAR_HASH, String.valueOf(userid));
        logger.info(thismethodName + REDIS_GET_INFO + REDIS_SHOPPINGCAR_HASH + "-" + userid);
        List<ShoppingcarRedisItemUtil> cars = null;
        if (carinredis == null) {
            //Redis中不存在查询主数据库
            List<ShoppingCar> dbcar = carMapper.selectByUserId(userid);
            cars = new ArrayList<>();
            for (ShoppingCar car : dbcar) {
                Long goodsid = car.getGoodstableId();
                Goods goods = goodsMapper.selectByPrimaryKey(goodsid);
                int num = car.getShoppingnum();
                cars.add(new ShoppingcarRedisItemUtil(goods, num));
            }

            //存入Redis数据库
            redisUtil.hPut(REDIS_SHOPPINGCAR_HASH, sessionid, JSONObject.toJSONString(cars));
            logger.info(thismethodName + REDIS_SET_INFO + REDIS_SHOPPINGCAR_HASH + sessionid);
        } else {
            //Redis中不为null，取出数据，添加数据后重新写入
            logger.info(thismethodName + REDIS_GET_SUCCESS_INFO + REDIS_SHOPPINGCAR_HASH + "-" + userid);
            cars = JSONObject.parseArray(carinredis, ShoppingcarRedisItemUtil.class);
        }

        Goods addGoods = goodsMapper.selectByPrimaryKey(id);
        if (addGoods == null) {
            //添加的商品为空
            return false;
        }

        //先存主数据库
        if (code == 1) {
            //增加
            carMapper.insertSelective(new ShoppingCar(idWorker.nextId(), userid, addGoods.getId(), nums));
        } else if (code == 0) {
            //删除
            carMapper.deleteByPrimaryKey(addGoods.getId());
        }


        List<ShoppingcarRedisItemUtil> rediscar = new ArrayList<>();

        if (code == 1) {
            //增添
            boolean flag = false;
            for (ShoppingcarRedisItemUtil item : cars) {
                if (id.equals(item.getGoods().getId())) {
                    item.setNums(item.getNums() + nums);
                    flag = true;
                }
                rediscar.add(item);
            }

            if (!flag) {
                rediscar.add(new ShoppingcarRedisItemUtil(addGoods, nums));
            }
        } else if (code == 0) {
            //删除
            for (ShoppingcarRedisItemUtil item : cars) {
                if (id.equals(item.getGoods().getId())) {
                    if (nums >= item.getNums()) {
                        //要删除的数量大于了剩余数量
                        return false;
                    }
                    item.setNums(item.getNums() - nums);
                }
                rediscar.add(item);
            }
        }

        //存Redis数据库
        redisUtil.hPut(REDIS_SHOPPINGCAR_HASH, sessionid, JSONObject.toJSONString(rediscar));
        logger.info(thismethodName + REDIS_SET_INFO + REDIS_SHOPPINGCAR_HASH + sessionid);

        return true;
    }


}
