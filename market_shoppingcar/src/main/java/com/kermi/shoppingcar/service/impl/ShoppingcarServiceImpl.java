package com.kermi.shoppingcar.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kermi.common.mapper.GoodsMapper;
import com.kermi.common.mapper.ShoppingCarMapper;
import com.kermi.common.pojo.Goods;
import com.kermi.common.pojo.ShoppingCar;
import com.kermi.common.pojo.User;
import com.kermi.shoppingcar.service.ShoppingcarService;
import com.kermi.shoppingcar.utils.ShoppingcarGetPrice;
import com.kermi.shoppingcar.utils.ShoppingcarRedisItemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;
import util.RedisUtil;
import util.SessionAttributes;

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
    private final String REDIS_GOODS_HASH = "GOODS_HASH";

    private final String REDIS_GET_INFO = "Redis取值:";
    private final String REDIS_SET_INFO = "Redis存值";
    private final String REDIS_DEL_INFO = "Redis移除值:";
    private final String REDIS_GET_SUCCESS_INFO = "Redis取到值:";

    @Override
    public boolean caroperation(Long id, int nums, String sessionid, int code) {
        //先查询用户是否为登录状态
        String thismethodName = new Exception().getStackTrace()[0].getMethodName();
        String sessionredis = redisUtil.get(REDIS_SESSION_PREFIX + sessionid);
        if (sessionredis == null) {
            //用户没有登录，取值失败
            throw new RuntimeException("请先登录");
        } else {
            logger.info(thismethodName + REDIS_GET_SUCCESS_INFO + REDIS_SESSION_PREFIX + sessionid);
        }
        Long userid = JSONObject.parseObject(String.valueOf(JSONObject.parseObject(sessionredis, SessionAttributes.class).getO()), User.class).getId();

        //先查询缓存中是否存在该用户的购物车
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


        ShoppingCar tofinddb = new ShoppingCar();
        tofinddb.setGoodstableId(addGoods.getId());
        tofinddb.setUsertableId(userid);
        tofinddb = carMapper.selectSelective(tofinddb);
        //先存主数据库
        if (code == 1) {
            //增加
            if (tofinddb == null) {
                carMapper.insertSelective(new ShoppingCar(idWorker.nextId(), userid, addGoods.getId(), nums));
            } else {
                tofinddb.setShoppingnum(tofinddb.getShoppingnum() + nums);
                carMapper.updateByPrimaryKey(tofinddb);
            }
        } else if (code == 0) {
            //删除
            if (tofinddb == null || tofinddb.getShoppingnum() < nums) {
                return false;
            }
            if (tofinddb.getShoppingnum() == nums) {
                carMapper.deleteByGoodsIdAndUserId(userid, addGoods.getId());
            } else {
                tofinddb.setShoppingnum(tofinddb.getShoppingnum() - nums);
                carMapper.updateByPrimaryKey(tofinddb);
            }
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
                    if (nums > item.getNums()) {
                        //要删除的数量大于了剩余数量
                        return false;
                    }
                    if (nums == item.getNums()) {
                        //如果刚好删除完，则不加入列表
                        continue;
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

    @Override
    public List<ShoppingcarRedisItemUtil> getcars(String sessionid) {
        //先看Redis中是否有数据
        //判断用户是否登录
        String thismethodName = new Exception().getStackTrace()[0].getMethodName();
        String sessionredis = redisUtil.get(REDIS_SESSION_PREFIX + sessionid);
        if (sessionredis == null) {
            //用户没有登录，取值失败
            throw new RuntimeException("请先登录");
        } else {
            logger.info(thismethodName + REDIS_GET_SUCCESS_INFO + REDIS_SESSION_PREFIX + sessionid);
        }
        Long userid = JSONObject.parseObject(String.valueOf(JSONObject.parseObject(sessionredis, SessionAttributes.class).getO()), User.class).getId();

        //先查询缓存中是否存在该用户的购物车
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
            //Redis中不为null，取出数据
            logger.info(thismethodName + REDIS_GET_SUCCESS_INFO + REDIS_SHOPPINGCAR_HASH + "-" + userid);
            cars = JSONObject.parseArray(carinredis, ShoppingcarRedisItemUtil.class);
        }

        return cars;
    }

    @Override
    public boolean clearall(String sessionid) {
        String thismethodName = new Exception().getStackTrace()[0].getMethodName();
        String sessionredis = redisUtil.get(REDIS_SESSION_PREFIX + sessionid);
        if (sessionredis == null) {
            //用户没有登录，取值失败
            throw new RuntimeException("请先登录");
        } else {
            logger.info(thismethodName + REDIS_GET_SUCCESS_INFO + REDIS_SESSION_PREFIX + sessionid);
        }
        Long userid = JSONObject.parseObject(String.valueOf(JSONObject.parseObject(sessionredis, SessionAttributes.class).getO()), User.class).getId();

        //先查询缓存中是否存在该用户的购物车
        String carinredis = (String) redisUtil.hGet(REDIS_SHOPPINGCAR_HASH, String.valueOf(userid));
        logger.info(thismethodName + REDIS_GET_INFO + REDIS_SHOPPINGCAR_HASH + "-" + userid);
        List<ShoppingcarRedisItemUtil> cars = null;
        if (carinredis != null) {
            //Redis中不为null，取出数据
            logger.info(thismethodName + REDIS_GET_SUCCESS_INFO + REDIS_SHOPPINGCAR_HASH + "-" + userid);
            redisUtil.hDelete(REDIS_SHOPPINGCAR_HASH, userid);
            logger.info(thismethodName + REDIS_DEL_INFO + REDIS_SHOPPINGCAR_HASH + "-" + userid);
        }
        //从数据库中删除用户对应的购物车数据
        carMapper.deleteByPrimaryKey(userid);
        return true;
    }

    @Override
    public double getprice(ShoppingcarGetPrice goodslist) {
        double price = 0;
        for (ShoppingcarRedisItemUtil item : goodslist.getCarsprice()) {
            //先查询Redis中是否有该商品的信息
            Long goodid = item.getGoods().getId();
            String json = (String) redisUtil.hGet(REDIS_GOODS_HASH, String.valueOf(goodid));
            Goods good = null;
            if (json != null) {
                good = JSONObject.parseObject(json, Goods.class);
            } else {
                good = goodsMapper.selectByPrimaryKey(goodid);
                if (good == null) {
                    throw new RuntimeException("商品不存在");
                }
            }
            price += good.getGoprice();
        }

        return price;
    }

}
