package org.example;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Application {
    private static final Path PATH = Paths.get("src/main/resources/files");

    public static void main(String[] args) {
        doSomeMagic();
    }

    public static void doSomeMagic(){
        List<Path> files = new ArrayList<>();
        try {
            files = Files.find(PATH, 1, (p, a) -> a.isRegularFile())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        printAllInfo(files);
        while (true){
            List<Path> filteredFiles = new ArrayList<>();
            System.out.println("_______________");
            System.out.println("Select one option to execute");
            System.out.println("filter by type [1]");
            System.out.println("filter by creation time [2]");
            System.out.println("filter by update time [3]");
            System.out.println("filter by last entry [4]");
            int userInput = new Scanner(System.in).nextShort();
            switch (userInput){
                case 1 -> filteredFiles = filterByFileType(files);
                case 2 -> filteredFiles = filterByCreationTime(files);
                case 3 -> filteredFiles = filterByUpdateTime(files);
                case 4 -> filteredFiles = filterByLastEntry(files);
            }
            System.out.println("_______________");
            printAllInfo(filteredFiles);
        }
    }

    public static List<Path> filterByCreationTime(List<Path> files){
        System.out.print("File add time : ");
        LocalDateTime createTime = LocalDateTime.parse(new Scanner(System.in).next());
        return files.stream().filter(file ->
           Objects.requireNonNull(getCreationTime(file)).isAfter(createTime)
        ).collect(Collectors.toList());
    }

    public static List<Path> filterByUpdateTime(List<Path> files){
        System.out.print("File update time : ");
        LocalDateTime updateTime = LocalDateTime.parse(new Scanner(System.in).next());
        return files.stream().filter(file ->
                Objects.requireNonNull(getUpdateTime(file)).isAfter(updateTime)
        ).collect(Collectors.toList());
    }

    public static List<Path> filterByLastEntry(List<Path> files){
        System.out.print("Last entry : ");
        LocalDateTime lastEntry = LocalDateTime.parse(new Scanner(System.in).next());
        return files.stream().filter(file ->
                Objects.requireNonNull(getLastEntry(file)).isAfter(lastEntry)
        ).collect(Collectors.toList());
    }

    public static List<Path> filterByFileType(List<Path> files){
        System.out.print("File type : ");
        String fileType = new Scanner(System.in).next();
        return files.stream().filter(file -> file.toString().endsWith(fileType)).collect(Collectors.toList());
    }

    public static void printAllInfo(List<Path> files){
        for (Path file : files) {
            System.out.printf("Start parsing file : %s%n", file.getFileName());
            System.out.println("\t-Creation time : " + getCreationTime(file));
            System.out.println("\t-Update time : " + getUpdateTime(file));
            System.out.println("\t-Last entry : " + getLastEntry(file));
        }
    }

    public static LocalDateTime getCreationTime(Path file){
        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            FileTime fileTime = attr.creationTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return LocalDateTime.parse(fileTime.toString().substring(0,19),formatter);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime getUpdateTime(Path file){
        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            FileTime fileTime = attr.lastModifiedTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return LocalDateTime.parse(fileTime.toString().substring(0,19),formatter);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDateTime getLastEntry(Path file){
        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            FileTime fileTime = attr.lastAccessTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return LocalDateTime.parse(fileTime.toString().substring(0,19),formatter);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
