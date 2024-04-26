import java.util.LinkedList;
import java.util.List;
import java.math.BigInteger;

public class JMCTestApplication {
	record Lockable(String name){}

    public static void main(String[] args) throws InterruptedException {	
		Lockable a = new Lockable("a");
		Lockable b = new Lockable("b");
		Lockable c = new Lockable("c");
    
        Thread thread1 = new Thread(new Runnable(){
			public void run(){
				int primeNumberCap = 500000;
				String name = Thread.currentThread().getName();
				System.out.println("Calculating prime numbers up to: " + primeNumberCap);
				System.out.println("Using thread: " + name);
				List<Integer> primeNumbers = findPrimeNumbrs(primeNumberCap);
				System.out.println("Finished calculating prime numbers");
				System.out.println("Prime numbers found: " + primeNumbers.size());
			}
		}, "thread1");
        Thread thread2 = new Thread(new Runnable(){
			public void run(){
				lockThreads(a, b);
			}
		}, "thread2");
        Thread thread3 = new Thread(new Runnable(){
			public void run(){
				lockThreads(b, c);
			}
		}, "thread3");
        Thread thread4 = new Thread(new Runnable(){
			public void run(){
				lockThreads(c, a);
			}
		}, "thread4");
        thread1.start();
        thread2.start();
        Thread.sleep(3000);
        thread3.start();
        Thread.sleep(3000);
		thread4.start();
    }
	
	public static List<Integer> findPrimeNumbrs(int n) {
	    List<Integer> primeNumbers = new LinkedList<>();
	    for (int i = 2; i <= n; i++) {
	        if (isPrimeNumber(i)) {
	            primeNumbers.add(i);
	        }
	    }
	    return primeNumbers;
	}
	public static boolean isPrimeNumber(int number) {
	    for (int i = 2; i < number; i++) {
	        if (number % i == 0) {
	            return false;
	        }
	    }
	    return true;
	}
    private static void work() {
        try {
		  BigInteger sum = new BigInteger("0");
		  for (long i = 0L; i < 100000000L; i++){
			 sum = sum.add(BigInteger.valueOf(i));
		  }
		  System.out.println("Summed value:" + sum.toString());
          Thread.sleep(10000);
        } catch (InterruptedException e) {
		System.out.println("here");
            e.printStackTrace();
        }
    }
	
	private static void lockThreads(Lockable lock1, Lockable lock2){
    	String name = Thread.currentThread().getName();
    	System.out.println(name + " acquiring first lock on "+lock1.name());
    	synchronized (lock1) {
     		System.out.println(name + " acquired first lock on "+lock1.name());
     	   	work();
     		System.out.println(name + " acquiring second lock on "+lock2.name());
     	   	synchronized (lock2) {
        		System.out.println(name + " acquired second lock on "+lock2.name());
        		work();
    	    }
     	    System.out.println(name + " released second lock on "+lock2.name());
    	}
    	System.out.println(name + " released first lock on "+lock1.name());
   	 	System.out.println(name + " finished execution.");
	}
}

