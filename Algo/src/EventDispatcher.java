
class EventDispatcher implements Runnable {

    private Data data;
    private Algorithms algorithms;
    private int operationType;

    public EventDispatcher(Data d) {
        data = d;
        operationType = 0;
    }

    public void setOperationType(int t) { operationType = t; }

    @Override
    public void run() {
        algorithms = new Algorithms();
        switch(operationType) {
            case 1: //hull
            {
                data.setHull(algorithms.findConvexHull(data.getPoints()));
                data.getGraphicsPanel().setHullPaintFlag(true);
                data.getGraphicsPanel().setDrawPtFlag(true);
                data.getGraphicsPanel().repaint();
            }
            default:
                System.out.println("Type = " + operationType);
        }
        operationType = 0;
    }
}