package com.reengen.utils.auditreporter;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.reengen.utils.auditreporter.FileForAuditReporter;;

public class Runner {

    private List<List<String>> users;
    private List<List<String>> fileList;
    List<FileForAuditReporter> fileAuditList;
    Map<Long, String> userList;
    private static final String SEPRARATOR = ",";
    private static final String CSV = "CSV";
    private static final String DEFAULT = "DEFAULT";
    

    public static void main(String[] args) throws IOException {
        Runner r = new Runner();
        r.loadData(args[0], args[1]);
        if(args.length == 2) {
            r.run(DEFAULT);
        } else if(args.length == 3) {
        	if(args[2].toString().equalsIgnoreCase("-c")) {
        		r.run(CSV);
        	}
        } else if(args.length == 4) {
        	if(args[2].toString().equalsIgnoreCase("--top")) {
        		r.printTopFileSize(DEFAULT, Integer.valueOf(args[3]));
        	} 
        } else if (args.length == 5) {
        	if(args[2].toString().equalsIgnoreCase("-c")) {
        		if(args[3].toString().equalsIgnoreCase("--top")) {
        			r.printTopFileSize(CSV, Integer.valueOf(args[4]));
        		}
        	}
        } else {
        	System.err.println("Wrong usage");
        }
        
    }

    private void run(String outputMode) {
    	
    	if(outputMode.equals(DEFAULT)) {
    		printHeader();
    	}
    	
        for (List<String> userRow : users) {
            long userId = Long.parseLong(userRow.get(0));
            String userName = userRow.get(1);
            
            if(outputMode.equals(DEFAULT)) {
            	printUserHeader(userName);
            }
            
            for (List<String> fileRow : fileList) {
                long fileSize = Long.parseLong(fileRow.get(1));
                String fileName = fileRow.get(2);
                long ownerUserId = Long.parseLong(fileRow.get(3));
                if (ownerUserId == userId) {
                	if(outputMode.equalsIgnoreCase(DEFAULT)) {
                		printFile(fileName, fileSize);
                	} else {
                		System.out.println(fileName + SEPRARATOR + userName + SEPRARATOR + fileSize);
                	}
                }
            }
        }
    }


       private void printTopFileSize(String mode, int limit) {
    	   
    	   userList = new HashMap<Long, String>();
    	   for(List<String> userRow : users) {
    		   userList.put(Long.valueOf(userRow.get(0)), userRow.get(1));
    	   }
    	
    	   fileAuditList= new ArrayList<FileForAuditReporter>();

	            for (List<String> fileRow : fileList) {
	            	FileForAuditReporter tempObjectForList = new FileForAuditReporter();
	            	tempObjectForList.setFileID(fileRow.get(0));
	            	tempObjectForList.setFileSize(Long.valueOf(fileRow.get(1)));
	            	tempObjectForList.setFileName(fileRow.get(2));
	            	tempObjectForList.setOwnerUserID(Long.valueOf(fileRow.get(3)));
	            	fileAuditList.add(tempObjectForList);
	            }	        
	       
	            Collections.sort(fileAuditList, Collections.reverseOrder());
	            
	            if(mode.equalsIgnoreCase(CSV)) {
	            	fileAuditList.stream().limit(limit).forEach(lineOfAudit -> System.out.println(lineOfAudit.fileName + SEPRARATOR + userList.get(lineOfAudit.getOwnerUserID())  + SEPRARATOR  + lineOfAudit.fileSize ));	
	            } else if(mode.equalsIgnoreCase(DEFAULT)) {
	            	printHeader();
	            	for(int i = 0; i<limit; i++) {
	            		printTopFile(fileAuditList.get(i).getFileName(), userList.get(fileAuditList.get(i).getOwnerUserID()), fileAuditList.get(i).getFileSize());
	            	}
	            }
       }

    private void loadData(String userFn, String filesFn) throws IOException {
     
        try(BufferedReader user = new BufferedReader(new FileReader(userFn))) {
            users = new ArrayList<List<String>>();
            List<List<String>> linesOfFile;
            
            linesOfFile = user.lines()
            		.skip(1)
            		.map(lineInsideOfFile -> Arrays.asList(lineInsideOfFile.split(SEPRARATOR)))
            		.collect(Collectors.toList());
            
            linesOfFile.forEach(string -> {users.add(string);});
            
        } 
        
        try(BufferedReader file = new BufferedReader(new FileReader(filesFn))) {
            fileList = new ArrayList<List<String>>();
            List<List<String>> linesOfFile;
            
            linesOfFile = file.lines()
            		.skip(1)
            		.map(lineInsideOfFile -> Arrays.asList(lineInsideOfFile.split(SEPRARATOR)))
            		.collect(Collectors.toList());
            
            linesOfFile.forEach(string -> {fileList.add(string);});
        } 
    }
    

    private void printHeader() {
        System.out.println("Audit Report");
        System.out.println("============");
    }

    private void printUserHeader(String userName) {
        System.out.println("## User: " + userName);
    }

    private void printFile(String fileName, long fileSize) {
        System.out.println("* " + fileName + " ==> " + fileSize + " bytes");
        
    }
    
    private void printTopFile(String fileName, String userName, long fileSize) {
    	System.out.println("* " + fileName + " ==> " + "user " + userName + ", " + fileSize + " bytes");
    }
    
}
