import java.util.concurrent.Semaphore;

/**
 * @author Masoud Dabbaghi
 * @version 1.0
 * @link https://github.com/masoudd2159/Unfair-Barber-Shop
 * @since 2019
 **/

public class UnfairBarberShop extends Thread {
    private static final int TIME_SLEEP = 2000;
    private static Semaphore maxCapacity;
    private static Semaphore sofa;
    private static Semaphore barberChair;
    private static Semaphore coord;
    private static Semaphore custReady;
    private static Semaphore leaveBarberChair;
    private static Semaphore payment;
    private static Semaphore receipt;
    private static Semaphore finished;

    private UnfairBarberShop() {
        maxCapacity = new Semaphore(20, true);
        sofa = new Semaphore(4, true);
        barberChair = new Semaphore(3, true);
        coord = new Semaphore(3, true);
        custReady = new Semaphore(0, true);
        leaveBarberChair = new Semaphore(0, true);
        payment = new Semaphore(0, true);
        receipt = new Semaphore(0, true);
        finished = new Semaphore(0, true);
    }

    class Customer extends Thread {
        int id;

        Customer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                maxCapacity.acquire();
                this.enterShop();
                sofa.acquire();
                this.sitOnSofa();
                barberChair.acquire();
                this.getUpFromSofa();
                sofa.release();
                this.sitOnBarberChair();
                custReady.release();
                finished.acquire();
                this.leaveBarberChairs();
                leaveBarberChair.release();
                this.pay();
                payment.release();
                receipt.acquire();
                this.exitShop();
                maxCapacity.release();
            } catch (
                    InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void enterShop() {
            try {
                System.out.println("Customer " + id + " Enter To Shop");
                sleep(TIME_SLEEP);
            } catch (InterruptedException ignored) {
            }
        }

        private void sitOnSofa() {
            try {
                System.out.println("Customer " + id + " Sit on Sofa");
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void getUpFromSofa() {
            try {
                System.out.println("Customer " + id + " Get Up From Sofa");
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void sitOnBarberChair() {
            try {
                System.out.println("Customer " + id + " Sit On Barber Chair");
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void leaveBarberChairs() {
            try {
                System.out.println("Customer " + id + " Leave Barber Chairs");
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void pay() {
            try {
                System.out.println("Customer " + id + " Pey Money");
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void exitShop() {
            try {
                System.out.println("Customer " + id + " Exit Shop");
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Barber extends Thread {
        int id;

        Barber(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    custReady.acquire();
                    coord.acquire();
                    cutHair();
                    coord.release();
                    finished.release();
                    leaveBarberChair.acquire();
                    barberChair.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void cutHair() {
            try {
                System.out.println("Barber " + id + " Cut Hair");
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Cashier extends Thread {

        int id;

        Cashier(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    payment.acquire();
                    coord.acquire();
                    acceptPay();
                    coord.release();
                    receipt.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void acceptPay() {
            try {
                System.out.println("Cashier " + id + " Accept Pay");
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        UnfairBarberShop unfairBarberShop = new UnfairBarberShop();
        unfairBarberShop.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            Customer customer = new Customer(i);
            customer.start();
        }

        for (int i = 0; i < 3; i++) {
            Barber barber = new Barber(i);
            barber.start();
        }

        for (int i = 0; i < 1; i++) {
            Cashier cashier = new Cashier(i);
            cashier.start();
        }
    }
}
