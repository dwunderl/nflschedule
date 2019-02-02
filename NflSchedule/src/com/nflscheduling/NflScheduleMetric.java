package com.nflscheduling;

import java.util.ArrayList;

public class NflScheduleMetric {

   public String metricName;
   public double weight;
   public double  score;   // combined and weighted 

   public NflScheduleMetric() {
      // TODO Auto-generated constructor stub
   }
		
   NflScheduleMetric(String theName) {
      //System.out.println("Creating an nflGameMetric");
      metricName = theName;
   }

   // use @Override when inheriting from nflScheduleMetric for this function
   public boolean computeMetric(NflSchedule schedule) {
      return false;
   }
   
   // use @Override when inheriting from nflScheduleMetric for this function
   public boolean computeMetric(NflSchedule schedule, String gameMetricName) {
      ArrayList<NflGameSchedule> candidateGames = null;
      score = 0;
      for (int ti=1; ti <= NflDefs.numberOfTeams; ti++) {
         NflTeamSchedule teamSchedule = schedule.teamSchedules.get(ti-1);
         for (int wi=1; wi <= NflDefs.numberOfWeeks; wi++) {
            NflGameSchedule teamGame = teamSchedule.scheduledGames[wi-1];
            if (teamGame == null || teamGame.isBye) {
               continue;
            }
            
	   		NflGameMetric gameMetric = teamGame.findMetric(gameMetricName);

	   		gameMetric.computeMetric(wi, schedule, candidateGames);
            score += gameMetric.score;
            
            if (gameMetric.score >= 1.0) {
    		    System.out.println("ScheduleMetric : " + metricName + " alert for game, weekNum: " + wi + " home team: " + teamGame.game.homeTeam + " away team: " + teamGame.game.awayTeam 
    		    		                + ", score: " + gameMetric.score);
    		    NflScheduleAlert alert = new NflScheduleAlert();
    		    alert.alertDescr = metricName;
    		    alert.weekNum = wi;
    		    alert.homeTeam = teamGame.game.homeTeam;
    		    alert.awayTeam = teamGame.game.awayTeam;
    		    schedule.alerts.add(alert);
            }
         }
      }

      return true;
   }
}
