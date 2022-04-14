package com.wondersgroup.test.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class TestCompletableFuture {
	
	@Test
    public void test3() throws Exception {
        // 创建异步执行任务:
        ExecutorService executorService= Executors.newSingleThreadExecutor();
		Future<Double> cf = executorService.submit(()->{
            System.out.println(Thread.currentThread()+" start,time->"+System.currentTimeMillis());
            Thread.sleep(2000);
            System.out.println(Thread.currentThread()+" exit,time->"+System.currentTimeMillis());
            //throw new RuntimeException("测试抛出异常");
            return 1.2;
        });
        System.out.println("main thread start,time->"+System.currentTimeMillis());
        //等待子任务执行完成,如果已完成则直接返回结果
        //如果执行任务异常，则get方法会把之前捕获的异常重新抛出
        System.out.println("run result->"+cf.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }
	
	
	
    @Test
    public void test2() throws Exception {
        // 创建异步执行任务，有返回值
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread()+" start,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        	//throw new RuntimeException("测试抛出异常");
            System.out.println(Thread.currentThread()+" exit,time->"+System.currentTimeMillis());
            return 1.2;
        });
        System.out.println("main thread start,time->"+System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("run result->"+cf.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }
 
   @Test
    public void test4() throws Exception {
        // 创建异步执行任务，无返回值
        CompletableFuture<Void> cf = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread()+" start,time->"+System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            //throw new RuntimeException("测试抛出异常");
            System.out.println(Thread.currentThread()+" exit,time->"+System.currentTimeMillis());
        });
        System.out.println("main thread start,time->"+System.currentTimeMillis());
        //等待子任务执行完成
        System.out.println("run result->"+cf.get());
        System.out.println("main thread exit,time->"+System.currentTimeMillis());
    }
   
   
   
   @Test
   public void test12() throws Exception {
       ForkJoinPool pool=new ForkJoinPool();
       // 创建异步执行任务:
       CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
           System.out.println(Thread.currentThread()+" start,time->"+System.currentTimeMillis());
           try {
               Thread.sleep(2000);
           } catch (InterruptedException e) {
           }
           //throw new RuntimeException("测试抛出异常");
           System.out.println(Thread.currentThread()+" exit,time->"+System.currentTimeMillis());
           return 1.2;
       },pool);
       System.out.println("main thread start,time->"+System.currentTimeMillis());
       //等待子任务执行完成
       System.out.println("run result->"+cf.get());
       System.out.println("main thread exit,time->"+System.currentTimeMillis());
   }

   @Test
   public void test14() throws Exception {
       ExecutorService executorService= Executors.newSingleThreadExecutor();
       // 创建异步执行任务:
       CompletableFuture<Void> cf = CompletableFuture.runAsync(()->{
           System.out.println(Thread.currentThread()+" start,time->"+System.currentTimeMillis());
           try {
               Thread.sleep(2000);
           } catch (InterruptedException e) {
           }
           //throw new RuntimeException("测试抛出异常");
           System.out.println(Thread.currentThread()+" exit,time->"+System.currentTimeMillis());
       },executorService);
       System.out.println("main thread start,time->"+System.currentTimeMillis());
       //等待子任务执行完成
       System.out.println("run result->"+cf.get());
       System.out.println("main thread exit,time->"+System.currentTimeMillis());
   }
   
   
   
   
   
   @Test
   public void asyncThread3()throws Exception{
	   CompletableFuture<String> a = CompletableFuture.supplyAsync(() -> "hello");
	   CompletableFuture<String> b = CompletableFuture.supplyAsync(() -> "youth");
	   CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> "!");
	   CompletableFuture<Void> all = CompletableFuture.allOf(a,b,c);
	   all.get();
	   String result = Stream.of(a, b,c)
	   .map(CompletableFuture::join)
	   .collect(Collectors.joining(" "));
	   System.out.println(result);

   }
   
   @Test
   public void testFutureDemo()  {
       Long start = System.currentTimeMillis();
       //开启多线程
       ExecutorService exs = Executors.newFixedThreadPool(10);
       try {
           //结果集
           List<Integer> list = new ArrayList<Integer>();
           List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
           //1.高速提交10个任务，每个任务返回一个Future入list
           for(int i=0;i<10;i++){
               futureList.add(exs.submit(new CallableTask(i+1)));
           }
           Long getResultStart = System.currentTimeMillis();
           System.out.println("结果归集开始时间="+new Date());
           //2.结果归集，遍历futureList,高速轮询（模拟实现了并发）获取future状态成功完成后获取结果，退出当前循环
           for (Future<Integer> future : futureList) {
               while (true) {//CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                   if (future.isDone()&& !future.isCancelled()) {//获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。
                       Integer i = future.get();//获取结果
                       System.out.println("任务i="+i+"获取完成!"+new Date());
                       list.add(i);
                       break;//当前future获取结果完毕，跳出while
                   } else {
                       Thread.sleep(0);//每次轮询休息1毫秒（CPU纳秒级），避免CPU高速轮循耗空CPU---》新手别忘记这个
                   }
               }
           }
           System.out.println("list="+list);
           System.out.println("总耗时="+(System.currentTimeMillis()-start)+",取结果归集耗时="+(System.currentTimeMillis()-getResultStart));
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           exs.shutdown();
       }
   }
   static class CallableTask implements Callable<Integer>{
       Integer i;
       
       public CallableTask(Integer i) {
           super();
           this.i=i;
       }

       @Override
       public Integer call() throws Exception {
           if(i==1){
               Thread.sleep(3000);//任务1耗时3秒
           }else if(i==5){
               Thread.sleep(5000);//任务5耗时5秒
           }else{
               Thread.sleep(1000);//其它任务耗时1秒
           }
           System.out.println("task线程："+Thread.currentThread().getName()+"任务i="+i+",完成！");  
           return i;
       }
   }
   
   
   
   
   
   
   /**
    * 多线程并发任务,取结果归集
    */
   @Test
   public void CompletableFutureDemo () {
       Long start = System.currentTimeMillis();
       //结果集
       List<String> list = new ArrayList<String>();
       List<String> list2 = new ArrayList<String>();
       //定长10线程池
       ExecutorService exs = Executors.newFixedThreadPool(10);
       //List<CompletableFuture<String>> futureList = new ArrayList<>();
       List<Integer> taskList = Arrays.asList(2,1,3,4,5,6,7,8,9,10);
       try {
           //方式一：循环创建CompletableFuture list,调用sequence()组装返回一个有返回值的CompletableFuture，返回结果get()获取
//           for(int i=0;i<taskList.size();i++){
//               final int j=i+1;
//               CompletableFuture<String> future = CompletableFuture.supplyAsync(()->calc(j), exs)//异步执行
//                       .thenApply(e->Integer.toString(e))//Integer转换字符串    thenAccept只接受不返回不影响结果
//                       .whenComplete((v, e) -> {//如需获取任务完成先手顺序，此处代码即可
//                           System.out.println("任务"+v+"完成!result="+v+"，异常 e="+e+","+new Date());
//                           list2.add(v);
//                       })
//                       ;
//               futureList.add(future);
//           }
//           //流式获取结果
//           list = sequence(futureList).get();//[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]此处不理解为什么是这样的顺序？谁知道求告知
           
           //方式二：全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
           @SuppressWarnings("rawtypes")
           CompletableFuture[] cfs = taskList.stream().map(object-> CompletableFuture.supplyAsync(()->calc(object), exs)
                   .thenApply(h->Integer.toString(h))
                   .whenComplete((v, e) -> {//如需获取任务完成先手顺序，此处代码即可
                       System.out.println("任务"+v+"完成!result="+v+"，异常 e="+e+","+new Date());
                       list2.add(v);
                   }))
                   .toArray(CompletableFuture[]::new);
           CompletableFuture.allOf(cfs).join();//封装后无返回值，必须自己whenComplete()获取
           System.out.println("list2="+list2+"list="+list+",耗时="+(System.currentTimeMillis()-start));
       } catch (Exception e) {
           e.printStackTrace();
       }finally {
           exs.shutdown();
       }
   }
   
   public static Integer calc(Integer i){
       try {
           if(i==1){
               Thread.sleep(3000);//任务1耗时3秒
           }else if(i==5){
               Thread.sleep(5000);//任务5耗时5秒
           }else{
               Thread.sleep(1000);//其它任务耗时1秒
           }
           System.out.println("task线程："+Thread.currentThread().getName()+"任务i="+i+",完成！+"+new Date());  
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       return i;
   }



}
