package com.jf.weidong.doc.check;

import com.jf.weidong.doc.service.BorrowManageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyJob implements Job {
    BorrowManageService borrowManageService = new BorrowManageService();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        borrowManageService.checkBorrowInfo();
    }
}
