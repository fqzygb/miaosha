package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDoMapper;
import com.miaoshaproject.dao.UserpasswordDoMapper;
import com.miaoshaproject.dataobject.UserDo;
import com.miaoshaproject.dataobject.UserpasswordDo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EnBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.impl.UserModel.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.DuplicateFormatFlagsException;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
   private UserDoMapper userDoMapper;
    @Autowired
    private UserpasswordDoMapper userpasswordDoMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userDoMapper获取对应用户的dataobject
       UserDo userDo =  userDoMapper.selectByPrimaryKey(id);
       if(userDo == null){
           return null;
       }
       //通过用户id获取对应的用户加密的密码信息
      UserpasswordDo userpasswordDo =  userpasswordDoMapper.selectByUserId(userDo.getId());
       return convertFromDataObject(userDo,userpasswordDo);

    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EnBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//

        ValidationResult validationResult = validator.validator(userModel);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EnBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }

        //实现model ->dataobject方法
        UserDo userDo = convertFromModel(userModel);
        try {
            userDoMapper.insertSelective(userDo);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EnBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已经重复注册");
        }

        userModel.setId(userDo.getId());  //把自动生成的id复制到usermodel，然后插入到密码表，让他和用户表的id保持一致（密码表的userid）
        UserpasswordDo userpasswordDo = convertPasswordFromModel(userModel);
        userpasswordDoMapper.insertSelective(userpasswordDo);
        return;
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户手机号获取用户信息
        UserDo userDo = userDoMapper.selectByTelphone(telphone);
        if(userDo == null){
            throw new BusinessException(EnBusinessError.USER_LOGIN_FAIL);
        }
        UserpasswordDo userpasswordDo = userpasswordDoMapper.selectByUserId(userDo.getId());
        UserModel userModel = convertFromDataObject(userDo,userpasswordDo);

        //比对用户信息内部加密的密码是否和传输进来的密码相匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EnBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;

    }

    private UserpasswordDo convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserpasswordDo userpasswordDo = new UserpasswordDo();
        userpasswordDo.setEncrptPassword(userModel.getEncrptPassword());
        userpasswordDo.setUserId(userModel.getId());
        return userpasswordDo;

    }

    private UserDo convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userModel,userDo);
        return userDo;
    }

    private UserModel convertFromDataObject(UserDo userDo , UserpasswordDo userpasswordDo){
        if(userDo == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo ,userModel);
        if(userpasswordDo != null) {
            userModel.setEncrptPassword(userpasswordDo.getEncrptPassword());
        }
        return userModel;
    }
}
