package com.li.controller;

import com.li.domain.Admin;
import com.li.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    @PostMapping("/login.action")
    public String getByExample(String name, String pwd, HttpSession session, HttpServletRequest request) {
        //判断是否该用户是否存在
        Admin admin = adminService.login(name, pwd);

        //用户存在则把用户数据放入session域，否则报错
        if (admin != null) {
            session.setAttribute("admin",admin);
            return "redirect:/admin/main.jsp";
        } else {
            request.setAttribute("errmsg","账号不存在或密码错误！！");
        }
        return "admin/login";
    }
}
