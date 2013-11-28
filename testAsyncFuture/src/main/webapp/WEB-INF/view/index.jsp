<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>Spring Async Future example</title>
		<script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
		
		<spring:url var="startJobUrl" value="/startJob"/>
		<spring:url var="jobStatusUrl" value="/jobStatus"/>
		<script>
		  	$(function() {
		  		$('#startJob').on('click', function (e) {
		  			e.preventDefault();
				  	$.ajax({
			            dataType: 'json',
			            contentType: "application/json;charset=UTF-8",
			            type: 'POST',
						url: '${startJobUrl}'
			        }).success(function(data, textStatus, jqXHR){
			        	$('#jobStatus').html(data);
			        	jobStatusPoll();
			        }).fail(function( jqXHR, textStatus ) {
			        	console.log(jqXHR.status);
			        	console.log( "Request failed: " + textStatus );
			        });
		  		});
		  	});
		  	
		  	function jobStatusPoll(){
		  		$.ajax({
		            dataType: 'json',
		            contentType: "application/json;charset=UTF-8",
		            type: 'POST',
					url: '${jobStatusUrl}'
		        }).done(function(doneSoFar, textStatus, jqXHR){
	  	    		$('ul#results li').remove();
	  	    		$.each(doneSoFar, function(done, doneSoFar) { // map of one element
	  	    			$('#jobStatus').html(done);
	  	    				
	  	    			$.each(doneSoFar, function(i, jobUnit){ // list
		  	    			if(jobUnit) {
				  	    		$('ul#results').append('<li>' + jobUnit.name + '</li>');
	  	    				}
			  	    	});

	  	    			if(done == "false") {
				  	        setTimeout(jobStatusPoll, 2000);
			  	    	}
	  	    		});
		        }).fail(function( jqXHR, textStatus ) {
		        	console.log(jqXHR.status);
		        	console.log( "Request failed: " + textStatus );
		        });
		  	}
	    </script>
	</head> 
	<body>
		<a href="#" id="startJob">Start job</a>
		<br/>
		<div>Job done? <span id="jobStatus"></span></div>
		<ul id="results"></ul>
	</body>
</html>
