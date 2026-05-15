#include <stdio.h>
#include "ohos_init.h"
#include "ohos_types.h"

void HelloWorld(void)
{
    // 个性化你的HelloWorld消息
    printf("\n=======================\n");
    printf("  OpenHarmony Hello\n");
    printf("  学号:2024311120\n");
    printf("  姓名:黄嘉康\n");
    printf("=======================\n\n");
}
APP_FEATURE_INIT(HelloWorld);