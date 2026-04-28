//二叉树的镜像核计数
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct TreeNode {
    int val;
    struct TreeNode *left;
    struct TreeNode *right;
};

struct TreeNode* buildTree(char* s[], int* idx) {
    if(strcmp(s[*idx], "#") == 0){
        (*idx)++;
        return NULL;
    }
    struct TreeNode* root = (struct TreeNode*)malloc(sizeof(struct TreeNode));
    root->val = atoi(s[*idx]);
    (*idx)++;
    root->left = buildTree(s, idx);
    root->right = buildTree(s, idx);
    return root;
}

int checkSymmetric(struct TreeNode* a, struct TreeNode* b) {
    if(a == NULL && b == NULL) return 1;
    if(a == NULL || b == NULL) return 0;
    return (a->val == b->val) && checkSymmetric(a->left, b->right) && checkSymmetric(a->right, b->left);
}

int countSymmetric(struct TreeNode* root) {
    if(root == NULL) return 0;
    int res = checkSymmetric(root->left, root->right) ? 1 : 0;
    res += countSymmetric(root->left);
    res += countSymmetric(root->right);
    return res;
}

int main() {
    char* arr[1000];
    char str[10000];
    fgets(str, sizeof(str), stdin);
    str[strcspn(str, "\n")] = 0;
    int cnt = 0;
    char* p = strtok(str, " ");
    while(p != NULL) {
        arr[cnt++] = p;
        p = strtok(NULL, " ");
    }
    int idx = 0;
    struct TreeNode* root = buildTree(arr, &idx);
    printf("%d", countSymmetric(root));
    return 0;
}