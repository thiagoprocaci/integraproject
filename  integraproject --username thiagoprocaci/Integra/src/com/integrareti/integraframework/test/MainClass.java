package com.integrareti.integraframework.test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainClass {
  ExecutorService executor = Executors.newFixedThreadPool(3);

  public void start() throws IOException {
    int i=0;
    while (i<100){
      executor.submit(new MyThread(i++));
    }
    try {
		shutdown();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
  }

  public void shutdown() throws InterruptedException {
    executor.shutdown();
  }

  public static void main(String argv[]) throws Exception {
   MainClass mc =   new MainClass();
   mc.start();
   
  }
}

class MyThread implements Runnable {
private int i;
  MyThread(int i) {
  this.i = i;
  }

  public void run() {
    System.out.println("I am in thread:"+i);
    
  }
}
