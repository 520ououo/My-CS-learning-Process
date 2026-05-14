#include <stdio.h>
#include <string.h>

#define MAXN 205
#define MAXC 505

// 结构体存储边
typedef struct {
    int u, v, w;
} Edge;

int main() {
    int N, M, C;
    int S, T;
    int values[MAXN];
    Edge edges[1005];
    int dp[MAXN][MAXC];

    // 读取 N, M, C
    if (scanf("%d %d %d", &N, &M, &C) != 3) return 0;
    // 读取起点 S 和终点 T
    scanf("%d %d", &S, &T);
    // 读取顶点价值
    for (int i = 1; i <= N; i++) {
        scanf("%d", &values[i]);
    }
    // 读取边信息
    for (int i = 0; i < M; i++) {
        scanf("%d %d %d", &edges[i].u, &edges[i].v, &edges[i].w);
    }

    // 初始化 DP 表，-1 表示不可达
    memset(dp, -1, sizeof(dp));

    // 初始状态：起点 S 的价值，成本为 0
    dp[S][0] = values[S];

    /* 
       由于是 DAG，可以按拓扑序更新，
       或者简单地进行 C 次迭代（类似背包）或利用其无环特性。
       这里利用 dp[v][curr_c + w] 依赖于 dp[u][curr_c] 的特性。
    */
    for (int cost = 0; cost <= C; cost++) {
        for (int i = 0; i < M; i++) {
            int u = edges[i].u;
            int v = edges[i].v;
            int w = edges[i].w;

            // 如果当前成本下 u 可达，且 u->v 不超过总成本上限
            if (dp[u][cost] != -1 && cost + w <= C) {
                int next_val = dp[u][cost] + values[v];
                if (next_val > dp[v][cost + w]) {
                    dp[v][cost + w] = next_val;
                }
            }
        }
    }

    // 在终点 T 寻找所有满足成本限制 (<=C) 的最大价值
    int max_value = -1;
    for (int c = 0; c <= C; c++) {
        if (dp[T][c] > max_value) {
            max_value = dp[T][c];
        }
    }

    // 输出结果
    printf("%d\n", max_value);

    return 0;
}