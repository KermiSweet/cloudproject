package com.kermi.shoppingcar.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingcarGetPrice {
    private List<ShoppingcarRedisItemUtil> carsprice;
}
