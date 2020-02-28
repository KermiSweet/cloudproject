package com.kermi.shoppingcar.utils;

import com.kermi.common.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingcarRedisItemUtil {
    /*商品类*/
    private Goods goods;
    /*数目*/
    private int nums;
}
