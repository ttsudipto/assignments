package project.pc.bluetooth;

public class BtServerThread implements Runnable {

    private BtServer mServer;

    public BtServerThread() { mServer = new BtServer(); }

    @Override
    public void run() {
        mServer.start();
        mServer.listen();
    }

    public BtServer getServer() { return mServer; }
}
