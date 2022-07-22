package com.cennavi.core.common;

import org.apache.commons.beanutils.BeanUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * controller 工具类
 */
public class ResponseUtils {
    public static final String success_message = "操作成功";
    public static final String error_message = "操作失败";

    /**
     * 返回默认正常 格式数据  例如 void 方法
     *
     * @return
     */
    public static ResultObj success() {
        ResultObj result = new ResultObj();
        result.setCode(ResultObj.SUCCESS);
        result.setMessage(success_message);
        return result;
    }

    /**
     * 返回带数据的正常操作
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> ResultObj success(T object) {
        ResultObj result = new ResultObj();
        result.setCode(ResultObj.SUCCESS);
        result.setMessage(success_message);
        result.setData(object);
        return result;
    }

    /**
     * 返回错误信息
     *
     * @param error
     * @return
     */
    public static ResultObj error(String error) {
        ResultObj result = new ResultObj();
        result.setCode(ResultObj.FAUlT);
        result.setMessage(error);
        return result;
    }

    /**
     * 自定义错误
     *
     * @param code
     * @param message
     * @return
     */
    public static ResultObj error(Integer code, String message) {
        ResultObj result = new ResultObj();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 把 jdbcTemplate 生成的 List<Map<String, Object>> 数据转化为 List<bean>
     *
     * @param map
     * @param T
     * @param <T>
     * @return
     */
    public static <T> List<T> toBean(List<Map<String, Object>> map, Class<T> T) {
        List<T> result = null;
        if (map == null) {
            return result;
        } else {
            result = new ArrayList<>();
            for (int i = 0; i < map.size(); i++) {
                result.add(toBean(map.get(i), T));
            }
        }
        return result;
    }

    /**
     * 把 jdbcTemplate 生成的 Map<String, Object> 数据转化为bean
     *
     * @param map
     * @param class1
     * @param <T>
     * @return
     */
    public static <T> T toBean(Map<String, Object> map, Class<T> class1) {
        T bean = null;
        try {
            bean = class1.newInstance();
            BeanUtils.populate(bean, map);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 获取 用户信息 session 模式
     *
     * @param key
     * @return
     */
    public static Map<String, String> getUserBySeesion(String key) {
        return null;
    }

    /**
     * 获取 用户信息  token
     *
     * @param token
     * @return
     */
    public static Map<String, String> getUserByToken(String token) {
        return null;
    }


    /**
     * 检查像素(低于某像素)
     * @return
     * @throws Exception
     */
    public Boolean checkImgPixel(BufferedImage bi, String width, String height) throws Exception{
        int wid = bi.getWidth(); // 像素
        int heig = bi.getHeight(); // 像素
        Integer img_width = Integer.valueOf(width);
        Integer img_height = Integer.valueOf(height);
        if (img_height<0) {
            return wid>img_width  ;
        }else{
            return wid>img_width || heig>img_height ;
        }
    }
    /**
     * 检查像素(等于某像素)
     * @return
     * @throws Exception
     */
    public Boolean checkImgPixelFinal(BufferedImage bi,String width,String height) throws Exception{
        int wid = bi.getWidth(); // 像素
        int heig = bi.getHeight(); // 像素

        System.out.println(wid+"*"+heig);
        Integer img_width = Integer.valueOf(width);
        Integer img_height = Integer.valueOf(height);
        System.out.println(wid==img_width && heig==img_height);
        return wid==img_width && heig==img_height ;
    }

    /**
     * 检查图片大小
     * @param fileup
     * @param size
     * @return
     * @throws Exception
     */
    public boolean checkImgSize(File fileup, String size) throws Exception{
        Integer img_size = Integer.valueOf(size);
        long length = fileup.length();
        return length >= img_size ;
    }
}
