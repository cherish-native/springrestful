package com.controller.user;

import com.constant.Constant;
import com.dao.SysUserDao;
import com.entity.SysUser;
import com.entity.vo.DataGridReturn;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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

    @RequestMapping(value = "/checkUserName",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> checkUserName(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            String userName = request.getParameter("username");
            Optional<SysUser> sysUser = sysUserDao.findByUserNameAndFlag(userName,1);
            if(sysUser.isPresent()){
                resultMap.put("success",false);
                resultMap.put("message","用户名已存在");
            }else {
                resultMap.put("success", true);
                resultMap.put("message", "用户名可使用");
            }
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
            ex.printStackTrace();
        }
        return resultMap;
    }

    @RequestMapping(value = "/saveUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saveUser(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            SysUser sysUser = new SysUser();
            sysUser.setUserName(request.getParameter("username"));
            sysUser.setPassword(DigestUtils.sha1Hex((request.getParameter("password") + Constant.SA).getBytes()));
            sysUser.setTrueName(request.getParameter("name"));
            sysUser.setIdCard(request.getParameter("idCard"));
            sysUser.setPoliceId(request.getParameter("policeId"));
            sysUser.setUnitCode(request.getParameter("unitcode"));
            sysUser.setSalt(request.getParameter("password"));
            sysUser.setRole(Integer.valueOf(request.getParameter("role")));
            sysUser.setFlag(1);
            sysUserDao.save(sysUser);
            resultMap.put("success",true);
            resultMap.put("message","保存成功");
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
            ex.printStackTrace();
        }
        return resultMap;
    }

    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public DataGridReturn getUserList(){
        List<SysUser> sysUserList = sysUserDao.findByFlag(1);
        return new DataGridReturn(sysUserList.size(), sysUserList);
    }

    @RequestMapping(value = "/getUser",method = RequestMethod.GET)
    @ResponseBody
    public SysUser getUser(HttpServletRequest request){
        Optional<SysUser> sysUser = sysUserDao.findByUserNameAndFlag(request.getParameter("username"),1);
        return sysUser.get();
    }

    @RequestMapping(value = "/deleteUser",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteUser(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            String id = request.getParameter("id");
            sysUserDao.deleteById(id);
            resultMap.put("success",true);
            resultMap.put("message","删除成功");
        }catch(Exception ex){
            resultMap.put("success",false);
            resultMap.put("message",ex.getMessage());
            ex.printStackTrace();
        }
        return resultMap;
    }

}
