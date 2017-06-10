package com.reengen.utils.auditreporter;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Runner {

    private List<List<String>> users;
    private List<List<String>> fileList;
    private static final String SEPRARATOR = ",";

    public static void main(String[] args) throws IOException {
        Runner r = new Runner();
        
        if(args.length == 2) {
            r.loadData(args[0], args[1]);
            r.run();
        } else if(args.length == 3) {
        	if(args[2].toString().equalsIgnoreCase("-c")) {
        		System.out.println("-c");
        	}
        } else if(args.length == 4) {
        	if(args[2].toString().equalsIgnoreCase("--top")) {
        		System.out.println(args[3]);
        	}
        } else {
        	System.err.println("Wrong usage..");
        	
        }

    }

    private void run() {
        printHeader();
        for (List<String> userRow : users) {
            long userId = Long.parseLong(userRow.get(0));
            String userName = userRow.get(1);

            printUserHeader(userName);
            for (List<String> fileRow : fileList) {
                String fileId = fileRow.get(0);
                long size = Long.parseLong(fileRow.get(1));
                String fileName = fileRow.get(2);
                long ownerUserId = Long.parseLong(fileRow.get(3));
                if (ownerUserId == userId) {
                    printFile(fileName, size);
                }
            }
        }
    }

    private void loadData(String userFn, String filesFn) throws IOException {
     
    	//TODO: Refactor this method for DRY
        try(BufferedReader user = new BufferedReader(new FileReader(userFn))) {
            users = new ArrayList<List<String>>();
            List<List<String>> linesOfFile;
            
            linesOfFile = user.lines()
            		.skip(1)
            		.map(lineInsideFile -> Arrays.asList(lineInsideFile.split(SEPRARATOR)))
            		.collect(Collectors.toList());
            
            linesOfFile.forEach(string -> {users.add(string);});
            
        } 
        
        try(BufferedReader file = new BufferedReader(new FileReader(filesFn))) {
            fileList = new ArrayList<List<String>>();
            List<List<String>> linesOfFile;
            
            linesOfFile = file.lines()
            		.skip(1)
            		.map(lineInsideFile -> Arrays.asList(lineInsideFile.split(SEPRARATOR)))
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

}
