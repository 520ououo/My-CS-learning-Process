package edu.hitsz.dao;

import java.io.*;
import java.util.*;

/**
 * 分数 DAO 实现类
 * 使用文件存储实现数据持久化，每个难度对应一个独立文件
 */
public class ScoreDAOImpl implements ScoreDAO {

    private static final String DATA_DIR = "scores";
    private static final String FILE_SUFFIX = ".dat";

    public ScoreDAOImpl() {
        createDataDirectory();
    }

    /**
     * 创建数据目录
     */
    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 获取指定难度的文件路径
     */
    private String getFilePath(String difficulty) {
        return DATA_DIR + File.separator + difficulty + FILE_SUFFIX;
    }

    @Override
    public void saveScore(ScoreRecord record) {
        String filePath = getFilePath(record.getDifficulty());
        File file = new File(filePath);

        List<ScoreRecord> records = loadRecords(file);
        records.add(record);
        records.sort(Collections.reverseOrder());

        saveRecords(file, records);
    }

    @Override
    public List<ScoreRecord> getScoresByDifficulty(String difficulty) {
        File file = new File(getFilePath(difficulty));
        List<ScoreRecord> records = loadRecords(file);
        records.sort(Collections.reverseOrder());
        return records;
    }

    @Override
    public List<ScoreRecord> getAllScores() {
        List<ScoreRecord> allRecords = new ArrayList<>();
        File dir = new File(DATA_DIR);

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(FILE_SUFFIX));
            if (files != null) {
                for (File file : files) {
                    allRecords.addAll(loadRecords(file));
                }
            }
        }

        allRecords.sort(Collections.reverseOrder());
        return allRecords;
    }

    @Override
    public void clearScores(String difficulty) {
        File file = new File(getFilePath(difficulty));
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 从文件加载记录
     */
    @SuppressWarnings("unchecked")
    private List<ScoreRecord> loadRecords(File file) {
        List<ScoreRecord> records = new ArrayList<>();

        if (!file.exists()) {
            return records;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                records = (List<ScoreRecord>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("读取分数文件失败: " + e.getMessage());
        }

        return records;
    }

    /**
     * 保存记录到文件
     */
    private void saveRecords(File file, List<ScoreRecord> records) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(records);
        } catch (IOException e) {
            System.err.println("保存分数文件失败: " + e.getMessage());
        }
    }
}
