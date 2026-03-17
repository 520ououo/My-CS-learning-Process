def todo_manager():
    tasks = []  # 使用列表存储任务，保持添加顺序
    history = set()  # 使用集合存储已出现过的任务，用于快速去重和查询

    print("--- 简易任务管理器 ---")
    print("输入 'show' 查看列表, 'quit' 退出, 直接输入内容添加任务。")

    while True:
        user_input = input("\n请输入操作或任务内容: ").strip()

        if user_input.lower() == 'quit':
            print("程序已退出，祝你工作顺利！")
            break
        
        elif user_input.lower() == 'show':
            if not tasks:
                print("当前清单为空。")
            else:
                print("\n当前待办清单：")
                # 使用 enumerate 获取带编号的列表
                for i, task in enumerate(tasks, 1):
                    print(f"{i}. {task}")
        
        elif user_input == "":
            print("输入不能为空，请重新输入。")
            
        else:
            # 利用集合的 O(1) 查询效率，快速判断是否重复
            if user_input in history:
                print(f"提示：任务 '{user_input}' 已经在清单中，无需重复添加。")
            else:
                tasks.append(user_input)
                history.add(user_input)
                print(f"已添加任务: {user_input}")

if __name__ == "__main__":
    todo_manager()
