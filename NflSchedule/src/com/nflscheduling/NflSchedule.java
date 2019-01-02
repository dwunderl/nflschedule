package com.nflscheduling;

import java.util.*;
import java.io.File;
import java.io.IOException;

public class NflSchedule {

   // ---
   // Instance data

   public ArrayList<NflTeamSchedule> teams;
   public ArrayList<NflResource> resources;
   public ArrayList<NflGameSchedule> allGames;
   public ArrayList<NflGameSchedule> unscheduledGames;
   public ArrayList<NflGameSchedule> unscheduledByes;
   public ArrayList<NflScheduleMetric> scheduleMetrics;
   public double score = 0.0;
   public int byesToScheduleThisWeek;
   public double latestScheduleFingerPrint;
   public static Random rnd = new Random();

   // Nfl Schedule Object
   //    Models an instance of a schedule
   //    Owns an array of team schedules
   //    Owns a list of unscheduled Games
   //    We model a scheduled game by ... each NflTeamSchedule holds a list of scheduledGames (NflGameSchedule)
   //    allGames
   
   NflSchedule() {
      System.out.println("Creating an nflSchedule");
      teams = new ArrayList<NflTeamSchedule>();
      allGames = new ArrayList<NflGameSchedule>();
      unscheduledGames = new ArrayList<NflGameSchedule>();
      unscheduledByes = new ArrayList<NflGameSchedule>();
      resources = new ArrayList<NflResource>();
      
      scheduleMetrics = new ArrayList<NflScheduleMetric>();
      
      //NflSMetNoRepeatedMatchup metricNRM = new NflSMetNoRepeatedMatchup("NoRepeatedMatchup");
      //scheduleMetrics.add(metricNRM);
      NflSMetRoadTripLimit metricRTL = new NflSMetRoadTripLimit("RoadTripLimit");
      scheduleMetrics.add(metricRTL);
      NflSMetHomeStandLimit metricHSL = new NflSMetHomeStandLimit("HomeStandLimit");
      scheduleMetrics.add(metricHSL);
      //NflSMetBalancedHomeAway metricBalHA = new NflSMetBalancedHomeAway("Balanced Home Away", this);
      //scheduleMetrics.add(metricBalHA);
   }

   public boolean createTeamSchedules(ArrayList<NflTeam> baseTeams) {
	  for (int ti=0; ti < baseTeams.size(); ti++) {
	     NflTeam team = baseTeams.get(ti);
	     
	     NflTeamSchedule teamSchedule = new NflTeamSchedule(team);
	     teams.add(teamSchedule);
	  }

      return true;
   }
   
   public boolean createGameSchedules(ArrayList<NflGame> baseGames) {
      for (int gi=0; gi < baseGames.size(); gi++) {
         NflGame game = baseGames.get(gi);
			     
         NflGameSchedule gameSchedule = new NflGameSchedule(game, this);
         if (gameSchedule.isBye) {
            gameSchedule.initBye();
            unscheduledByes.add(gameSchedule);
         }
         else {
            gameSchedule.initGame();
            unscheduledGames.add(gameSchedule);
         }
      }

      return true;
   }
  
   public boolean createByeSchedules() {

      // TBD:Byes
      // create a bye/gameschedule for each pair of teams according NflDefs.numberOfTeams/2
      // keep a collection of the byes
      // create an empty NflGame for each bye gameSchedule - to hold the assigned teams
	  for (int ti=0; ti < teams.size()/2; ti++) {
		  NflGame game = new NflGame();
		  NflGameSchedule bye = new NflGameSchedule(game, this);
		  bye.initBye();
		  unscheduledByes.add(bye);
	  }

      return true;
   }

   public NflTeamSchedule findTeam(String teamName) {
	  if (teamName == null) return null;
	  
      for (int ti=0; ti < teams.size(); ti++) {
         NflTeamSchedule teamSchedule = teams.get(ti);
         if (teamName.equalsIgnoreCase(teamSchedule.team.teamName)) {
            return teamSchedule;
         }
      }

      return null;
   }

   public NflResource findResource(String attrName) {
      for (int ali=0; ali < resources.size(); ali++) {
         NflResource resource = resources.get(ali);
         if (attrName.equalsIgnoreCase(resource.resourceName)) {
            return resource;
         }
      }

      return null;
   }

   public boolean resourceExists(String resourceName) {
      NflResource resource = findResource(resourceName);
      return resource != null;
   }

   public boolean resourceHasCapacity(String resourceName, int weekNum) {
      NflResource resource = findResource(resourceName);
      if (resource != null) {
          if (resource.weeklyLimit[weekNum-1] > 0) {
            return true;
         }
      }

      return false;
   }
   
   public int scheduledTeamsInWeek(int weekNum) {
	  int gameCount = 0;
	  for (int ti=0; ti < teams.size(); ti++) {
	     NflTeamSchedule teamSchedule = teams.get(ti);
	     if (teamSchedule.scheduledGames[weekNum-1] != null) {
            gameCount++;
	     }
	  }
	   
	  return gameCount;
   }

   public int unscheduledTeamsInWeek(int weekNum, ArrayList<NflTeamSchedule> unscheduledGames) {
	  int gameCount = 0;
	  for (int ti=0; ti < teams.size(); ti++) {
	     NflTeamSchedule teamSchedule = teams.get(ti);
	     if (teamSchedule.scheduledGames[weekNum-1] == null) {
	    	    if (!teamSchedule.hasScheduledBye()) {
               gameCount++;
               unscheduledGames.add(teamSchedule);
	    	    }
	     }
	  }
	   
	  return gameCount;
  }

   public boolean computeMetrics() {
      score = 0.0;
      for (int mi=0; mi < scheduleMetrics.size(); mi++) {
         NflScheduleMetric scheduleMetric = scheduleMetrics.get(mi);
         //System.out.println("Computing Metric: " + gameMetric.metricName + " for game: " + game.homeTeam + " : " + game.awayTeam);
         scheduleMetric.computeMetric(this);
         score += scheduleMetric.score;
         System.out.println("Computing ScheduleMetric: " + scheduleMetric.metricName + " score: " + scheduleMetric.score);
      }
      
      return true;
   }
   
   public int byeCapacityAvail(int weekNum) {
	   NflResource byeResource = findResource("Bye");
       int byeAvail = byeResource.weeklyLimit[weekNum-1] - byeResource.usage[weekNum-1];
       return byeAvail;
   }
   
   public boolean determineNumByesForWeek(NflSchedule schedule, int weekNum) {
	   // byesToScheduleThisWeek - set
	   // remaining byeCapacity in resource "Bye" from weeknum back

	   byesToScheduleThisWeek = 0;
	   float remainingByeCapacity = 0;
	   float remainingByesToSchedule = 0;
	   
	   NflResource byeResource = findResource("Bye");
	   
       if (byeResource == null) {
          return true;
       }
       
       // byesToScheduleThisWeek = byeResource.weeklyLimit[weekNum-1] - byeResource.usage[weekNum-1];

	   for (int wi=weekNum; wi >= 1; wi--) {
          remainingByeCapacity += byeResource.weeklyLimit[wi-1] - byeResource.usage[wi-1];
	   }
	   
	   // remaining byes to be scheduled
	   remainingByesToSchedule = unscheduledByes.size();

	   if (remainingByeCapacity == 0 || remainingByesToSchedule == 0) {
		   byesToScheduleThisWeek = 0;
		   return true;
	   }
	   
	   // Determine number of byes already scheduled (forced) in this week
	   // Loop through all of the teamschedules, and check
	   
	   int byesScheduledThisWeek = 0;
	   
	   for (NflTeamSchedule teamSched: schedule.teams) {
		   NflGameSchedule gameInThisWeek = teamSched.scheduledGames[weekNum-1];
		   if (gameInThisWeek != null) {
			   if (gameInThisWeek.isBye) {
				   byesScheduledThisWeek++;
			   }
		   }
	   }
	   
       int min = Math.max(byeResource.weeklyMinimum[weekNum-1] - byesScheduledThisWeek, 0);
	   int max = Math.max(byeResource.weeklyLimit[weekNum-1] - byesScheduledThisWeek, 0);

	   if (remainingByesToSchedule > remainingByeCapacity) {
	       System.out.println("   ERROR determineNumByesForWeek: For week: " + weekNum + " insufficient bye capacity: " + remainingByeCapacity + ", remainingByesToSchedule: " + remainingByesToSchedule);
	       //ERROR determineNumByesForWeek: For week: 4 insufficient bye capacity: 6.0, remainingByesToSchedule: 8.0
	   }
	   
	   if (remainingByesToSchedule >= remainingByeCapacity) {
		   byesToScheduleThisWeek = max;
		   return true;
	   }
	   
	   double excessAvailRatio = (remainingByeCapacity-remainingByesToSchedule)/remainingByeCapacity;
       double randomNum  = rnd.nextDouble();
       double useMaxByeCapacityProb = randomNum;
       //double useMaxByeCapacityProb = excessAvailRatio*randomNum;
       
       //useMaxByeCapacityProb = 1.0 - useMaxByeCapacityProb;
       //useMaxByeCapacityProb = randomNum;

       byesToScheduleThisWeek = (int) (useMaxByeCapacityProb*(max - min) + min);
	   
	   for (int bc=min; bc <= max; bc += 2) {
		   if (byesToScheduleThisWeek <= bc) {
			   byesToScheduleThisWeek = bc;
			   break;
		   }
	   }
	   
	   if (byesToScheduleThisWeek > 0) {
          // System.out.println("   For week: " + weekNum + " byesToScheduleThisWeek: " + byesToScheduleThisWeek);
	   }

	   return true;
   }
}
