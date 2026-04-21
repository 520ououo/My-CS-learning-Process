package edu.hitsz.application;

import edu.hitsz.dao.ScoreDAO;
import edu.hitsz.dao.ScoreDAOImpl;
import edu.hitsz.dao.ScoreRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 排行榜GUI窗口
 */
public class LeaderboardWindow extends JFrame {

    private ScoreDAO scoreDAO;
    private String difficulty;
    private JTable scoreTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;

    public LeaderboardWindow(String playerName, int score, long duration, String difficulty) {
        this.difficulty = difficulty;
        this.scoreDAO = new ScoreDAOImpl();

        setTitle("游戏结束 - 排行榜");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.add(new JLabel("玩家姓名:"));
        nameField = new JTextField(playerName, 15);
        inputPanel.add(nameField);

        JButton saveButton = new JButton("保存记录");
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入玩家姓名!", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ScoreRecord record = new ScoreRecord(name, score, duration, difficulty);
            scoreDAO.saveScore(record);
            refreshTable();
            JOptionPane.showMessageDialog(this, "记录已保存!", "成功", JOptionPane.INFORMATION_MESSAGE);
        });
        inputPanel.add(saveButton);

        JButton deleteButton = new JButton("删除选中记录");
        deleteButton.addActionListener(e -> {
            int selectedRow = scoreTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择要删除的记录!", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "确定删除选中的记录吗?", "确认删除", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                scoreDAO.deleteScore(difficulty, selectedRow);
                refreshTable();
            }
        });
        inputPanel.add(deleteButton);

        JButton clearButton = new JButton("清空所有记录");
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "确定清空所有记录吗?", "确认清空", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                scoreDAO.clearScores(difficulty);
                refreshTable();
            }
        });
        inputPanel.add(clearButton);

        add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"排名", "玩家名", "难度", "分数", "用时"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scoreTable = new JTable(tableModel);
        scoreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scoreTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        scoreTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("排行榜 - 难度: " + difficulty, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titleLabel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<ScoreRecord> records = scoreDAO.getScoresByDifficulty(difficulty);

        int rank = 1;
        for (ScoreRecord record : records) {
            tableModel.addRow(new Object[]{
                    rank,
                    record.getPlayerName(),
                    record.getDifficulty(),
                    record.getScore(),
                    record.getFormattedTime()
            });
            rank++;
        }
    }
}

