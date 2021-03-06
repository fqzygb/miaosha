package com.miaoshaproject.controller;

import com.miaoshaproject.controller.baseController.baseController;
import com.miaoshaproject.controller.viewobject.ItemVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class ItemController extends baseController {
    @Autowired
    private ItemService itemService;

    //创建商品的CONTROLLER
    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FROMED})
    @ResponseBody
    public CommonReturnType  createItem(@RequestParam(name = "title") String title,
                                        @RequestParam(name = "description") String description,
                                        @RequestParam(name = "price") BigDecimal price,
                                        @RequestParam(name = "stock") Integer stock,
                                        @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        //封装service 来请求创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemVO itemVO = convertVOFromModel(itemModelForReturn);

        return CommonReturnType.create(itemVO);

    }

    //商品详情页浏览
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }






    private ItemVO convertVOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return  null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;

    }
}
