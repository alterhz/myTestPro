package com.magazine;

import com.magazine.dao.FilterRepository;
import com.magazine.model.SheetFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension. class)
@SpringBootTest(classes = MagazineApplication.class)
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FilterRepository filterRepository;

    @Test
    public void testView() {
        final SheetFilter sheetFilter = new SheetFilter();
        sheetFilter.setSheetName("bookFilter");
        filterRepository.setFilter("bookFilter", sheetFilter);

        final SheetFilter filter = filterRepository.getFilter(sheetFilter.getSheetName());
        System.out.println("filter = " + filter);
    }


    @Test
    public void test() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111");
//        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }

//    @Test
//    public void testObj() throws Exception {
//        SheetRow sheetRow =new SheetRow();
//        ValueOperations<String, SheetRow> operations=redisTemplate.opsForValue();
//        operations.set("com.neox", sheetRow);
//        operations.set("com.neo.f", sheetRow,1, TimeUnit.SECONDS);
//        Thread.sleep(1000);
//        //redisTemplate.delete("com.neo.f");
//        boolean exists=redisTemplate.hasKey("com.neox");
//        if(exists){
//            System.out.println("exists is true");
//        }else{
//            System.out.println("exists is false");
//        }
//        // Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
//    }
}
