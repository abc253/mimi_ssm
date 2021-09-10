package com.li.listener;

import com.li.domain.ProductType;
import com.li.service.ProductTypeService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

public class SysListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("------>初始化数据字典！！");
        //获取servletContext和WebApplicationContext
        ServletContext application = event.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(application);
        //获取IOC容器中的ProductTypeService
        ProductTypeService typeService = webApplicationContext.getBean(ProductTypeService.class);
        //获取所有的Product类型
        List<ProductType> types = typeService.getProductTypes();
        //把list放入ServletContext作用域
        application.setAttribute("ptlist",types);

        System.out.println("----------->数据字典初始化结束！！");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
