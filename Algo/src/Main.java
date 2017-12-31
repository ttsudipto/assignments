class Main {
    public static void main(String[] args) {

        Data data = new Data();
        EventDispatcher dispatcher = new EventDispatcher(data);
        MainWindow win = new MainWindow(data.getGraphicsPanel(), dispatcher);
    }
}