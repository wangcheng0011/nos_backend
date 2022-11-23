package com.knd.batch.job;

import com.knd.batch.service.IAttachService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeleteObsJob implements BaseJob {

    @Autowired
    private IAttachService iAttachService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        iAttachService.deleteObsFile();
    }
}
