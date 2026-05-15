#include <stdio.h>
#include <unistd.h>

#include "ohos_init.h"
#include "cmsis_os2.h"

#define QUEUE_SIZE 3

/* 步骤 1：修改消息结构体，添加图片中要求的字段 */
typedef struct {
    int msg_id;               // 消息ID
    osMessageQueueId_t mq_id; // 消息队列句柄
    uint32_t send_time;       // 新增：发送时的时间戳（单位：Tick）
    osThreadId_t tid;         // 保持原有的发送者线程ID
    int count;                // 保持原有的计数值
} message_entry;

osMessageQueueId_t qid;

void sender_thread(void *arg) {
    static int count = 0;
    message_entry sentry;
    (void)arg;
    while(1) {
        sentry.tid = osThreadGetId();
        sentry.count = count;
        
        /* 步骤 2：在发送消息前调用 osKernelGetTickCount() 记录发送时间戳 */
        sentry.send_time = osKernelGetTickCount();
        
        /* 按照步骤 5 的预期输出格式，修改发送端的打印日志 */
        printf("[Message Test] %s send %d to message queue (send_time: %d).\r\n", 
               osThreadGetName(osThreadGetId()), count, sentry.send_time);
               
        osMessageQueuePut(qid, (const void *)&sentry, 0, osWaitForever);
        count++;
        osDelay(5);
    }
}

void receiver_thread(void *arg) {
    (void)arg;
    message_entry rentry;
    while(1) {
        osMessageQueueGet(qid, (void *)&rentry, NULL, osWaitForever);
        
        /* 步骤 3：接收消息后获取当前 Tick，并计算接收延迟 */
        uint32_t recv_time = osKernelGetTickCount();
        uint32_t delay_ticks = recv_time - rentry.send_time; // 计算 Tick 差值
        
        /* 根据 1 Tick = 10ms 的条件，直接将 Tick 差值乘以 10 换算为毫秒 */
        uint32_t delay_ms = delay_ticks * 10;
        
        /* 按照步骤 5 的预期输出格式，修改接收端的打印日志 */
        printf("[Message Test] %s get %d from %s (send_time: %d, delay: %dms)\r\n", 
               osThreadGetName(osThreadGetId()), rentry.count, osThreadGetName(rentry.tid), rentry.send_time, delay_ms);
               
        osDelay(3);
    }
}

osThreadId_t newThread(char *name, osThreadFunc_t func, void *arg) {
    osThreadAttr_t attr = {
        name, 0, NULL, 0, NULL, 1024*2, osPriorityNormal, 0, 0
    };
    osThreadId_t tid = osThreadNew(func, arg, &attr);
    if (tid == NULL) {
        printf("[Message Test] osThreadNew(%s) failed.\r\n", name);
    } else {
        printf("[Message Test] osThreadNew(%s) success, thread id: %d.\r\n", name, tid);
    }
    return tid;
}

void rtosv2_msgq_main(void *arg) {
    (void)arg;

    qid = osMessageQueueNew(QUEUE_SIZE, sizeof(message_entry), NULL);

    osThreadId_t ctid1 = newThread("recevier1", receiver_thread, NULL);
    osThreadId_t ctid2 = newThread("recevier2", receiver_thread, NULL);
    osThreadId_t ptid1 = newThread("sender1", sender_thread, NULL);
    osThreadId_t ptid2 = newThread("sender2", sender_thread, NULL);
    osThreadId_t ptid3 = newThread("sender3", sender_thread, NULL);

    osDelay(20);
    uint32_t cap = osMessageQueueGetCapacity(qid);
    printf("[Message Test] osMessageQueueGetCapacity, capacity: %d.\r\n", cap);
    uint32_t msg_size = osMessageQueueGetMsgSize(qid);
    printf("[Message Test] osMessageQueueGetMsgSize, size: %d.\r\n", msg_size);
    uint32_t count = osMessageQueueGetCount(qid);
    printf("[Message Test] osMessageQueueGetCount, count: %d.\r\n", count);
    uint32_t space = osMessageQueueGetSpace(qid);
    printf("[Message Test] osMessageQueueGetSpace, space: %d.\r\n", space);

    osDelay(80);
    osThreadTerminate(ctid1);
    osThreadTerminate(ctid2);
    osThreadTerminate(ptid1);
    osThreadTerminate(ptid2);
    osThreadTerminate(ptid3);
    osMessageQueueDelete(qid);
}

static void MessageTestTask(void)
{
    osThreadAttr_t attr;

    attr.name = "rtosv2_msgq_main";
    attr.attr_bits = 0U;
    attr.cb_mem = NULL;
    attr.cb_size = 0U;
    attr.stack_mem = NULL;
    attr.stack_size = 1024;
    attr.priority = osPriorityNormal;

    if (osThreadNew((osThreadFunc_t)rtosv2_msgq_main, NULL, &attr) == NULL) {
        printf("[MessageTestTask] Falied to create rtosv2_msgq_main!\n");
    }
}

APP_FEATURE_INIT(MessageTestTask);