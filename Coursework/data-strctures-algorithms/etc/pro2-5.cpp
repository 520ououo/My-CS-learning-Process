//括号匹配
#include <stdio.h>
#include <string.h>

#define MAXSIZE 1000

int main() {
    char s[MAXSIZE];
    char stack[MAXSIZE];
    int top = -1;
    int i, len, valid = 1;

    fgets(s, MAXSIZE, stdin);
    len = strlen(s);
    if (s[len-1] == '\n') {
        s[len-1] = '\0';
        len--;
    }

    for (i = 0; i < len; i++) {
        if (s[i] == '(' || s[i] == '[' || s[i] == '{') {
            stack[++top] = s[i];
        } else {
            if (top == -1) {
                valid = 0;
                break;
            }
            char topChar = stack[top--];
            if ((s[i] == ')' && topChar != '(') ||
                (s[i] == ']' && topChar != '[') ||
                (s[i] == '}' && topChar != '{')) {
                valid = 0;
                break;
            }
        }
    }

    if (valid && top == -1) {
        printf("True\n");
    } else {
        printf("False\n");
    }
    return 0;
}