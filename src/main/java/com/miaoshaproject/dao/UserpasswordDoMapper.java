package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.UserpasswordDo;

public interface UserpasswordDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int insert(UserpasswordDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int insertSelective(UserpasswordDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    UserpasswordDo selectByPrimaryKey(Integer id);
    UserpasswordDo selectByUserId(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int updateByPrimaryKeySelective(UserpasswordDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat May 09 10:45:35 CST 2020
     */
    int updateByPrimaryKey(UserpasswordDo record);
}