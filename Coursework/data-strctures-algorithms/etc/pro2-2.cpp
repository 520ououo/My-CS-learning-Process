//逆波兰式求值
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAXSIZE 1000

int main() {
    int n, top = -1;
    int stack[MAXSIZE];
    char token[20];
    scanf("%d", &n);
    for (int i = 0; i < n; i++) {
        scanf("%s", token);
        if (strcmp(token, "+") == 0 || strcmp(token, "-") == 0 ||
            strcmp(token, "*") == 0 || strcmp(token, "/") == 0) {
            int b = stack[top--];
            int a = stack[top--];
            int res;
            if (strcmp(token, "+") == 0) {
                res = a + b;
            } else if (strcmp(token, "-") == 0) {
                res = a - b;
            } else if (strcmp(token, "*") == 0) {
                res = a * b;
            } else {
                res = a / b;
            }
            stack[++top] = res;
        } else {
            stack[++top] = atoi(token);
        }
    }
    printf("%d\n", stack[0]);
    return 0;
}