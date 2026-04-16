package edu.hitsz.dao;

import java.util.List;

/**
 * 分数数据访问对象接口（DAO Pattern）
 * 定义排行榜数据的持久化操作
 */
public interface ScoreDAO {

    /**
     * 保存分数记录到文件
     * @param record 分数记录
     */
    void saveScore(ScoreRecord record);

    /**
     * 获取指定难度下的所有分数记录
     * @param difficulty 难度等级
     * @return 分数记录列表（已按分数降序排序）
     */
    List<ScoreRecord> getScoresByDifficulty(String difficulty);

    /**
     * 获取所有分数记录
     * @return 分数记录列表
     */
    List<ScoreRecord> getAllScores();

    /**
     * 清空指定难度下的所有记录
     * @param difficulty 难度等级
     */
    void clearScores(String difficulty);
}
