#include <stdio.h>
#include <stdlib.h>

// 信封结构体
typedef struct {
    int width;
    int height;
} Envelope;

// 比较函数：用于 qsort
// 宽度升序排；宽度相同时，高度降序排
int compare(const void* a, const void* b) {
    Envelope* e1 = (Envelope*)a;
    Envelope* e2 = (Envelope*)b;
    if (e1->width != e2->width) {
        return e1->width - e2->width;
    } else {
        return e2->height - e1->height;
    }
}

int maxEnvelopes(Envelope* envelopes, int n) {
    if (n == 0) return 0;

    // 1. 排序
    qsort(envelopes, n, sizeof(Envelope), compare);

    // 2. 在高度序列上寻找最长递增子序列 (LIS)
    // 使用动态规划 O(n^2) 实现（若 n 较大建议改用二分优化至 O(nlogn)）
    int* dp = (int*)malloc(n * sizeof(int));
    int max_len = 0;

    for (int i = 0; i < n; i++) {
        dp[i] = 1; // 初始长度为 1
        for (int j = 0; j < i; j++) {
            // 如果高度也递增，则可以嵌套
            if (envelopes[i].height > envelopes[j].height) {
                if (dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
        }
        if (dp[i] > max_len) {
            max_len = dp[i];
        }
    }

    free(dp);
    return max_len;
}

int main() {
    int n;
    if (scanf("%d", &n) != 1) return 0;
    
    Envelope* envelopes = (Envelope*)malloc(n * sizeof(Envelope));
    for (int i = 0; i < n; i++) {
        scanf("%d %d", &envelopes[i].width, &envelopes[i].height);
    }
    
    int result = maxEnvelopes(envelopes, n);
    printf("%d\n", result);
    
    free(envelopes);
    return 0;
}