package com.rain.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.rain.api.apple.model.User;
import com.rain.dao.BananaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * UserListener
 *
 * @author lazy cat
 * 2020-08-27
 **/
public class UserListener extends AnalysisEventListener<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserListener.class);

    private static final int BATCH_COUNT = 5000;

    private List<User> list = new ArrayList<>();

    private BananaMapper bananaMapper;

    private int sum;

    public UserListener(BananaMapper bananaMapper) {
        this.bananaMapper = bananaMapper;
        sum = 0;
    }

    @Override
    public void invoke(User data, AnalysisContext context) {
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        LOGGER.info("一共{}条数据解析存储完成.", sum);
    }

    private void saveData() {
        LOGGER.info("{}条数据解析完成，开始存储数据库...", list.size());
        sum = sum + list.size();
        System.out.println(list.toString());
        bananaMapper.batchInsertUser(list);
        LOGGER.info("存储数据库成功！");
    }
}
