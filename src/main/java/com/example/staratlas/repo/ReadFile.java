package com.example.staratlas.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import com.example.staratlas.repo.StarService;

public class ReadFile {
    StarService starService = new StarService();

    public int[] number;
    public String[] name;
    public double[] ra;

    public double[] dec;
    public double[] color;

    public void readFromFile() {
        try {
            File file = new File("data\\data.txt");
            Scanner scanner = new Scanner(file);
            int count = 0;
            while (scanner.hasNextLine()) {
                count++;
                scanner.nextLine();
            }
            scanner.close();

            this.number = new int[count];
            this.name = new String[count];
            this.ra = new double[count];
            this.dec = new double[count];
            this.color = new double[count];

            scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                this.number[i] = Integer.parseInt(parts[0]);
                this.name[i] = parts[1];
                this.ra[i] = Double.parseDouble(parts[2]);
                this.dec[i] = Double.parseDouble(parts[3]);
                this.color[i] = Double.parseDouble(parts[4]);
                i++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /*public ArrayList<String> getInfoById(int id){
        return starService.getStarInfo(id);
    }*/
}