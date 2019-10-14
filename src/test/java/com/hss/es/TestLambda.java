package com.hss.es;

import com.hss.entity.Product;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * lambda表达式测试类
 */
public class TestLambda {

    private final static Logger logger = Logger.getLogger(TestLambda.class);

    @Test
    public void test1(){
        //数组快速排序
        String[] arr1 = new String[]{"java","php","python","go"};
        Arrays.parallelSort(arr1, (o1,o2) -> o1.length() - o2.length());
        logger.info("arr1="+Arrays.toString(arr1));
        //left代表数组中当前一个索引处的元素，计算第一个元素时，left为1
        //reght代表数组中当前索引处的元素
        int[] arr2 = new int[]{3, -4, 25, 16, 30, 18};
        Arrays.parallelPrefix(arr2, ((left, right) -> left * right));
        logger.info("arr2="+Arrays.toString(arr2));

        long[] arr3 = new long[5];
        //operand代表正在计算的元素索引
        Arrays.parallelSetAll(arr3, operand -> operand * 5);
        logger.info("arr3"+Arrays.toString(arr3));
    }

    @Test
    public void test2(){
        //lambda表达式操作集合
        List<Product> productList = new ArrayList<Product>();

        Product product1 = new Product();
        product1.setName("hss1");
        productList.add(product1);

        Product product2 = new Product();
        product2.setName("hss2");
        productList.add(product2);

        Product product3 = new Product();
        product3.setName("hss3");
        productList.add(product3);
        logger.info("productList="+productList.toString());
        productList.forEach(product -> {
            product.setVersion("1.0.1");
        });
        logger.info("productList="+productList.toString());
    }
}
