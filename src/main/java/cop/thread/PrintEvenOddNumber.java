package cop.thread;

public class PrintEvenOddNumber {

    public static void main(String[] args) throws InterruptedException {

        Printer printer = new Printer(20);

        Thread oddThread = new Thread(() -> {
            try {
                printer.printOdd();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread evenThread = new Thread(() -> {
            try {
                printer.printEven();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        oddThread.start();
        evenThread.start();

        oddThread.join();
        evenThread.join();

        System.out.println("\nCompleted");
    }
}

class Printer {

    private final int limit;
    private int counter = 1;
    private final Object lock = new Object();
    private boolean oddTurn = true;

    public Printer(int limit) {
        this.limit = limit;
    }

    public void printOdd() throws InterruptedException {
        synchronized (lock) {
            while (counter <= limit) {

                while (!oddTurn) {
                    lock.wait();
                }

                if (counter > limit) break;

                System.out.println("Odd Thread: " + counter);
                counter++;
                oddTurn = false;

                lock.notifyAll();
            }
        }
    }

    public void printEven() throws InterruptedException {
        synchronized (lock) {
            while (counter <= limit) {

                while (oddTurn) {
                    lock.wait();
                }

                if (counter > limit) break;

                System.out.println("Even Thread: " + counter);
                counter++;
                oddTurn = true;

                lock.notifyAll();
            }
        }
    }
}