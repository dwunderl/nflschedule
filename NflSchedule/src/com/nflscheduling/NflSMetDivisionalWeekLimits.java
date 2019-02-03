package com.nflscheduling;

import java.util.ArrayList;

public class NflSMetDivisionalWeekLimits extends NflScheduleMetric {

	public NflSMetDivisionalWeekLimits() {
		// TODO Auto-generated constructor stub
	}

	public NflSMetDivisionalWeekLimits(String theName) {
		super(theName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean computeMetric(NflSchedule schedule) {
	   score = 0;
	   
	   for (int wi=1; wi <= NflDefs.numberOfWeeks-1; wi++) {
		  int divisionalGameCount = schedule.divisionalGameCount(wi);
	      
	      String limitViolated = "";
	      
	      if (divisionalGameCount > 11) {
	         score = 2.0;
	         limitViolated = " > 11";
	      }
	      else if (divisionalGameCount > 8) {
	         score = 1.0;
	         limitViolated = " > 8";
	      }
	      else if (divisionalGameCount < 2) {
	    	  score = 1.0;
		      limitViolated = " < 2";
	      }
	      
	      if (!limitViolated.isEmpty()) {
	         NflScheduleAlert alert = new NflScheduleAlert();
	         alert.alertDescr = metricName;
	         alert.weekNum = wi;
	         alert.homeTeam = limitViolated;
	         alert.awayTeam = Integer.toString(divisionalGameCount);
	         schedule.alerts.add(alert);
	      }
	   }

	   return true;
	}

}
