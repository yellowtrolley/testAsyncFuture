package org.pgg.testAsyncFuture.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.pgg.testAsyncFuture.model.JobUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service("slowService")
public class SlowServiceImpl implements SlowService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	List<JobUnit> jobUnitList;
	AtomicBoolean working = new AtomicBoolean(false);
	
	@Override
	@Async
	public Future<List<JobUnit>> work() {
		if(working.compareAndSet(false, true)) {
			jobUnitList = Collections.synchronizedList(new ArrayList<JobUnit>());
	
			for(int i=0 ; i<25 ; i++) {
				try {
					logger.debug("Thread going to sleep " + i);
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				jobUnitList.add(new JobUnit("Job unit " + i));
				logger.debug("Added Job unit " + i + " to jobUnitList");
			}
			
			working.set(false);
		}
		
		return new AsyncResult<List<JobUnit>>(jobUnitList);
	}

	@Override
	public List<JobUnit> doneSoFar() {
		return jobUnitList;
	}
	
}
