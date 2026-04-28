//有序数组的插入操作
#include <stdio.h>

int main() {
    int n, val, i, pos = 0;
    int nums[1000];
    scanf("%d", &n);
    for (i = 0; i < n; i++) {
        scanf("%d", &nums[i]);
    }
    scanf("%d", &val);
    
    while (pos < n && nums[pos] < val) {
        pos++;
    }
    
    for (i = n; i > pos; i--) {
        nums[i] = nums[i - 1];
    }
    nums[pos] = val;
    
    for (i = 0; i <= n; i++) {
        printf("%d", nums[i]);
        if (i < n) {
            printf(" ");
        }
    }
    return 0;
}