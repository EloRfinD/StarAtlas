package com.example.staratlas.repo;

import com.example.staratlas.dao.Star;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class StarRepo {

    private final String uploadPath1 = "data\\desc";
    private ArrayList<Star> stars = new ArrayList<>();
    private final String uploadPath2 = "data\\move";

    public StarRepo(){
        listFilesForFolder(new File(uploadPath1), "");
    }

    private void listFilesForFolder(final File folder, String path) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry, path + "\\" + fileEntry.getName());
            } else {
                try (BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
                    String[] star;
                    String DELIMITER = ";";

                    String line = br.readLine();
                    star = line.split(DELIMITER);
                    String id = uploadPath1 + path + "\\" + fileEntry.getName();
                    //System.out.println(id);
                    ArrayList<String> starArray = (ArrayList<String>) Arrays.stream(star).collect(Collectors.toList());
                    Star star1 = new Star(id, Double.parseDouble(starArray.get(1)), Double.parseDouble(starArray.get(2)), Float.parseFloat(starArray.get(3)), starArray.get(0));
                    stars.add(star1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<Star> getAllStars(){
        return stars;
    }

    public int getNumberOfStars(){
        return stars.size();
    }
}
