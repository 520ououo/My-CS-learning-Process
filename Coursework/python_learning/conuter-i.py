count = 0
with open("an_essay.md","r") as f :
    for line in f.readlines() :
        line = line.strip()
        list = line.split (" ")
        for I in list :
            count +=1
print(count)

