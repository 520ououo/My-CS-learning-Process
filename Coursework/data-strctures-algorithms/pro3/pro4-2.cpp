#include <stdio.h>
#include <string.h>

// 根据题目通常的数据范围设定，若 C 很大需按需调整
#define MAXC 10005 

// 求最大值的宏
#define MAX(a, b) ((a) > (b) ? (a) : (b))

int main() {
    int C, n;
    // dp[j] 表示在时间 j 内能获得的最大价值
    long long dp[MAXC]; 

    // 读取总时间 C 和草药数目 n
    if (scanf("%d %d", &C, &n) != 2) return 0;

    // 初始化 dp 数组为 0
    memset(dp, 0, sizeof(dp));

    for (int i = 0; i < n; i++) {
        int cost, weight;
        scanf("%d %d", &cost, &weight);

        // 完全背包的核心：正向遍历时间
        // 这样在计算 dp[j] 时，dp[j - cost] 已经可能包含过第 i 种草药
        for (int j = cost; j <= C; j++) {
            dp[j] = MAX(dp[j], dp[j - cost] + weight);
        }
    }

    // 输出在规定时间内可以采到的最大总价值
    printf("%lld\n", dp[C]);

    return 0;
}