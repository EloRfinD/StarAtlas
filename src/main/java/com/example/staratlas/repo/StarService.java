package com.example.staratlas.repo;

import com.example.staratlas.dao.Star;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StarService {
    StarRepo starRepo = new StarRepo();
    private String uploadPath = "data\\desc";

    public String getStarInfo(Star star){
        StringBuilder lines = new StringBuilder();
        String line;
        File file = new File(star.getId());
        boolean firstLine = true;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())))){
            while ((line = br.readLine()) != null) {
                if(!firstLine)
                    lines.append(line).append("<p>");
                firstLine = false;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lines.toString();
    }

    public static String getStarInfoN(int id){
        String uploadPath = "data\\desc\\";
        StringBuilder lines = new StringBuilder();
        String line;
        File file = new File(uploadPath + id + ".txt");
        System.out.println(uploadPath + id + ".txt");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())))){
            while ((line = br.readLine()) != null) {
                lines.append(line).append("<p>");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lines.toString();
    }

    public ArrayList<Star> getAllStars(){
        return starRepo.getAllStars();
    }

    public int getNumberOfStars(){
        return starRepo.getNumberOfStars();
    }

    /*public Optional<Star> findById(int id){
        return Optional.of(starRepo.getAllStars().stream().filter(star -> star.getId() == id).collect(Collectors.toList()).get(0));
    }*/

    public ArrayList<Star> findByName(String name){
        return (ArrayList<Star>) starRepo.getAllStars().stream().filter(star -> star.getName().contains(name)
                || name.contains(star.getName())).collect(Collectors.toList());
    }

    /*public void changeInfo(Star star){
        StringBuilder lines = new StringBuilder();
        String line;

        boolean added = false;
        try(BufferedReader reader = new BufferedReader(new FileReader(starRepo.getUploadPath()))){
            while ((line = reader.readLine()) != null){
                if (Arrays.stream(line.split(";")).collect(Collectors.toList()).get(0) == star.getId()) {
                    lines.append(star).append("\n");
                    added = true;
                }
                else{
                    lines.append(line).append("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!added){
            lines.append(star).append("\n");
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(starRepo.getUploadPath())))
        {
            writer.write(String.valueOf(lines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    public void editMainFileById(String newText, int id){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(uploadPath + id + ".txt")))
        {
            writer.write(newText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createMainFile(String newText, int id){
        //Path path = Paths.get("D:\\Проекты\\SAE build 0.0.3" + uploadPath + id + ".txt");
        Path path = Paths.get(uploadPath + id + ".txt");

        try {
            Files.write(path, newText.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
