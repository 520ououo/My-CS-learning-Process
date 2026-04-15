#include <cstdio>
#include <cstdlib>

#define MAXSIZE 100

typedef struct {
    int data[MAXSIZE];
    int top;
} Stack;

typedef struct {
    int data[MAXSIZE];
    int front, rear;
} Queue;

void initStack(Stack *s) {
    s->top = -1;
}

void push(Stack *s, int val) {
    s->data[++(s->top)] = val;
}

int pop(Stack *s) {
    return s->data[(s->top)--];
}

int peek(Stack *s) {
    return s->data[s->top];
}

int isStackEmpty(Stack *s) {
    return s->top == -1;
}

void initQueue(Queue *q) {
    q->front = q->rear = 0;
}

void enQueue(Queue *q, int val) {
    q->data[q->rear++] = val;
}

int deQueue(Queue *q) {
    return q->data[q->front++];
}

int isQueueEmpty(Queue *q) {
    return q->front == q->rear;
}

int main() {
    Stack s1, s2;
    Queue q;
    initStack(&s1);
    initStack(&s2);
    initQueue(&q);

    int val;
    char ch;

    while (scanf("%d%c", &val, &ch) == 2) {
        push(&s1, val);
        if (ch == '\n') break;
    }

    while (scanf("%d%c", &val, &ch) == 2) {
        push(&s2, val);
        if (ch == '\n' || ch == '\r') break;
    }

    while (!isStackEmpty(&s1) && !isStackEmpty(&s2)) {
        if (peek(&s1) < peek(&s2)) {
            enQueue(&q, pop(&s1));
        } else {
            enQueue(&q, pop(&s2));
        }
    }

    while (!isStackEmpty(&s1)) enQueue(&q, pop(&s1));
    while (!isStackEmpty(&s2)) enQueue(&q, pop(&s2));

    while (!isQueueEmpty(&q)) {
        printf("%d ", deQueue(&q));
    }
    printf("\n");

    return 0;
}