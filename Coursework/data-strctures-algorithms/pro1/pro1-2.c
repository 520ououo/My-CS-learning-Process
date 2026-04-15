#include <cstdio>
#include <cstdlib>

int main() {
    int n;
    
    if (scanf("%d", &n) != 1) {
        return 0;
    }

    int* cards = (int*)malloc((n + 1) * sizeof(int));
    if (cards == NULL) return -1;

    for (int i = 1; i <= n; i++) {
        cards[i] = 1;
    }

    for (int i = 2; i <= n; i++) {
        for (int j = i; j <= n; j += i) {
            if (cards[j] == 1) {
                cards[j] = 0;
            } else {
                cards[j] = 1;
            }
        }
    }

    int count = 0;
    for (int i = 1; i <= n; i++) {
        if (cards[i] == 1) {
            printf("%d ", i);
            count++;
        }
    }
    
    printf("\n%d\n", count);

    free(cards);

    system("pause");

    return 0;
}