import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import static com.rain.util.Snowflake.getSnowflakeId;

@Slf4j
public class JunitTest {

    @Test
    public void testFirst() {
        System.out.println("hello world!");
    }

    @Test
    public void testPhone() {
        try {
            checkPhone("15602227266");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 校验电话号码
     **/
    private void checkPhone(String phone) throws Exception {
        if (!Pattern.compile("^1[3|4|5|7|8|9][0-9]\\d{4,8}$").matcher(phone).matches()) {
            throw new Exception();
        }
    }

    /**
     * 引用类型
     * 1、被强引用关联的对象不会被回收。
     * 2、被软引用关联的对象只有在内存不够的情况下才会被回收。
     * 3、被弱引用关联的对象一定会被回收，也就是说它只能存活到下一次垃圾回收发生之前。
     * 4、为一个对象设置虚引用的唯一目的是能在这个对象被回收时收到一个系统通知。
     **/
    @Test
    public void testobject() {

        Object obj = new Object();

        //软引用
        SoftReference<Object> sf = new SoftReference<>(obj);

        //弱引用
        WeakReference<Object> wf = new WeakReference<>(obj);

        //虚引用
        PhantomReference<Object> pf = new PhantomReference<>(obj, null);

        obj = null;
    }

    /**
     * 雪花算法测试用例
     **/
    @Test
    public void test() {
        long start = System.currentTimeMillis();
        try {
            for (int i = 0, len = 100000; i < len; i++) {
                System.out.println(getSnowflakeId());
            }
        } catch (Exception e) {
            System.out.println("异常");
        }
        System.out.println("10万耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 测试线程池的使用
     **/
    @Test
    public void testThreadPool() throws Exception {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 2, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        threadPoolExecutor.execute(() -> System.out.println("hello ya!"));

        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            int result = 0;
            for (int i = 0; i < 100; i++) {
                result += i;
            }
            return result;
        });
        threadPoolExecutor.submit(futureTask);
        System.out.println(futureTask.get());
    }
}
