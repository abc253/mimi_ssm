package com.li.service;

import com.li.domain.Admin;

public interface AdminService {
    Admin login(String username, String password);
}
