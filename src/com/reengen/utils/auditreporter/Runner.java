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
    private List<List<String>> files;

    public static void main(String[] args) throws IOException {
        Runner r = new Runner();
        r.loadData(args[0], args[1]);
        r.run();
    }

    private void run() {
        printHeader();
        for (List<String> userRow : users) {
            long userId = Long.parseLong(userRow.get(0));
            String userName = userRow.get(1);

            printUserHeader(userName);
            for (List<String> fileRow : files) {
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
        String line;
     
        try(BufferedReader userReader = new BufferedReader(new FileReader(userFn))) {
            users = new ArrayList<List<String>>();

            userReader.readLine(); // skip header

            while ((line = userReader.readLine()) != null) {
                users.add(Arrays.asList(line.split(",")));
            }
        } 
        
        try(BufferedReader fileReader = new BufferedReader(new FileReader(filesFn))) {
            files = new ArrayList<List<String>>();

            fileReader.readLine(); // skip header

            while ((line = fileReader.readLine()) != null) {
                files.add(Arrays.asList(line.split(",")));
            }
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
