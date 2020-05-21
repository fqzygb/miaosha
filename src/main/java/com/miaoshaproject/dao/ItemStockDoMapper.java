package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.ItemStockDo;

public interface ItemStockDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int insert(ItemStockDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int insertSelective(ItemStockDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    ItemStockDo selectByPrimaryKey(Integer id);
    ItemStockDo selectByItemID(Integer itemid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int updateByPrimaryKeySelective(ItemStockDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int updateByPrimaryKey(ItemStockDo record);
}