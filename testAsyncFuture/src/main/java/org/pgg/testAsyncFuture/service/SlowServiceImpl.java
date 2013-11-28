package org.pgg.testAsyncFuture.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
	int counter;
	
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
	
	@Override
	@Async
	public Future<List<JobUnit>> work() {
		if(working.compareAndSet(false, true)) {
			counter = 25;
			jobUnitList = new ArrayList<>();
	
			long delay = 5000;
			for(int i=0 ; i<25 ; i++) {
				takeAlongTime(i, delay);				
				delay = delay + 1000;
			}
			
		}
		return new AsyncResult<List<JobUnit>>(jobUnitList);
	}
	
	
	
	public boolean isDone() {
		return counter == 0;
	}
	
	private void takeAlongTime(final int i, final long delay) {
		executor.schedule(new Runnable() {
			@Override
			public void run() {
				jobUnitList.add(new JobUnit("Job unit " + i));
				counter--;
				
				if(counter == 0) {
					working.set(false);
				}
			}}, 
			delay, TimeUnit.MILLISECONDS);
	}
}
