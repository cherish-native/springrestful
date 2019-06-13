package com.controller;

import com.entity.SysUser;
import com.entity.vo.Pagination;
import com.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    /**
     * 获取分页信息
     * @return
     */
    public Pageable getPagination(HttpServletRequest request){
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("rows");
        int page = 0;
        int size = 0;
        if(StringUtils.isEmpty(pageStr) || StringUtils.isEmpty(sizeStr)){
            return null;
        }
        page = Integer.parseInt(pageStr) - 1;
        size = Integer.parseInt(sizeStr);
        return new PageRequest(page, size);
    }

    /**
     * 获取登录用户信息
     * @param request
     * @return
     */
    public SysUser findLoginUser(HttpServletRequest request){
        return ((SysUser)request.getSession().getAttribute("user"));
    }
}
