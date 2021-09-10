package com.li.service.impl;

import com.li.domain.Admin;
import com.li.domain.AdminExample;
import com.li.mapper.AdminMapper;
import com.li.service.AdminService;
import com.li.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {
        AdminExample example = new AdminExample();
        example.createCriteria().andANameEqualTo(name);

        List<Admin> admins = adminMapper.selectByExample(example);

        if(admins.size() > 0) {
            Admin admin = admins.get(0);
            pwd = MD5Util.getMD5(pwd);

            if (pwd.equals(admin.getaPass())) {
                return admin;
            }
        }
        return null;
    }
}
