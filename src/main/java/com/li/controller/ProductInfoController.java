package com.li.controller;

import com.github.pagehelper.PageInfo;
import com.li.domain.ProductInfo;
import com.li.domain.vo.ProductInfoVo;
import com.li.service.ProductInfoService;
import com.li.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoController {
    private static final int PAGE_SIZE = 5;

    String saveName = "";

    @Resource
    ProductInfoService productInfoService;

     /*
        展示第一页的数据
     */
    @RequestMapping("/split.action")
    public ModelAndView getPage(HttpServletRequest request) {
        PageInfo pageInfo = null;
        //拿出session域中的数据
        ProductInfoVo vo = (ProductInfoVo) request.getSession().getAttribute("updateVo");

        if(vo != null) {
            pageInfo = productInfoService.splitPageVo(vo, PAGE_SIZE);
            //清空session
            request.getSession().removeAttribute("updateVo");
        } else {
            pageInfo = productInfoService.splitPage(1, PAGE_SIZE);
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("info",pageInfo);
        mv.setViewName("admin/product");

        return mv;
    }

    /*
        删除后的分页
    */
    @RequestMapping(value = "/deleteAjaxSplit.action", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteAjaxSplit(HttpServletRequest request) {
        PageInfo pageInfo = null;
        //拿出session域中的数据
        ProductInfoVo vo = (ProductInfoVo) request.getSession().getAttribute("deleteVo");

        if(vo != null) {
            pageInfo = productInfoService.splitPageVo(vo, PAGE_SIZE);
            //清空session
            request.getSession().removeAttribute("deleteVo");
        } else {
            pageInfo = productInfoService.splitPage(1, PAGE_SIZE);
        }

        request.getSession().setAttribute("info",pageInfo);
        return (String) request.getAttribute("msg");
    }

    /*
        取消后，返回原来的的界面
    */
    @RequestMapping("/recover.action")
    public ModelAndView recover(int page) {
        //取出当前页的数据
        PageInfo pageInfo = productInfoService.splitPage(page, PAGE_SIZE);;

        ModelAndView mv = new ModelAndView();
        mv.addObject("info",pageInfo);
        mv.setViewName("admin/product");

        return mv;
    }

    /*
        展示某页的数据
     */
    @PostMapping("/ajaxsplit.action")
    @ResponseBody
    public void splitPage(ProductInfoVo vo, HttpSession session) {
        PageInfo info = productInfoService.splitPageVo(vo,PAGE_SIZE);
        session.setAttribute("info",info);
    }

    /*
        图片上传
    */
    @RequestMapping("/ajaxImg.action")
    @ResponseBody
    public String uploadFile(MultipartFile pimage, HttpServletRequest request) {
        //获取图片原来的名称
        String oldName = pimage.getOriginalFilename();
        //获取新的存储名字
        saveName = FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(oldName);
        //获取文件存储的位置
        String realPath = request.getServletContext().getRealPath("/image_big");

        //判断文件是否存在
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdir();
        }
        //把照片存入指定位置
        try {
            pimage.transferTo(new File(realPath+File.separator+saveName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回客户端json对象,封装图片路径，实现图片回显效果
        JSONObject object = new JSONObject();
        object.put("imgurl",saveName);

        return object.toString();
    }

    /*
        新增商品
    */
    @RequestMapping("/save.action")
    public String addProductInfo(ProductInfo productInfo, HttpServletRequest request) {
        //保存图片的名称到数据库
        productInfo.setpImage(saveName);
        //保存添加商品的时间
        productInfo.setpDate(new Date());

        //添加商品
        int num = productInfoService.save(productInfo);
        //判断是否添加成功
        if(num > 0) {
            request.setAttribute("msg","商品添加成功！");
        } else {
            request.setAttribute("msg","商品添加失败！");
        }
        //清空saveName，为修改提供判断依据
        saveName = "";
        //添加完成后，跳转到商品列表页
        return "forward:/prod/split.action";
    }

    /*
        回显商品信息
    */
    @RequestMapping("/one.action")
    public String one(Integer pid, ProductInfoVo vo, HttpServletRequest request) {
        //通过id查询商品信息
        ProductInfo info = productInfoService.getByID(pid);
        //把商品信息和当前页面page放入request域中
        request.setAttribute("prod",info);
        //把vo中的数据存入session域
        request.getSession().setAttribute("updateVo",vo);
        return "admin/update";
    }

    /*
        修改商品信息
    */
    @RequestMapping("/update.action")
    public String update(ProductInfo info, HttpServletRequest request) {
        info.setpDate(new Date());
        //判断是否上传图片
        if(!saveName.equals("")) {
            info.setpImage(saveName);
        }
        //修改商品信息
        int num = productInfoService.update(info);

        if (num > 0) {
            request.setAttribute("msg","修改成功！");
        } else {
            request.setAttribute("msg","修改失败！");
        }

        //清空saveName
        saveName = "";
        return "forward:/prod/split.action";
    }

    /*
        删除指定的商品信息
    */
    @RequestMapping(value = "/delete.action")
    public String delete(Integer pid, ProductInfoVo vo, HttpServletRequest request) {
        //通过id查询商品信息
        int num = productInfoService.delete(pid);

        if(num > 0) {
            request.setAttribute("msg","删除成功！");
            //往session里放入数据
            request.getSession().setAttribute("deleteVo",vo);
        } else {
            request.setAttribute("msg","删除失败！");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }

    /*
        批量删除商品信息
    */
    @RequestMapping(value = "/deletebatch.action")
    public String deleteBatch(String pids, HttpServletRequest request) {
        //通过id查询商品信息
        String[] ids = pids.split(",");
        //执行批量删除操作
        int num = productInfoService.deleteBatch(ids);

        if(num > 0) {
            request.setAttribute("msg","删除成功！");
        } else {
            request.setAttribute("msg","删除失败！");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }

    /*
        根据条件查询商品信息
    */
    @RequestMapping(value = "/selectCondition.action")
    @ResponseBody
    public void selectCondition(ProductInfoVo productInfoVo, HttpServletRequest request) {
        //执行条件查询操作
        List<ProductInfo> productInfos = productInfoService.selectCondition(productInfoVo);

        request.getSession().setAttribute("list",productInfos);

    }
}
