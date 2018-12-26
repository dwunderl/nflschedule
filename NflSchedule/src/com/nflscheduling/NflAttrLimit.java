package com.nflscheduling;

import java.util.*;

public class NflAttrLimit {

   // ---
   // Instance data

   public String attrName;
   public int weekNum;
   public int[] weeklyLimit;

   NflAttrLimit() {
      System.out.println("Creating an nflAttrLimit");
      weeklyLimit = new int[NflDefs.numberOfWeeks];
   }
}

