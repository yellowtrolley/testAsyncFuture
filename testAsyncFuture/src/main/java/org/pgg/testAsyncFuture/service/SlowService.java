package org.pgg.testAsyncFuture.service;

import java.util.List;
import java.util.concurrent.Future;

import org.pgg.testAsyncFuture.model.JobUnit;

public interface SlowService {

	Future<List<JobUnit>> work();
	List<JobUnit> doneSoFar();
}