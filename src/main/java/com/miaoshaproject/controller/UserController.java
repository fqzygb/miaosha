package com.miaoshaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.controller.baseController.baseController;
import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EnBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.impl.UserModel.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class UserController extends baseController {
    @Autowired
    private  UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FROMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam("telphone")String telphone,
                                     @RequestParam("password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EnBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务，用来校验登录用户是否合法
      UserModel userModel =  userService.validateLogin(telphone,this.EncodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);


        return CommonReturnType.create(null);

    }



    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FROMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam("telphone")String telphone,
                                     @RequestParam("name")String name,
                                     @RequestParam("otpCode")String otpCode,
                                     @RequestParam("gender")Integer gender,
                                     @RequestParam("age")Integer age,
                                     @RequestParam("password")String password ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //验证手机号和对应的otpcode相符合
       String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
       if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
           throw new BusinessException(EnBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
       }
        //用户的注册流程
        UserModel userModel = new UserModel();
       userModel.setName(name);
       userModel.setAge(age);
       userModel.setGender(new Byte(String.valueOf(gender.intValue())));
       userModel.setTelphone(telphone);
       userModel.setRegisterMode("byphone");
       userModel.setEncrptPassword(this.EncodeByMd5(password));

       userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 =MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;

    }



    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FROMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam("telphone")String telphone){
        //需要按照一定规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);


        //将OTP验证码于对用手机号码关联，使用httpsession的方式绑定他的手机号与OTPCODE
        httpServletRequest.getSession().setAttribute(telphone,otpCode);



        //将OTP验证码通过短信通道发送给用户（本项目省略）
        System.out.println("telphone ="+telphone+" & otpCode ="+otpCode);   //实际情况不允许打印验证码，用户隐私，这里是方便调试

        return CommonReturnType.create(null);


    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service获取对应id的用户对象并返回给前端
      UserModel userModel =   userService.getUserById(id);

      //若获取的对应用户信息不存在
        if(userModel == null){
            //userModel.setEncrptPassword("123");
            throw new BusinessException(EnBusinessError.USER_NOT_EXIST);
        }

      //将核心领域模型用户对象转化为可供UI使用的viewobject
      UserVO userVO = convertFromModel(userModel);

      //返回通用对象
      return CommonReturnType.create(userVO);

    }
    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }




}
