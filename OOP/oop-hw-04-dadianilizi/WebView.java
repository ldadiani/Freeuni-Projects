import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class WebView extends JFrame {

    private static final int DEFAULT_WORKERS = 4;
    private static final int STAT_COL = 1;

    private static final String LINKS = "links.txt";
    private static final String RUNNING_LABEL = "Running: ";
    private static final String COMPLETED_LABEL = "Completed: ";
    private static final String ELAPSED_LABEL = "Elapsed: ";

    private JLabel runLabel;
    private JLabel completeLabel;
    private JLabel elapseLabel;

    private JButton singleFetchButton;
    private JButton concurrentFetchButton;
    private JButton stopButton;

    private DefaultTableModel model;
    private JTextField ThreadsNumField;
    private JProgressBar progressBar;


    private final Object countWorkerLock;
    private final Object createWorkerLock;
    private Semaphore limitWorkerLock;

    private int numRunningWorkers;
    private int numCompleted;
    private long startTime;

    private Worker worker;
    private ArrayList<Thread> workers;

    public WebView() {
        this(LINKS);
    }

    public WebView(String filename) {
        countWorkerLock = new Object();
        createWorkerLock = new Object();
        numRunningWorkers = 0;
        numCompleted = 0;
        workers = new ArrayList<>();

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        initialTable(filename);
        initialStartButtons();
        initialFieldsAndLabels();
        initialProgressBar();
        initialStopButton();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void changeWorkerCount(int val) {
        synchronized (countWorkerLock) {
            numRunningWorkers += val;
            if (val < 0) numCompleted++;
        }
        SwingUtilities.invokeLater(() -> {
            runLabel.setText(RUNNING_LABEL + numRunningWorkers);
            completeLabel.setText(COMPLETED_LABEL + numCompleted);
            if (numRunningWorkers == 0) {
                setReadyState();
                elapseLabel.setText(ELAPSED_LABEL + (System.currentTimeMillis() - startTime) / 1000.0);
            }
        });
    }


    public void sendCompletionNotice() {
        changeWorkerCount(-1);
        limitWorkerLock.release();
        SwingUtilities.invokeLater(() -> progressBar.setValue(progressBar.getValue() + 1));
    }


    public void updateRow(final int row, final String msg) {
        SwingUtilities.invokeLater(() -> model.setValueAt(msg, row, STAT_COL));
    }

    public static void main(String[] args) {
        new WebView().setVisible(true);
    }

    private void initialTable(String file) {
        model = new DefaultTableModel(new String[]{"url", "status"}, 0);
        parseURLs(file);

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600, 300));
        add(scrollpane);
    }

    private void initialStartButtons() {
        singleFetchButton = new JButton("Single Thread Fetch");
        singleFetchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        singleFetchButton.addActionListener(e -> startWorker(1));

        concurrentFetchButton = new JButton("Concurrent Fetch");
        concurrentFetchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        concurrentFetchButton.addActionListener(e -> startWorker(Math.max(1, Integer.parseInt(ThreadsNumField.getText()))));

        add(singleFetchButton);
        add(concurrentFetchButton);
    }


    private void initialFieldsAndLabels() {
        ThreadsNumField = new JTextField(Integer.toString(DEFAULT_WORKERS));
        ThreadsNumField.setMaximumSize(new Dimension(100, ThreadsNumField.getHeight()));
        ThreadsNumField.setAlignmentX(Component.LEFT_ALIGNMENT);

        runLabel = new JLabel(RUNNING_LABEL);
        runLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        completeLabel = new JLabel(COMPLETED_LABEL);
        completeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        elapseLabel = new JLabel(ELAPSED_LABEL);
        elapseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(ThreadsNumField);
        add(runLabel);
        add(completeLabel);
        add(elapseLabel);
    }

    private void initialProgressBar() {
        progressBar = new JProgressBar(0, 10);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        add(progressBar);
    }

    private void initialStopButton() {
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        stopButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        stopButton.addActionListener(e -> {
            synchronized (createWorkerLock) {
                if (worker != null) {
                    worker.interrupt();
                    worker = null;
                }

                if (!workers.isEmpty()) {
                    for (Thread worker : workers) {
                        worker.interrupt();
                    }
                }
            }
        });

        add(stopButton);
    }

    private void parseURLs(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                model.addRow(new String[]{line, ""});
            }
        } catch (Exception ignored) {
        }
    }

    private void startWorker(int numWorkers) {
        resetInterface();
        limitWorkerLock = new Semaphore(numWorkers);
        worker = new Worker(this);
        startTime = System.currentTimeMillis();
        worker.start();
        setRunningState();
    }

    private void resetInterface() {
        numCompleted = 0;
        progressBar.setValue(0);
        progressBar.setMaximum(model.getRowCount());
        runLabel.setText(RUNNING_LABEL);
        completeLabel.setText(COMPLETED_LABEL);
        elapseLabel.setText(ELAPSED_LABEL);
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt("", i, STAT_COL);
        }
    }

    private void setReadyState() {
        singleFetchButton.setEnabled(true);
        concurrentFetchButton.setEnabled(true);
        stopButton.setEnabled(false);

        ThreadsNumField.setEnabled(true);
    }

    private void setRunningState() {
        singleFetchButton.setEnabled(false);
        concurrentFetchButton.setEnabled(false);
        stopButton.setEnabled(true);

        ThreadsNumField.setEnabled(false);
    }

    private class Worker extends Thread {

        private WebView view;

        public Worker(WebView view) {
            this.view = view;
        }

        @Override
        public void run() {
            changeWorkerCount(1);
            int numURLs = model.getRowCount();
            workers.clear();
            for (int i = 0; i < numURLs; i++) {
                try {
                    limitWorkerLock.acquire();
                } catch (InterruptedException e) {
                    break;
                }
                synchronized (createWorkerLock) {
                    Thread worker = new WebWorker((String) model.getValueAt(i, 0), i, view);
                    workers.add(worker);
                    worker.start();
                }
            }
            changeWorkerCount(-1);
        }
    }
}