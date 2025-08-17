package org.example.client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Scanner;

public class SimpleClient {

    public  static void main(String [] args) throws IOException {

        Path path= Path.of("file.txt");
        if(!Files.exists(path)){
            Files.createFile(path);

            Files.writeString(path, """
                    hello dear this file sent from client to you to test the ability of chnucking data \s 
                    and sending it from client to server. \s
                    Line 1: The quick brown fox jumps over the lazy dog. \s
                    Line 2: In a distant galaxy, stars danced in silent harmony. \s
                    Line 3: Clouds drifted lazily over the emerald hills. \s
                    Line 4: The old clock ticked, counting secrets in the air. \s
                    Line 5: Beneath the waves, coral whispered to passing fish. \s
                    Line 6: A paper boat sailed bravely into the storm drain. \s
                    Line 7: Moonlight spilled silver over the quiet streets. \s
                    Line 8: The kettle hummed a sleepy tune. \s
                    Line 9: Shadows stretched long across the empty park. \s
                    Line 10: A red balloon floated away into the horizon. \s
                    Line 11: Raindrops tapped softly on the dusty window. \s
                    Line 12: The cat blinked slowly, unimpressed by the chaos. \s
                    Line 13: Sunflowers turned their faces to the morning light. \s
                    Line 14: A train rumbled far in the distance. \s
                    Line 15: Frost painted lace on the old wooden fence. \s
                    Line 16: The library smelled of dust, paper, and dreams. \s
                    Line 17: A bicycle leaned against the chipped brick wall. \s
                    Line 18: The wind carried laughter from somewhere unseen. \s
                    Line 19: Pebbles crunched under worn leather boots. \s
                    Line 20: The candle flickered against the cold draft. \s
                    Line 21: An owl hooted, breaking the midnight silence. \s
                    Line 22: Leaves rustled in the quiet forest. \s
                    Line 23: A half-written letter lay forgotten on the desk. \s
                    Line 24: The ocean sighed with the weight of the tide. \s
                    Line 25: Footprints vanished slowly in the falling snow. \s
                    Line 26: A streetlamp buzzed under a swarm of moths. \s
                    Line 27: Time felt slower in the sleepy little town. \s
                    Line 28: The smell of bread drifted from the bakery. \s
                    Line 29: A wooden swing creaked in the empty playground. \s
                    Line 30: Somewhere far away, thunder rolled over the hills. \s
                
                    """, StandardOpenOption.APPEND);
        }


        try(Socket socket=new Socket("localhost",5000);
            PrintWriter output=new PrintWriter(socket.getOutputStream(),true);
            RandomAccessFile raf=new RandomAccessFile(path.toFile(),"r");
        ){

            String line;



            output.println(path);
                BufferedReader fileData=new BufferedReader(new FileReader(path.toString()),1024);

                while ((line=fileData.readLine())!=null){

                    output.println(line);
                }

        }
        catch (IOException ex){
            System.out.println("Client error occurred "+ ex.getMessage());
        }

    }
}
