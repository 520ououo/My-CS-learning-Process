//判断子串
#include <stdio.h>
#include <string.h>

int main() {
    char A[80], B[80];
    fgets(A, 80, stdin);
    fgets(B, 80, stdin);
    A[strcspn(A, "\n")] = '\0';
    B[strcspn(B, "\n")] = '\0';
    
    if (strstr(A, B) != NULL) {
        printf("Yes\n");
    } else {
        printf("No\n");
    }
    return 0;
}