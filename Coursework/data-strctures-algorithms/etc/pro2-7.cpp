//反转字符串
#include <stdio.h>
#include <string.h>

void reverse(char *start, char *end) {
    while (start < end) {
        char temp = *start;
        *start = *end;
        *end = temp;
        start++;
        end--;
    }
}

int main() {
    char s[1000];
    int k, len, i;
    fgets(s, 1000, stdin);
    s[strcspn(s, "\n")] = '\0';
    scanf("%d", &k);
    len = strlen(s);
    for (i = 0; i < len; i += 2 * k) {
        if (i + k > len) {
            reverse(s + i, s + len - 1);
            break;
        } else {
            reverse(s + i, s + i + k - 1);
        }
    }
    printf("%s\n", s);
    return 0;
}