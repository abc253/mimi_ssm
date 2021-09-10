package myTest;

import com.li.domain.Admin;
import com.li.domain.AdminExample;
import com.li.domain.ProductInfo;
import com.li.domain.vo.ProductInfoVo;
import com.li.mapper.AdminMapper;
import com.li.mapper.ProductInfoMapper;
import com.li.utils.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:applicationContext.xml")
public class AdminTest {
    @Resource
    private ProductInfoMapper productInfoMapper;

    private AdminMapper adminMapper;
    @Test
    public void getByExample() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        AdminExample example = new AdminExample();

        String name = "admin";

        String s = MD5Util.getMD5("000000");
        System.out.println(s);
        example.createCriteria().andANameEqualTo(name);

        adminMapper = (AdminMapper) context.getBean("adminMapper");
        System.out.println(adminMapper);
        List<Admin> admins = this.adminMapper.selectByExample(example);
    }

    @Test
    public void selectCondition() {
        ProductInfoVo vo = new ProductInfoVo();
        vo.setPname("4");
        vo.setLprice(1000.0);
        vo.setHprice(3000.0);
        List<ProductInfo> productInfos = productInfoMapper.selectCondition(vo);
        productInfos.forEach(productInfo -> System.out.println(productInfo));
    }

    @Test
    public void look() {
        int[] a = {1,2,3,4,5};
        long start =  System.currentTimeMillis();
        boolean flag = lookarray(a);
        long end = System.currentTimeMillis();
        System.out.println(start +"-----"+ end);
        System.out.println(flag);
    }

    public static boolean lookarray(int[] array) {
        boolean flag = false;
        int count = 0;
        int num = 0;
        for(int i = 0; i<array.length-1; i++) {
            if(array[i] > array[i+1]) {
                count++;
            } else {
                num++;
            }
        }

        if((count == 0 && num == array.length-1) || (count == array.length-1 && num == 0) ) {
            flag = true;
        }
        return flag;
    }
}
