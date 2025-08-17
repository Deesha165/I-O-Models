package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//        Consumer<ByteBuffer>printBuffer=(buffer)->{
//          byte[] data=new byte[buffer.limit()] ;
//          buffer.get(data);
//            System.out.printf("\"%s\" ",new String(data, StandardCharsets.UTF_8));
//        };
//        ByteBuffer buffer=ByteBuffer.allocate(1024);
//        doOperation("Print: ",buffer,(b)-> System.out.println(b+ " "));
//
//        doOperation("Write: ",buffer,b->b.put("This is a Test".getBytes()));
//
//        doOperation("Flip (from Write to Read): ",buffer,ByteBuffer::flip);
//        doOperation("1- Read and Print value: ",buffer,printBuffer);


    }
    private static void doOperation(String op, ByteBuffer buffer, Consumer<ByteBuffer> c){
        System.out.printf("%-30s",op);
        c.accept(buffer);
        System.out.printf("Capacity= %d, Limit= %d,Position=%d, Remaining=%d%n"
                ,buffer.capacity(),
                buffer.limit(),
                buffer.position(),
                buffer.remaining()
                );
    }
}