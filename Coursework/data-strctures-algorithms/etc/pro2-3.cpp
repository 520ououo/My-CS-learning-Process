//验证回文串
#include <stdio.h>
#include <string.h>
#include <ctype.h>

int main() {
    char s[10000];
    char t[10000];
    int i = 0, j = 0;
    fgets(s, 10000, stdin);
    s[strcspn(s, "\n")] = 0;
    for (i = 0; s[i] != '\0'; i++) {
        if (isalnum(s[i])) {
            t[j++] = tolower(s[i]);
        }
    }
    t[j] = '\0';
    int left = 0, right = j - 1;
    int isPalindrome = 1;
    while (left < right) {
        if (t[left] != t[right]) {
            isPalindrome = 0;
            break;
        }
        left++;
        right--;
    }
    printf("%s\n", isPalindrome ? "true" : "false");
    return 0;
}