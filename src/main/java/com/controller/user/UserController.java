package com.controller.user;

import com.constant.Constant;
import com.dao.SysUserDao;
import com.entity.SysUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/3/19
 */
@Controller
@RequestMapping(value = "/pages/user")
public class UserController  {
    @Autowired
    private SysUserDao sysUserDao;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> userLogin(HttpServletRequest request, HttpServletResponse response){

        Map<String,Object> resultMap = new HashMap<>();
        try{
            String userName = request.getParameter("username");
            String password = request.getParameter("password");
            Optional<SysUser> sysUser = sysUserDao.findByUserNameAndFlag(userName,1);
            if(sysUser.isPresent()){
                if(!DigestUtils.sha1Hex((password + Constant.SA).getBytes()).equals(sysUser.get().getPassword())){
                    throw new Exception("密码不正确");
                }else{
                    request.getSession().setAttribute("user",sysUser.get());
                    Cookie cookie = new Cookie("key",sysUser.get().getId());
                    cookie.setMaxAge(24*60*60);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }else{
                throw new Exception("没有此用户");
            }
            resultMap.put("success",true);
            resultMap.put("message","ok");
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
        }
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Headers","X-Requested-With,Content-Type,X-Cherish-Request");
        return resultMap;
    }


    /**
     * login success,get userInfo to display on html pages
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getUserInfo(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            SysUser sysUser = (SysUser) request.getSession().getAttribute("user");
            if(null == sysUser){
                throw new Exception("用户登录信息不存在");
            }
            resultMap.put("success",true);
            resultMap.put("message","ok");
            resultMap.put("rows",sysUser);
        }catch (Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/loginOut",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> loginOut(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            request.getSession().invalidate();
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies){
                if("key".equals(cookie.getName())){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
            resultMap.put("success",true);
            resultMap.put("message","ok");
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
        }
        return resultMap;
    }
}
