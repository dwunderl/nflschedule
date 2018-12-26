package com.nflscheduling;

import java.util.*;

public class NflResource {

   // ---
   // Instance data

   public String resourceName;
   public int weekNum;
   public int[] weeklyLimit;
   public int[] weeklyMinimum;
   public int[] usage;

   NflResource() {
      System.out.println("Creating an nflAttrLimit");
      weeklyLimit = new int[NflDefs.numberOfWeeks];
      weeklyMinimum = new int[NflDefs.numberOfWeeks];
      usage = new int[NflDefs.numberOfWeeks];
   }
   
   public boolean hasCapacity(int weekNum) {
	   if (usage[weekNum-1] < weeklyLimit[weekNum-1]) {
		   return true;
	   }
	   
	   return false;
   }
}

