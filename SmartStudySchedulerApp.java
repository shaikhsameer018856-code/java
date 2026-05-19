import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class SmartStudySchedulerApp extends JFrame {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("H:mm");
    private static final String[] COLS = { "Subject", "Start", "End", "Priority" };

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField subjectField, startField, endField;
    private JSpinner prioritySpinner;
    private JTextArea outputArea;

    public SmartStudySchedulerApp() {
        setTitle("Smart Study Scheduler — AOA Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        initComponents();
        buildUI();
        loadSampleData();
    }

    private void initComponents() {
        subjectField = new JTextField();
        startField = new JTextField("09:00");
        endField = new JTextField("10:00");
        prioritySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        tableModel = new DefaultTableModel(COLS, 0);
        table = new JTable(tableModel);
        outputArea = new JTextArea("Add study sessions and click \"Build Optimal Plan\".");

        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(224, 233, 247));
        table.getTableHeader().setForeground(new Color(27, 49, 79));

        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(new Color(246, 250, 255));
        outputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        root.setBackground(new Color(244, 247, 252));
        setContentPane(root);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(lbl("Smart Study Scheduler", 26, Font.BOLD, new Color(20, 44, 80)), BorderLayout.NORTH);
        header.add(lbl("Weighted Interval Scheduling  |  Dynamic Programming  |  AOA Project",
                13, Font.PLAIN, new Color(78, 94, 116)), BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildLeft(), buildRight());
        split.setResizeWeight(0.42);
        split.setDividerSize(10);
        split.setBorder(null);
        root.add(split, BorderLayout.CENTER);
    }

    private JPanel buildLeft() {
        JPanel panel = card();
        panel.add(lbl("Add Study Session", 17, Font.BOLD, new Color(20, 44, 80)), BorderLayout.NORTH);

        // input form
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 4, 5, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        String[] lbls = { "Subject", "Start (HH:mm)", "End (HH:mm)", "Priority (1-10)" };
        JComponent[] inputs = { subjectField, startField, endField, prioritySpinner };
        for (int i = 0; i < lbls.length; i++) {
            g.gridx = 0;
            g.gridy = i;
            g.weightx = 0.25;
            JLabel l = new JLabel(lbls[i]);
            l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            form.add(l, g);
            g.gridx = 1;
            g.weightx = 1;
            inputs[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            form.add(inputs[i], g);
        }

        JButton addBtn = btn("+ Add", new Color(27, 88, 205));
        JButton removeBtn = btn("X Remove", new Color(199, 65, 72));
        JButton sortBtn = btn("Sort Time", new Color(120, 80, 180));
        JButton clearBtn = btn("Clear All", new Color(130, 130, 130));
        JButton sampleBtn = btn("Sample Data", new Color(70, 123, 64));

        addBtn.addActionListener(e -> addSession());
        removeBtn.addActionListener(e -> removeSelected());
        sortBtn.addActionListener(e -> {
            sortByStartTime();
            outputArea.setText("Sorted by start time.");
        });
        clearBtn.addActionListener(e -> confirmClear());
        sampleBtn.addActionListener(e -> {
            loadSampleData();
            outputArea.setText("Sample sessions loaded.");
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        btnRow.setOpaque(false);
        for (JButton b : new JButton[] { addBtn, removeBtn, sortBtn, clearBtn, sampleBtn })
            btnRow.add(b);

        // problem description box
        JTextArea info = new JTextArea(
                "Problem: Students need to study multiple\n" +
                        "subjects but sessions overlap in time.\n\n" +
                        "Goal:\n" +
                        "  * Select non-overlapping sessions\n" +
                        "  * Maximize total priority score\n" +
                        "  * Suggest optimal study plan\n\n" +
                        "Algorithm: Weighted Interval Scheduling\n" +
                        "  Dynamic Programming — O(n log n)");
        info.setEditable(false);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setBackground(new Color(240, 245, 255));
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 230)),
                new EmptyBorder(8, 10, 8, 10)));

        JPanel center = new JPanel(new BorderLayout(8, 10));
        center.setOpaque(false);
        center.add(form, BorderLayout.NORTH);
        center.add(btnRow, BorderLayout.CENTER);
        center.add(info, BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildRight() {
        JPanel panel = card();
        panel.add(lbl("Sessions Table & Optimal Plan", 17, Font.BOLD, new Color(20, 44, 80)), BorderLayout.NORTH);

        JButton runBtn = btn("Build Optimal Plan", new Color(24, 104, 62));
        runBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        runBtn.addActionListener(e -> runPlanner());

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setOpaque(false);
        top.add(new JScrollPane(table), BorderLayout.CENTER);
        top.add(runBtn, BorderLayout.SOUTH);

        JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top,
                titledScroll("Planner Output", outputArea));
        vSplit.setResizeWeight(0.45);
        vSplit.setDividerSize(9);
        vSplit.setBorder(null);
        panel.add(vSplit, BorderLayout.CENTER);
        return panel;
    }

    private JPanel card() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 219, 232)),
                new EmptyBorder(12, 12, 12, 12)));
        p.setBackground(Color.WHITE);
        return p;
    }

    private JLabel lbl(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        return l;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(7, 11, 7, 11));
        return b;
    }

    private JScrollPane titledScroll(String title, JComponent c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createTitledBorder(title));
        return sp;
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addSession() {
        String sub = subjectField.getText().trim();
        String start = startField.getText().trim();
        String end = endField.getText().trim();
        if (sub.isEmpty()) {
            error("Subject is required.");
            return;
        }
        if (!validRange(start, end)) {
            error("Invalid time. Use HH:mm, ensure start < end.");
            return;
        }
        tableModel.addRow(new Object[] { sub, start, end, (Integer) prioritySpinner.getValue() });
        subjectField.setText("");
        sortByStartTime();
    }

    private void removeSelected() {
        int row = table.getSelectedRow();
        if (row >= 0)
            tableModel.removeRow(row);
        else
            error("Please select a row to remove.");
    }

    private void sortByStartTime() {
        int n = tableModel.getRowCount();
        List<Object[]> rows = new ArrayList<>();
        for (int i = 0; i < n; i++)
            rows.add(new Object[] { tableModel.getValueAt(i, 0), tableModel.getValueAt(i, 1),
                    tableModel.getValueAt(i, 2), tableModel.getValueAt(i, 3) });
        rows.sort((a, b) -> LocalTime.parse(a[1].toString(), FMT)
                .compareTo(LocalTime.parse(b[1].toString(), FMT)));
        tableModel.setRowCount(0);
        rows.forEach(tableModel::addRow);
    }

    private void confirmClear() {
        int ok = JOptionPane.showConfirmDialog(this, "Clear all sessions?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
            outputArea.setText("All sessions cleared.");
        }
    }

    private void loadSampleData() {
        Object[][] data = {
                { "Mathematics", "08:00", "09:30", 8 },
                { "Physics", "09:00", "10:30", 7 },
                { "Chemistry", "10:30", "12:00", 9 },
                { "Biology", "11:00", "12:30", 6 },
                { "English", "12:45", "13:45", 5 },
                { "Computer Science", "13:30", "15:00", 10 },
                { "History", "15:15", "16:15", 4 }
        };
        for (Object[] row : data)
            tableModel.addRow(row);
    }

    private void runPlanner() {
        if (tableModel.getRowCount() == 0) {
            error("Please add at least one session.");
            return;
        }

        List<Session> sessions = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String sub = tableModel.getValueAt(i, 0).toString().trim();
            String start = tableModel.getValueAt(i, 1).toString().trim();
            String end = tableModel.getValueAt(i, 2).toString().trim();
            int pri;
            try {
                pri = Integer.parseInt(tableModel.getValueAt(i, 3).toString().trim());
            } catch (NumberFormatException ex) {
                error("Priority must be a number at row " + (i + 1));
                return;
            }
            if (sub.isEmpty() || !validRange(start, end)) {
                error("Invalid data at row " + (i + 1));
                return;
            }
            sessions.add(new Session(sub, LocalTime.parse(start, FMT), LocalTime.parse(end, FMT), pri));
        }

        Result result = solve(sessions);
        outputArea.setText(buildReport(sessions, result));
        outputArea.setCaretPosition(0);
    }

    private Result solve(List<Session> sessions) {
        List<Session> s = new ArrayList<>(sessions);
        s.sort(Comparator.comparing(Session::end).thenComparing(Session::start));
        int n = s.size();

        // precompute end times in minutes for binary search
        int[] endMin = new int[n];
        for (int i = 0; i < n; i++)
            endMin[i] = toMins(s.get(i).end());

        int[] prev = new int[n];
        Arrays.fill(prev, -1);
        for (int i = 0; i < n; i++) {
            int startMin = toMins(s.get(i).start());
            int lo = 0, hi = i - 1, ans = -1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (endMin[mid] <= startMin) {
                    ans = mid;
                    lo = mid + 1;
                } else
                    hi = mid - 1;
            }
            prev[i] = ans;
        }

        int[] dp = new int[n];
        int[] cnt = new int[n];
        boolean[] pick = new boolean[n];
        for (int i = 0; i < n; i++) {
            int withScore = s.get(i).priority() + (prev[i] >= 0 ? dp[prev[i]] : 0);
            int withCount = 1 + (prev[i] >= 0 ? cnt[prev[i]] : 0);
            int skipScore = i > 0 ? dp[i - 1] : 0;
            int skipCount = i > 0 ? cnt[i - 1] : 0;
            if (withScore > skipScore || (withScore == skipScore && withCount > skipCount)) {
                dp[i] = withScore;
                cnt[i] = withCount;
                pick[i] = true;
            } else {
                dp[i] = skipScore;
                cnt[i] = skipCount;
            }
        }

        List<Session> chosen = new ArrayList<>();
        for (int i = n - 1; i >= 0;) {
            if (pick[i]) {
                chosen.add(s.get(i));
                i = prev[i];
            } else
                i--;
        }
        Collections.reverse(chosen);
        return new Result(s, chosen, dp[n - 1], cnt[n - 1]);
    }

    private List<String> findOverlaps(List<Session> sessions) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < sessions.size(); i++) {
            for (int j = i + 1; j < sessions.size(); j++) {
                Session a = sessions.get(i), b = sessions.get(j);
                if (toMins(a.start()) < toMins(b.end()) && toMins(b.start()) < toMins(a.end()))
                    list.add(String.format("  %-18s (%s-%s)  overlaps  %-18s (%s-%s)",
                            a.subject(), a.start().format(FMT), a.end().format(FMT),
                            b.subject(), b.start().format(FMT), b.end().format(FMT)));
            }
        }
        return list;
    }

    private String buildReport(List<Session> original, Result r) {
        StringBuilder sb = new StringBuilder();

        sb.append("=============================================================\n");
        sb.append("       SMART STUDY SCHEDULER  —  AOA PROJECT REPORT\n");
        sb.append("=============================================================\n\n");

        sb.append("1. PROBLEM DESCRIPTION\n");
        sb.append("----------------------\n");
        sb.append("Students need to study multiple subjects before exams.\n");
        sb.append("Each subject has a fixed time slot and a priority level.\n");
        sb.append("Challenge: Sessions overlap — only one can be studied at a time.\n");
        sb.append("We must select the best non-overlapping set of sessions.\n\n");
        sb.append("  Input  : ").append(original.size()).append(" sessions (subject, start, end, priority)\n");
        sb.append("  Output : Non-overlapping subset with maximum total priority\n\n");

        List<String> overlaps = findOverlaps(original);
        sb.append("2. OVERLAPPING CONSTRAINTS\n");
        sb.append("--------------------------\n");
        if (overlaps.isEmpty()) {
            sb.append("  No overlapping sessions found.\n");
        } else {
            sb.append("  Found ").append(overlaps.size()).append(" conflict(s):\n");
            overlaps.forEach(o -> sb.append(o).append("\n"));
        }
        sb.append("\n");

        sb.append("3. PROBLEM MAPPING\n");
        sb.append("------------------\n");
        sb.append("  Mapped To  : Weighted Interval Scheduling Problem (WISP)\n");
        sb.append("  Approach   : Dynamic Programming\n");
        sb.append("  Reason     : Each session = interval [start, end] with weight = priority\n");
        sb.append("               Goal = max weight non-overlapping subset — exact match\n\n");
        sb.append("  Alternatives Considered:\n");
        sb.append("  * Greedy (max sessions)      — ignores priority weights         [rejected]\n");
        sb.append("  * Interval Partitioning      — assigns rooms, no selection      [rejected]\n");
        sb.append("  * WISP with DP  (selected)   — handles priorities correctly     [chosen]\n\n");

        sb.append("4. OPTIMAL STUDY PLAN\n");
        sb.append("---------------------\n");
        int totalMins = 0;
        for (int i = 0; i < r.selected().size(); i++) {
            Session s = r.selected().get(i);
            int dur = toMins(s.end()) - toMins(s.start());
            totalMins += dur;
            sb.append(String.format("  %d) %-20s %s - %s   priority=%2d   (%d min)\n",
                    i + 1, s.subject(), s.start().format(FMT), s.end().format(FMT), s.priority(), dur));
        }
        sb.append("\n  Sessions Selected : ").append(r.selectedCount())
                .append(" / ").append(original.size()).append("\n");
        sb.append("  Total Priority    : ").append(r.totalScore()).append("\n");
        sb.append("  Total Study Time  : ").append(totalMins / 60).append("h ")
                .append(totalMins % 60).append("min\n\n");

        // skipped sessions
        Set<String> keys = new HashSet<>();
        r.selected().forEach(s -> keys.add(s.subject() + s.start()));
        sb.append("  Skipped (overlap or lower priority):\n");
        boolean any = false;
        for (Session s : original) {
            if (!keys.contains(s.subject() + s.start())) {
                sb.append(String.format("  X %-20s %s - %s   priority=%d\n",
                        s.subject(), s.start().format(FMT), s.end().format(FMT), s.priority()));
                any = true;
            }
        }
        if (!any)
            sb.append("  None — all sessions selected!\n");
        sb.append("\n");

        // 5. Complexity Analysis
        sb.append("5. COMPLEXITY ANALYSIS\n");
        sb.append("----------------------\n");
        sb.append("  Step 1 — Sort by end time             : O(n log n)\n");
        sb.append("  Step 2 — Binary search for prev[]     : O(n log n)\n");
        sb.append("  Step 3 — DP table fill                : O(n)\n");
        sb.append("  Step 4 — Backtrack & reconstruct      : O(n)\n");
        sb.append("  ----------------------------------------\n");
        sb.append("  Total Time Complexity                  : O(n log n)\n");
        sb.append("  Space Complexity                       : O(n)\n");

        return sb.toString();
    }

    private int toMins(LocalTime t) {
        return t.getHour() * 60 + t.getMinute();
    }

    private boolean validRange(String s, String e) {
        try {
            return LocalTime.parse(s, FMT).isBefore(LocalTime.parse(e, FMT));
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private record Session(String subject, LocalTime start, LocalTime end, int priority) {
    }

    private record Result(List<Session> sorted, List<Session> selected, int totalScore, int selectedCount) {
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new SmartStudySchedulerApp().setVisible(true));
    }
}