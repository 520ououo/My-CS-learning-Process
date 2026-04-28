//从前序与中序遍历序列构造二叉树
#include <stdio.h>
#include <stdlib.h>

struct TreeNode {
    int val;
    struct TreeNode *left;
    struct TreeNode *right;
};

struct TreeNode* buildTree(int* preorder, int preorderSize, int* inorder, int inorderSize)
{
    if (preorderSize == 0) return NULL;

    struct TreeNode* root = (struct TreeNode*)malloc(sizeof(struct TreeNode));
    root->val = preorder[0];
    root->left = NULL;
    root->right = NULL;

    int rootIndex = 0;
    while (rootIndex < inorderSize && inorder[rootIndex] != preorder[0]) {
        rootIndex++;
    }

    int leftSize = rootIndex;
    int rightSize = inorderSize - leftSize - 1;

    root->left = buildTree(preorder + 1, leftSize, inorder, leftSize);
    root->right = buildTree(preorder + 1 + leftSize, rightSize, inorder + leftSize + 1, rightSize);

    return root;
}

void printTree(struct TreeNode* root)
{
    if (root == NULL) return;

    struct TreeNode** queue = (struct TreeNode**)malloc(10000 * sizeof(struct TreeNode*));
    int front = 0, rear = 0;
    queue[rear++] = root;

    while (front < rear) {
        struct TreeNode* node = queue[front++];
        if (node != NULL) {
            printf("%d ", node->val);
            queue[rear++] = node->left;
            queue[rear++] = node->right;
        } else {
            printf("null ");
        }
    }

    free(queue);
}

int main()
{
    int preorderSize;
    scanf("%d", &preorderSize);
    int* preorder = (int*)malloc(preorderSize * sizeof(int));
    for (int i = 0; i < preorderSize; i++) {
        scanf("%d", &preorder[i]);
    }

    int inorderSize = preorderSize;
    int* inorder = (int*)malloc(inorderSize * sizeof(int));
    for (int i = 0; i < inorderSize; i++) {
        scanf("%d", &inorder[i]);
    }

    struct TreeNode* root = buildTree(preorder, preorderSize, inorder, inorderSize);
    printTree(root);

    free(preorder);
    free(inorder);
    return 0;
}