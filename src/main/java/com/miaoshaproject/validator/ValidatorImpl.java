package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;

    //实现检验方法并返回检验结果
    public ValidationResult validator(Object bean){
        ValidationResult validationResult = new ValidationResult();
         Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
         //有错误
         if(constraintViolationSet.size()>0){
             validationResult.setHasErrors(true);
             constraintViolationSet.forEach(constraintViolation -> {
                 String errMsg = constraintViolation.getMessage();
                 String propertyName = constraintViolation.getPropertyPath().toString();
                 validationResult.getErrorMsgMap().put(propertyName,errMsg);

             });
         }
         return validationResult;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //将hlibernate validator通过工厂的初始化方式使其实例化
        this.validator= Validation.buildDefaultValidatorFactory().getValidator();


    }
}
