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

    public void run(String outputMode) {
    	
    	if(outputMode.equals(DEFAULT)) {
    		printHeader();
    	}
    	
        for (List<String> userRow : users) {
            long userId = Long.parseLong(userRow.get(0));
            String userName = userRow.get(1);
            
            if(outputMode.equals(DEFAULT)) {
            	printUserHeader(userName);
            }
            
            fileAuditList = getFileList();
            
	            for (FileForAuditReporter fileInformation : fileAuditList) {
					if(fileInformation.getOwnerUserID() == userId) {
	                	if(outputMode.equalsIgnoreCase(DEFAULT)) {
	                		System.out.println(printFile(fileInformation.getFileName(), fileInformation.getFileSize()));
	                	} else {
	                		System.out.println(fileInformation.getFileName() + SEPRARATOR + userName + SEPRARATOR + fileInformation.getFileSize());
	                	}
					}
				}
         }
     }


    public void printTopFileSize(String mode, int limit) {
    	   
    	   userList = getUserList();
    	
    	   fileAuditList = getFileList();
	       Collections.sort(fileAuditList, Collections.reverseOrder());
	            
	       if(mode.equalsIgnoreCase(CSV)) {
	           	fileAuditList.stream().limit(limit).forEach(lineOfAudit -> System.out.println(lineOfAudit.fileName + SEPRARATOR + userList.get(lineOfAudit.getOwnerUserID())  + SEPRARATOR  + lineOfAudit.fileSize ));	
	        } else if(mode.equalsIgnoreCase(DEFAULT)) {
	           	printHeader();
	         	for(int i = 0; i<limit; i++) {
	          		System.out.println(printTopFile(fileAuditList.get(i).getFileName(), userList.get(fileAuditList.get(i).getOwnerUserID()), fileAuditList.get(i).getFileSize()));
	           	}
	         }
       }

    public void loadData(String userFn, String filesFn) throws IOException {
     
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
    
    public ArrayList<FileForAuditReporter> getFileList() {
    	
 	   fileAuditList = new ArrayList<FileForAuditReporter>();

       for (List<String> fileRow : fileList) {
	       	FileForAuditReporter tempObjectForList = new FileForAuditReporter();
	       	tempObjectForList.setFileID(fileRow.get(0));
	       	tempObjectForList.setFileSize(Long.valueOf(fileRow.get(1)));
	       	tempObjectForList.setFileName(fileRow.get(2));
	       	tempObjectForList.setOwnerUserID(Long.valueOf(fileRow.get(3)));
	       	fileAuditList.add(tempObjectForList);
       }
       
       return (ArrayList<FileForAuditReporter>) fileAuditList;
    }
    
   public Map<Long, String> getUserList() {
	   userList = new HashMap<Long, String>();
	   for(List<String> userRow : users) {
		   userList.put(Long.valueOf(userRow.get(0)), userRow.get(1));
	   }
	   
	   return userList;
   }
    

    public void printHeader() {
        System.out.println("Audit Report");
        System.out.println("============");
    }

    public String printUserHeader(String userName) {
        return "## User: " + userName;
    }

    public String printFile(String fileName, long fileSize) {
        return "* " + fileName + " ==> " + fileSize + " bytes";
        
    }
    
    public String printTopFile(String fileName, String userName, long fileSize) {
    	return "* " + fileName + " ==> " + "user " + userName + ", " + fileSize + " bytes";
    }
    
}
