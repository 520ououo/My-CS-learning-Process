#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    char bookId[20];
    char title[100];
    char author[50];
    int stock;
} Book;

typedef struct Node {
    Book book;
    struct Node *next;
} Node;

Node* CreateNode(Book book) {
    Node *newNode = (Node*)malloc(sizeof(Node));
    if (!newNode) return NULL;
    newNode->book = book;
    newNode->next = NULL;
    return newNode;
}

void InsertBook(Node **head, Book book) {
    Node *newNode = CreateNode(book);
    if (*head == NULL) {
        *head = newNode;
        return;
    }
    Node *p = *head;
    while (p->next != NULL) {
        p = p->next;
    }
    p->next = newNode;
}

int DeleteBook(Node **head, char bookId[]) {
    if (*head == NULL) return 0;
    Node *p = *head;
    Node *pre = NULL;
    if (strcmp(p->book.bookId, bookId) == 0) {
        *head = p->next;
        free(p);
        printf("图书%s删除成功!\n", bookId);
        return 1;
    }
    while (p != NULL && strcmp(p->book.bookId, bookId) != 0) {
        pre = p;
        p = p->next;
    }
    if (p == NULL) return 0;
    pre->next = p->next;
    free(p);
    printf("图书%s删除成功!\n", bookId);
    return 1;
}

int UpdateStock(Node *head, char bookId[], int newStock) {
    Node *p = head;
    while (p != NULL) {
        if (strcmp(p->book.bookId, bookId) == 0) {
            p->book.stock = newStock;
            printf("图书%s的库存数量已修改为%d!\n", bookId, newStock);
            return 1;
        }
        p = p->next;
    }
    return 0;
}

Node* FindBook(Node *head, char bookId[]) {
    Node *p = head;
    while (p != NULL) {
        if (strcmp(p->book.bookId, bookId) == 0) {
            printf("查找的图书信息:\n");
            printf("书号:%s,书名:%s,作者:%s,库存:%d\n",
                   p->book.bookId, p->book.title, p->book.author, p->book.stock);
            return p;
        }
        p = p->next;
    }
    return NULL;
}

void TraverseList(Node *head) {
    printf("图书列表:\n");
    Node *p = head;
    while (p != NULL) {
        printf("书号:%s,书名:%s,作者:%s,库存:%d\n",
               p->book.bookId, p->book.title, p->book.author, p->book.stock);
        p = p->next;
    }
}

int main() {
    Node *head = NULL;
    char bookId_find[4], bookId_update[4], bookId_delete[4];
    int num;
    scanf("%s", bookId_find);
    scanf("%s", bookId_update);
    scanf("%d", &num);
    scanf("%s", bookId_delete);

    Book book1 = {"001", "C程序设计", "谭浩强", 10};
    Book book2 = {"002", "数据结构", "严蔚敏", 5};
    Book book3 = {"003", "算法导论", "Thomas H. Cormen", 3};
    InsertBook(&head, book1);
    InsertBook(&head, book2);
    InsertBook(&head, book3);

    FindBook(head, bookId_find);
    UpdateStock(head, bookId_update, num);
    DeleteBook(&head, bookId_delete);
    TraverseList(head);

    return 0;
}