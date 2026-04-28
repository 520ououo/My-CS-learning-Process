//二叉搜索树染色
#include <stdio.h>
#include <stdlib.h>

struct TreeNode {
    int val;
    struct TreeNode *left;
    struct TreeNode *right;
 };

static int count = 0;

void collectVals(struct TreeNode* root, int* vals, int* idx) {
    if (!root) return;
    collectVals(root->left, vals, idx);
    vals[(*idx)++] = root->val;
    collectVals(root->right, vals, idx);
}

int getNodeCount(struct TreeNode* root) {
    if (!root) return 0;
    return 1 + getNodeCount(root->left) + getNodeCount(root->right);
}

/******************* 染色 *******************/
void getNumber(struct TreeNode* root, int** ops, int opsSize){
    int n = getNodeCount(root);
    if (n == 0) {
        printf("0\n");
        return;
    }
    int* vals = (int*)malloc(n * sizeof(int));
    int idx = 0;
    collectVals(root, vals, &idx);
    
    int* color = (int*)calloc(n, sizeof(int));
    for (int i = 0; i < opsSize; i++) {
        int type = ops[i][0];
        int x = ops[i][1];
        int y = ops[i][2];
        int l = 0, r = n - 1;
        int left = n, right = -1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (vals[mid] >= x) {
                left = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        l = 0, r = n - 1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (vals[mid] <= y) {
                right = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        if (left > right) continue;
        int c = (type == 1) ? 1 : 0;
        for (int j = left; j <= right; j++) {
            color[j] = c;
        }
    }
    int res = 0;
    for (int i = 0; i < n; i++) {
        if (color[i] == 1) res++;
    }
    printf("%d\n", res);
    free(vals);
    free(color);
}
/*****************************************************/

/******************* 读取数据 *******************/
struct TreeNode* newTreeNode(int val) {
    struct TreeNode* node = (struct TreeNode*)malloc(sizeof(struct TreeNode));
    node->val = val;
    node->left = node->right = NULL;
    return node;
}

struct TreeNode* constructTree(int size) {
    if (size == 0)
        return NULL;

    struct TreeNode** nodes = (struct TreeNode**)malloc(size * sizeof(struct TreeNode*));
    for (int i = 0; i < size; i++) {
        int val;
        scanf("%d", &val);
        if (val == -1) {
            nodes[i] = NULL;
        } else {
            nodes[i] = newTreeNode(val);
        }
    }

    for (int i = 0, j = 1; j < size; i++) {
        if (nodes[i] != NULL) {
            if (j < size)
                nodes[i]->left = nodes[j++];
            if (j < size)
                nodes[i]->right = nodes[j++];
        }
    }

    struct TreeNode* root = nodes[0];
    free(nodes);
    return root;
}

void readOps(int ***ops, int *opsSize) {
    scanf("%d", opsSize);

    *ops = (int **)malloc(*opsSize * sizeof(int *));
    while(getchar() != '[') {}
    for (int i = 0; i < *opsSize; i++) {
        (*ops)[i] = (int *)malloc(3 * sizeof(int));
        while(getchar() != '[') {}
        for (int j = 0; j < 3; j++) {
            scanf("%d", &((*ops)[i][j]));
        }
        while(getchar() != ']') {}
    }
}
/*****************************************************/

int main() {
    int nodeSize;
    scanf("%d", &nodeSize);
    struct TreeNode* root = constructTree(nodeSize);
    int **ops, opsSize;
    readOps(&ops, &opsSize);
    getNumber(root, ops, opsSize);

    return 0;
}