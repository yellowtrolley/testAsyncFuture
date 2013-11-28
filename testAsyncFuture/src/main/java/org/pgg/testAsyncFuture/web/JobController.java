package org.pgg.testAsyncFuture.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.pgg.testAsyncFuture.model.JobUnit;
import org.pgg.testAsyncFuture.service.SlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import flexjson.JSONSerializer;

@Controller
public class JobController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Qualifier("slowService")
	@Autowired SlowService slowService;
	
	Future<List<JobUnit>> jobStatus;
	
	
	
	@RequestMapping(value = "/startJob", headers = "Accept=application/json")
	public ResponseEntity<String> startJob() {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        jobStatus = slowService.work();
        
        return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").serialize("job started"), headers, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/jobStatus", headers = "Accept=application/json")
	public ResponseEntity<String> jobStatus() {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        Map<Boolean, List<JobUnit>> jobStatusMap = new HashMap<>();
        
        try {
			jobStatusMap.put(slowService.isDone(), jobStatus.get());
		} catch (InterruptedException | ExecutionException e) {
			logger.debug(e.getMessage(), e);
		}

        return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(jobStatusMap), headers, HttpStatus.OK);
	}
}
